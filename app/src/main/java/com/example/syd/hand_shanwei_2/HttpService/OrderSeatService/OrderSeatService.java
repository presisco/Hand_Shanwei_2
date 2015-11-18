package com.example.syd.hand_shanwei_2.HttpService.OrderSeatService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.syd.hand_shanwei_2.HttpService.HttpConnectionService;
import com.example.syd.hand_shanwei_2.HttpService.HttpTools;
import com.example.syd.hand_shanwei_2.Model.FloorInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by syd on 2015/11/17.
 */
public class OrderSeatService extends Service{
    public static HttpClient coreClient;
    /**
     * Called by the system when the service is first created.  Do not call this method directly.
     */


    public static boolean testUserInfoIsTrue(String userId, String passWd)
            throws Exception {

        HttpClient client = new DefaultHttpClient(); // 链接

        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        // 构建实体数据
        postParameters.add(new BasicNameValuePair("subCmd", "Login"));
        postParameters.add(new BasicNameValuePair("txt_LoginID", userId));
        postParameters.add(new BasicNameValuePair("txt_Password", passWd));
        postParameters.add(new BasicNameValuePair("selSchool", "15"));
        postParameters
                .add(new BasicNameValuePair(
                        "__EVENTVALIDATION",
                        "/wEWBQLovZzDCwK1lMLgCgLS9cL8AgKA8vrfDwKXzJ6eD7a6jXrjAPWziXt2/r5XJ+4s9/usHi9Ol6zTksTCuM5e"));
        postParameters
                .add(new BasicNameValuePair(
                        "__VIEWSTATE",
                        "/wEPDwUKMTQzNzIwMTA1OA9kFgICAQ9kFgICBw8QZA8WDGYCAQICAgMCBAIFAgYCBwIIAgkCCgILFgwQBREt6K+36YCJ5oup5a2m5qChLQUCLTFnEAUU5bGx5Lic5aSn5a2mKOWogea1tykFAjE1ZxAFEueUteWtkOenkeaKgOWkp+WtpgUCMTZnEAUM5Y6m6Zeo5aSn5a2mBQIxN2cQBRLkuK3lm73kurrmsJHlpKflraYFAjIwZxAFEuWMl+S6rOS6pOmAmuWkp+WtpgUCNjBnEAUS5q2m5rGJ55CG5bel5aSn5a2mBQI2M2cQBQ/kuIrmtbfllYblrabpmaIFAjY1ZxAFEuS4reWbveaUv+azleWkp+WtpgUCNjlnEAUM5b6u5L+h5rWL6K+VBQI4MGcQBRLnrKzkuozlhpvljLvlpKflraYFAjg0ZxAFGOa1i+ivleWtpuagoe+8iOS7mem5pO+8iQUCODVnZGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFEGNoa19SZW1QYXNzcHdvcmT0WPLpAo3Ccrt09bJcgbY4R1Q2qHbhQLnlRORyTkXMkQ=="));

        // 发送post请求
        String s = HttpTools.PostHTTPRequest(
                "http://yuyue.juneberry.cn/Login.aspx", client, postParameters);

        // 登录失败
        if (s.contains("登录失败:用户名或密码错误")) {
            return false;
        } else {
            OrderSeatService.coreClient = client;
        }

        return true;
    }

    /**
     * 一键抢座业务实现
     */
    public static String OneKeySit(HttpClient client) throws Exception {

        // 获取当前日期+1
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        String date = format.format(calendar.getTime());

        // 发送get请求
        HttpTools.GetHTTPRequest(
                "http://yuyue.juneberry.cn/BookSeat/BookSeatListForm.aspx",
                client);

        // 随机生成room号
        String[] rooms = { "103", "104", "105", "106", "107", "108", "109",
                "110", "111", "112", "203", "204" };
        Random random = new Random();
        int index = random.nextInt(rooms.length); // 随机生成一个
        String room = rooms[index]; // 获取房间No
        // System.out.println("room：" + room);

        // 测试，他妈的座位都被抢光了。
        // room = "103";
        List<String> list =getYuYueInfo(client, "000" + room, date); // 获取该房间的列表
        System.out.println(list);
        if (0 == list.size()) {
            // 没有可用座位
            return "empty";
        } else {
            // 获取座位
            String sitNo = list.get(random.nextInt(list.size()));
            // sitNo = "25";
            String res = subYuYueInfo(client, "000" + room, sitNo,
                    date);
            // System.out.println(res);
            if (res.contains("已经存在有效的预约记录")) {
                return "fail";
            }

           /* String info = "<infos>";

            info += "<info><lblReadingRoomName>"
                    + room.charAt(room.length() - 1) + "楼</lblReadingRoomName>"
                    + "<lblSeatNo>" + sitNo + "</lblSeatNo>" + "<lblBookDate>"
                    + date + "</lblBookDate>" + "<lbComformTime>7:50至8:35"
                    + "</lbComformTime></info>";

            info += "</infos>";*/
            String s=room.charAt(room.length() - 1) +"楼"+sitNo+"座位号"+"日期："+date;
            return s;
        }

    }
    /**
     * 取消功能
     */
    public static boolean Cancel(HttpClient client) {
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        // 构建实体数据
        postParameters.add(new BasicNameValuePair("subCmd", ""));
        postParameters.add(new BasicNameValuePair("subBookNo", ""));
        postParameters.add(new BasicNameValuePair("chooseDate", "选择日期"));
        postParameters.add(new BasicNameValuePair("ddlDate", "7"));
        postParameters.add(new BasicNameValuePair("ddlRoom", "-1"));
        postParameters
                .add(new BasicNameValuePair(
                        "__EVENTVALIDATION",
                        "/wEWBQLvts3VCALgu8z3BgKk+56eBwLMgJnOCQLytq6nDxwZ8op+k3QYxYJeMoeP2u+RpfUiaPbfZS4bPn8dP2tI"));
        postParameters
                .add(new BasicNameValuePair(
                        "__VIEWSTATE",
                        "/wEPDwUKMTY3NjM4MDk3NA9kFgICAw9kFgQCBg8QZA8WDAIBAgICAwIEAgUCBgIHAggCCQIKAgsCDBYMEAUG5LiJ5qW8BQYwMDAxMDNnEAUG5Zub5qW8BQYwMDAxMDRnEAUG5LqU5qW8BQYwMDAxMDVnEAUG5YWt5qW8BQYwMDAxMDZnEAUG5LiD5qW8BQYwMDAxMDdnEAUG5YWr5qW8BQYwMDAxMDhnEAUG5Lmd5qW8BQYwMDAxMDlnEAUG5Y2B5qW8BQYwMDAxMTBnEAUJ5Y2B5LiA5qW8BQYwMDAxMTFnEAUJ5Y2B5LqM5qW8BQYwMDAxMTJnEAUS5Zu+5Lic546v5qW85LiJ5qW8BQYwMDAyMDNnEAUS5Zu+5Lic546v5qW85Zub5qW8BQYwMDAyMDRnZGQCBw8WAh4HVmlzaWJsZWhkZONTC/tn2kwfwfZY8epEJotHBLSdf+MCFLNjxCHkx81V"));

        // 1.提交到http://yuyue.juneberry.cn/UserInfos/QueryLogs.aspx表单
        String s = HttpTools.PostHTTPRequest(
                "http://yuyue.juneberry.cn/UserInfos/QueryLogs.aspx", client,
                postParameters);
        // System.out.println(s);
        // 获取subBookNo
        int start = s.indexOf("(&apos;") + 7;
        int end = s.indexOf("&apos;)");
        if (start <= 6 || end <= 0 || start > s.length() || end > s.length()) {
            return false;
        }
        String subBookNo = s.substring(start, end);
        System.out.print("----"+subBookNo+"----");
        postParameters.set(0, new BasicNameValuePair("subCmd", "cancel"));
        postParameters.set(1, new BasicNameValuePair("subBookNo", subBookNo));
        postParameters
                .set(5,
                        new BasicNameValuePair(
                                "__EVENTVALIDATION",
                                "/wEWBQKXkMTsAwLgu8z3BgKk+56eBwLMgJnOCQLytq6nDyhE/jZ/OHteWSojk+uquYn1F9VAILikpoQIYKA/MH+e"));
        postParameters
                .set(6,
                        new BasicNameValuePair(
                                "__VIEWSTATE",
                                "/wEPDwUKMTY3NjM4MDk3NA9kFgICAw9kFgoCAg8WAh4FY2xhc3MFDXVpLWJ0bi1hY3RpdmVkAgMPFgIfAGVkAgQPFgIfAGVkAgYPEGQPFgwCAQICAgMCBAIFAgYCBwIIAgkCCgILAgwWDBAFBuS4iealvAUGMDAwMTAzZxAFBuWbm+alvAUGMDAwMTA0ZxAFBuS6lOalvAUGMDAwMTA1ZxAFBuWFrealvAUGMDAwMTA2ZxAFBuS4g+alvAUGMDAwMTA3ZxAFBuWFq+alvAUGMDAwMTA4ZxAFBuS5nealvAUGMDAwMTA5ZxAFBuWNgealvAUGMDAwMTEwZxAFCeWNgeS4gOalvAUGMDAwMTExZxAFCeWNgeS6jOalvAUGMDAwMTEyZxAFEuWbvuS4nOeOr+alvOS4iealvAUGMDAwMjAzZxAFEuWbvuS4nOeOr+alvOWbm+alvAUGMDAwMjA0Z2RkAgcPFgIeB1Zpc2libGVoZGSjYQhm8YNM5rq82smP8U2T73urvz+iRczip3OAgdSKwA=="));
        // 2.提交到http://yuyue.juneberry.cn/UserInfos/QueryLogs.aspx,带详细信息
        s = HttpTools.PostHTTPRequest(
                "http://yuyue.juneberry.cn/UserInfos/QueryLogs.aspx", client,
                postParameters);
        if (s.contains("alert('成功取消预约！')"))
            return true;
        else
            return false;
    }
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
     * 得到指定房间预约信息,
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
        String yuyueInfo = HttpTools.PostHTTPRequest("http://yuyue.juneberry.cn/BookSeat/BookSeatListForm.aspx", client, postParameters);
        System.out.println(yuyueInfo);
        // 座号list
        List<String> lists =parseSitsInfo(yuyueInfo);
        System.out.println("得到指定房间预约信息返回结果\n"+lists);
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
     * 提交预约信息,精准预约
     * @param client
     * @return
     * @throws Exception
     */
    public static String subYuYueInfo(HttpClient client, String room, String sitNo, String date) throws Exception
    {
        // 发送get请求
        String reqUrl = "http://yuyue.juneberry.cn/BookSeat/BookSeatMessage.aspx?seatNo=" + room + sitNo + "&seatShortNo=" + sitNo + "&roomNo=" + room + "&date=" + date;

//		System.out.println(reqUrl);

        String s = HttpTools.GetHTTPRequest(reqUrl, client);

        System.out.println("s:" + s);

        // 需要解析出__EVENTVALIDATION和__VIEWSTATE字段
        String []strs = parseEandVAttri(s).split(",");

        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();

        // 构建实体数据
        postParameters.add(new BasicNameValuePair("subCmd", "query"));
        postParameters.add(new BasicNameValuePair("__EVENTVALIDATION", strs[1]));
        postParameters.add(new BasicNameValuePair("__VIEWSTATE", strs[0]));

        // 发送post请求
        String res = HttpTools.PostHTTPRequest(reqUrl, client, postParameters);
        System.out.println("subYuYueInfo返回结果：\n" + res);

        return res;
    }
    /**
     * 从网上获取座位信息
     *
     * @return
     *
     * @throws Exception
     */
    public static List<FloorInfo> getFloorInfo(String id,String password) throws Exception {
        HttpClient client = new DefaultHttpClient();
        // 登录
        HttpPost request = new HttpPost("http://yuyue.juneberry.cn/");
        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("subCmd", "Login"));
        postParameters
                .add(new BasicNameValuePair("txt_LoginID", id));
        postParameters.add(new BasicNameValuePair("txt_Password", password));
        postParameters.add(new BasicNameValuePair("selSchool", "15"));
        postParameters
                .add(new BasicNameValuePair(
                        "__EVENTVALIDATION",
                        "/wEWBQLovZzDCwK1lMLgCgLS9cL8AgKA8vrfDwKXzJ6eD7a6jXrjAPWziXt2/r5XJ+4s9/usHi9Ol6zTksTCuM5e"));
        postParameters
                .add(new BasicNameValuePair(
                        "__VIEWSTATE",
                        "/wEPDwUKMTQzNzIwMTA1OA9kFgICAQ9kFgICBw8QZA8WDGYCAQICAgMCBAIFAgYCBwIIAgkCCgILFgwQBREt6K+36YCJ5oup5a2m5qChLQUCLTFnEAUU5bGx5Lic5aSn5a2mKOWogea1tykFAjE1ZxAFEueUteWtkOenkeaKgOWkp+WtpgUCMTZnEAUM5Y6m6Zeo5aSn5a2mBQIxN2cQBRLkuK3lm73kurrmsJHlpKflraYFAjIwZxAFEuWMl+S6rOS6pOmAmuWkp+WtpgUCNjBnEAUS5q2m5rGJ55CG5bel5aSn5a2mBQI2M2cQBQ/kuIrmtbfllYblrabpmaIFAjY1ZxAFEuS4reWbveaUv+azleWkp+WtpgUCNjlnEAUM5b6u5L+h5rWL6K+VBQI4MGcQBRLnrKzkuozlhpvljLvlpKflraYFAjg0ZxAFGOa1i+ivleWtpuagoe+8iOS7mem5pO+8iQUCODVnZGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFEGNoa19SZW1QYXNzcHdvcmT0WPLpAo3Ccrt09bJcgbY4R1Q2qHbhQLnlRORyTkXMkQ=="));
        UrlEncodedFormEntity formEntity = null;
        formEntity = new UrlEncodedFormEntity(postParameters);
        request.setEntity(formEntity);
        client.execute(request);
        // 访问座位页面
        HttpGet sitRequest = new HttpGet(
                new URI(
                        "http://yuyue.juneberry.cn/ReadingRoomInfos/ReadingRoomState.aspx"));
        HttpResponse response = client.execute(sitRequest);
        List<FloorInfo> floorInfos =parseInfo(EntityUtils.toString(response.getEntity(), "UTF-8"));
        return floorInfos;
    }
    /**
    * 解析HTML信息
    *
            * @return
            */
    public static List<FloorInfo> parseInfo(String content) {
        List<FloorInfo> result = new ArrayList<FloorInfo>();
        Document doc = Jsoup.parse(content);
        Elements items = doc.getElementsByAttributeValue("data-theme", "c");
        Log.d("#####", items.toString());
        for (Element item : items) {
            FloorInfo info = new FloorInfo();
            info.layer = item.text().substring(0, item.text().indexOf(':'));
            Elements itemlis = item.getElementsByTag("ul").get(0)
                    .getElementsByTag("li");
            for (Element li : itemlis) {
                if (li.text().startsWith("总座位")) {
                    info.total = Integer
                            .parseInt(li.text().replace("总座位：", ""));
                } else if (li.text().startsWith("空闲")) {
                    info.rest = Integer.parseInt(li.text().replace("空闲：", ""));
                }
            }
            result.add(info);
        }
        return result;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
