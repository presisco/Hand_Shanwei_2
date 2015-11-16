package Service;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syd on 2015/11/16.
 */
public class YuyueTools {
    /**
     * 解析指定楼层的座位信息获得座位
     */
    public static List<String> parseSitsInfo(String s)
    {
        String tag1 = "text-align: center; margin-left: 0; margin-top: 0\">";

        List<String> lists = new ArrayList<String>();

        String subStr = s; // 原串

        int index = subStr.indexOf(tag1);

        while(index != -1)
        {
            String val = subStr.substring(index + tag1.length() + 30, index + tag1.length() + 33);

            lists.add(val);

            subStr = subStr.substring(index + tag1.length() + 33);
            index = subStr.indexOf(tag1);
        }

        return lists;
    }

    /**
     * 得到预约信息
     * @param client
     * @return
     * @throws Exception
     */
    public static List<String> getYuYueInfo(HttpClient client, String room, String date) throws Exception
    {
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        // 构建实体数据
        postParameters.add(new BasicNameValuePair("subCmd", "query"));
        postParameters.add(new BasicNameValuePair("selReadingRoom", room));

        postParameters.add(new BasicNameValuePair("txtBookDate", date));
        postParameters.add(new BasicNameValuePair("hidBookDate", ""));
        postParameters.add(new BasicNameValuePair("hidRrId", ""));
        postParameters.add(new BasicNameValuePair("__EVENTVALIDATION","/wEWBQKn3LCMAgKP25+PBwKozKffBwKe8pjrCQLbhOG0Ag8P+D24L0yDPqWsH9l8fvFZfIvf5D0SWty7+UgyaODK"));
        postParameters.add(new BasicNameValuePair("__VIEWSTATE","/wEPDwUKLTgxNTcwOTg4OQ9kFgICAw9kFgYCBw8QZA8WDGYCAQICAgMCBAIFAgYCBwIIAgkCCgILFgwQBQbkuInmpbwFBjAwMDEwM2cQBQblm5vmpbwFBjAwMDEwNGcQBQbkupTmpbwFBjAwMDEwNWcQBQblha3mpbwFBjAwMDEwNmcQBQbkuIPmpbwFBjAwMDEwN2cQBQblhavmpbwFBjAwMDEwOGcQBQbkuZ3mpbwFBjAwMDEwOWcQBQbljYHmpbwFBjAwMDExMGcQBQnljYHkuIDmpbwFBjAwMDExMWcQBQnljYHkuozmpbwFBjAwMDExMmcQBRLlm77kuJznjq/mpbzkuInmpbwFBjAwMDIwM2cQBRLlm77kuJznjq/mpbzlm5vmpbwFBjAwMDIwNGdkZAIJDxYEHglpbm5lcmh0bWxlHgdWaXNpYmxlaGQCCw88KwAJAGRk3N7Y/deh68M4Efs081yDORR8De16X09J94eraQdd4ek="));

        // 发送post请求
        String yuyueInfo = HttpTools.getYuYueInfoPost("http://yuyue.juneberry.cn/BookSeat/BookSeatListForm.aspx", client, postParameters);

        // 座号list
        List<String> lists = YuyueTools.parseSitsInfo(yuyueInfo);

        return lists;
    }

    /**
     * 根据s获取属性值
     * @param s
     * @param beginStr
     * @param endStr
     * @return
     */
    public static String getEandVAttri(String s, String beginStr, String endStr)
    {
        String sub_s = s;

        int b_index = sub_s.indexOf(beginStr) + beginStr.length();
        sub_s = sub_s.substring(b_index);

        int e_index = sub_s.indexOf(endStr);

        return sub_s.substring(0, e_index);
    }

    /**
     * 解析属性
     * @param s
     */
    public static String parseEandVAttri(String s) {

        String __VIEWSTATE = getEandVAttri(s, "id=\"__VIEWSTATE\" value=\"", "\" />");
        String __EVENTVALIDATION = getEandVAttri(s, "id=\"__EVENTVALIDATION\" value=\"", "\" />");

        System.out.println("__VIEWSTATE: " + __VIEWSTATE);
        System.out.println("__EVENTVALIDATION: " + __EVENTVALIDATION);

        String val = __VIEWSTATE + "," + __EVENTVALIDATION;

        return val;
    }

    /**
     * 提交预约信息
     * @param client
     * @return
     * @throws Exception
     */
    public static String subYuYueInfo(HttpClient client, String room, String sitNo, String date) throws Exception
    {
        // 发送get请求
        String reqUrl = "http://yuyue.juneberry.cn/BookSeat/BookSeatMessage.aspx?seatNo=" + room + sitNo + "&seatShortNo=" + sitNo + "&roomNo=" + room + "&date=" + date;

//		System.out.println(reqUrl);

        String s = HttpTools.getYuYueInfoGet(reqUrl, client);

//		System.out.println("s:" + s);

        // 需要解析出__EVENTVALIDATION和__VIEWSTATE字段
        String []strs = parseEandVAttri(s).split(",");

        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        // 构建实体数据
        postParameters.add(new BasicNameValuePair("subCmd", "query"));
        postParameters.add(new BasicNameValuePair("__EVENTVALIDATION", strs[1]));
        postParameters.add(new BasicNameValuePair("__VIEWSTATE", strs[0]));

        // 发送post请求
        String res = HttpTools.getYuYueInfoPost(reqUrl, client, postParameters);
        return res;
    }
}
