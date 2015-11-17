package com.example.syd.hand_shanwei_2.HttpService.SearchBookService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.syd.hand_shanwei_2.HttpService.HttpTools;
import com.example.syd.hand_shanwei_2.Model.Book;
import com.example.syd.hand_shanwei_2.Model.BookListState;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by syd on 2015/11/17.
 */
public class SearchBookService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 按标题检索
     */
    public static final String TYPE_TITLE = "title";
    /**
     * 按作者检索
     */
    public static final String TYPE_AUTHOR = "author";

    /**
     * 二次检索，返回HTML页面内容
     *
     * @param onceKeyword
     *            第一次检索的关键字
     * @param onceType
     *            第一次检索的类型，为title或author
     * @param twiceKeyword
     *            第二次检索的关键字
     * @param twiceType
     *            第二次检索类型，为title或author
     * @param page
     *            检索的页数
     * @return HTML内容
     */
    public static String queryTwice(String onceKeyword, String onceType,
                                    String twiceKeyword, String twiceType, int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s2_type", twiceType);
        params.put("s2_text", twiceKeyword);
        params.put("search_bar", "result");
        params.put(onceType, onceKeyword);
        params.put("page", page + "");
        return HttpTools.getContent(
                "http://202.194.40.71:8080/opac/openlink.php", params);
    }

    /**
     * 根据书名，获取检索页面HTML
     *
     * @param title
     *            书名
     * @param type
     *            检索类别，title为按标题，author为按作者
     * @param page
     *            当前页
     * @return HTML内容
     */
    public static String getPage(String title, String type, int page) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("strSearchType", type);
        params.put("strText", title);
        params.put("page", page + "");
        return HttpTools.getContent(
                "http://202.194.40.71:8080/opac/openlink.php", params);
    }
    /**
     * 获取楼层，使用book对象的macrono作为参数
     *
     * @param marcno
     *            书目URL id
     * @return 楼层
     */
    public static String getLayer(String marcno) throws Exception {
        Document document = Jsoup.parse(new URL(
                "http://202.194.40.71:8080/opac/ajax_item.php?marc_no="
                        + marcno), 3000);
        // 书籍信息所在行
        Element tr = document.getElementsByTag("tbody").get(0)
                .getElementsByTag("tr").get(1);
        // 获取楼层单元格数据
        String layer = tr.getElementsByTag("td").get(3).text();
        // 去除文字
        if(layer.contains("文献建设部"))
            return layer.substring(5);
        else return layer.replaceAll("[^0-9]*", "")+"楼";

    }
    /**
     * 获取图书的相关信息，例如图书名称，版本号，详细信息
     * @param httpContent
     * @return
     */
    public static List<Book> getBookList(String httpContent) {
        List<Book> result = new ArrayList<Book>();
        Document doc = Jsoup.parse(httpContent);
        Elements booklist = doc.getElementsByClass("book_list_info");
        if (booklist.size() == 0)
            return null;
        for (Element bookitem : booklist) {
            Book book = new Book();
            book.setName(bookitem.getElementsByTag("a").get(0).text()
                    .replaceFirst("\\d+\\.", ""));
            book.setCode(bookitem.getElementsByTag("h3").get(0).ownText());
            book.setDetail(bookitem.getElementsByTag("p").get(0).ownText()
                    .replaceFirst("\\(\\d+\\)", "").trim());
            String temp = bookitem.getElementsByTag("p").get(0)
                    .getElementsByTag("span").get(0).ownText();
            book.setTotal(Integer.parseInt(temp
                    .substring(0, temp.indexOf("可借")).replace("馆藏复本：", "")
                    .trim()));
            book.setRest(Integer.parseInt(temp.substring(temp.indexOf("可借"))
                    .replace("可借复本：", "").trim()));
            String marcno = bookitem.getElementsByTag("h3").get(0)
                    .getElementsByTag("a").get(0).attr("href")
                    .replace("item.php?marc_no=", "");
            book.setMarcno(marcno);
            result.add(book);
        }
        return result;
    }
    /**
     * 获取搜索图书结果的页数信息
     * @param httpContent  解析到的HTML文件
     * @return
     */
    public static BookListState getListState(String httpContent) {
        Document doc = Jsoup.parse(httpContent);
        Elements stateDiv = doc.getElementsByClass("num_prev").get(0)
                .getElementsByTag("b");
        if (stateDiv.size() == 0)
            return new BookListState(1, 1);
        String currNum = stateDiv.get(0)
                .getElementsByAttributeValue("color", "red").get(0).ownText();
        String totalNum = stateDiv.get(0)
                .getElementsByAttributeValue("color", "black").get(0).ownText();
        return new BookListState(Integer.parseInt(currNum),
                Integer.parseInt(totalNum));
    }
}
