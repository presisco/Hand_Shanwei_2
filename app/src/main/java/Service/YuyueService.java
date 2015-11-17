package Service;

import com.example.syd.hand_shanwei_2.Atys.HomeActivity;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by syd on 2015/11/16.
 */
public class YuyueService {
    /**
     * 查询用户名密码是否正确 实现
     */
    public YuyueService(){

    }
    public boolean testUserInfoIsTrue(String userId, String passWd)
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
        String s = HttpTools.getYuYueInfoPost(
                    "http://yuyue.juneberry.cn/Login.aspx", client, postParameters);

        // 登录失败
        if (s.contains("登录失败:用户名或密码错误")) {
            return false;
        } else {
            HomeActivity.client = client;
        }

        return true;
    }

    /**
     * 一键抢座业务实现
     */
    public String OneKeySit(HttpClient client) throws Exception {

        // 获取当前日期+1
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date beginDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        String date = format.format(calendar.getTime());

        // 发送get请求
        HttpTools.getYuYueInfoGet(
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

        List<String> list = YuyueTools.getYuYueInfo(client, "000" + room, date); // 获取该房间的列表

        if (0 == list.size()) {
            // 没有可用座位
            return "empty";
        } else {
            // 获取座位
            String sitNo = list.get(random.nextInt(list.size()));

            // sitNo = "25";

            String res = YuyueTools.subYuYueInfo(client, "000" + room, sitNo,
                    date);

            // System.out.println(res);
            if (res.contains("已经存在有效的预约记录")) {
                return "fail";
            }

            String info = "<infos>";

            info += "<info><lblReadingRoomName>"
                    + room.charAt(room.length() - 1) + "楼</lblReadingRoomName>"
                    + "<lblSeatNo>" + sitNo + "</lblSeatNo>" + "<lblBookDate>"
                    + date + "</lblBookDate>" + "<lbComformTime>7:50至8:35"
                    + "</lbComformTime></info>";

            info += "</infos>";

            return info;
        }

    }
    /**
     * 取消功能
     */
    public boolean Cancel(HttpClient client) {
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
        String s = HttpTools.getYuYueInfoPost(
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
        s = HttpTools.getYuYueInfoPost(
                "http://yuyue.juneberry.cn/UserInfos/QueryLogs.aspx", client,
                postParameters);
        if (s.contains("alert('成功取消预约！')"))
            return true;
        else
            return false;
    }
}
