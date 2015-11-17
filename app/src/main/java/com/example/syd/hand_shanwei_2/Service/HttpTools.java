package com.example.syd.hand_shanwei_2.Service;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.List;

/**
 * Created by syd on 2015/11/16.
 */
public class HttpTools {
    public static String getYuYueInfoPost(String path, HttpClient client, List<NameValuePair> postParameters)
    {
        try {
            HttpPost request = new HttpPost(path); // 构建post路径
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters); // 传递post实体数据
            request.setEntity(formEntity); // 设置实体
            HttpResponse res = client.execute(request); // 执行请求
            String str = EntityUtils.toString(res.getEntity(), "UTF-8"); // 获取请求数据
            //EntityUtils.consume(res.getEntity()); // clear实体数据
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取预约信息get提交
     * @param path
     * @param client
     * @return
     */
    public static String getYuYueInfoGet(String path, HttpClient client)
    {
        try {
            HttpGet sitRequest = new HttpGet(new URI(path)); // 构建get路径

            HttpResponse res = client.execute(sitRequest); // 执行请求
            String str = EntityUtils.toString(res.getEntity(), "UTF-8"); // 获取请求数据

            //EntityUtils.consume(res.getEntity()); // clear实体数据

            return str;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
