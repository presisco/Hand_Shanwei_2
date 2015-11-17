package com.example.syd.hand_shanwei_2.Http_Utils;

import android.os.Handler;
import android.os.Message;

import com.example.syd.hand_shanwei_2.Service.YuyueService;

/**
 * Created by syd on 2015/11/15.
 */
public class Login_Util {
    Handler handler;
    //登陆是否成功
    boolean islog_in_Success =false;
    public Login_Util(){
    }
    public boolean login(final String id, final String password){
         handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
               islog_in_Success =true;
            }

        };
        //验证登陆信息是否符合,验证成功则发送handler消息
        new Thread(){
            @Override
            public void run() {
                //调用服务器验证信息接口
                YuyueService yuyueService=new YuyueService();
                try {
                    yuyueService.testUserInfoIsTrue(id,password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x123);
            }
        }.start();
        return islog_in_Success;
    }
}
