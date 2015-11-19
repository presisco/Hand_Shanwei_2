package com.example.syd.hand_shanwei_2.Atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.Http_Utils.Login_Util;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/12.
 */
public class WelcomeAty extends Activity {
    //欢迎界面停留时间
    private static final int TIME_DELAY=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        //到时间后进入主页
        handler.sendEmptyMessageDelayed(0x123,TIME_DELAY);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //判断是否已经登陆，如果没有登陆，则进入登陆界面
//            UserinfoUtils userinfoUtils =new UserinfoUtils(WelcomeAty.this);
            /*if (!userinfoUtils.get_Login_Status()){
                Toast.makeText(WelcomeAty.this,"请先登录",Toast.LENGTH_LONG).show();
                goAtyLogin();
            }else {
                goHome();//进入程序主界面
            }*/
            goHome();
        }


    };
   /* private void goAtyLogin() {
        Intent intent=new Intent(WelcomeAty.this,Aty_LogIn.class);
        startActivity(intent);
        finish();
    }*/

    private void goHome() {
        Intent intent=new Intent(WelcomeAty.this,HomeActivity.class);
        startActivity(intent);
        finish();//结束当前的activity
    }
}
