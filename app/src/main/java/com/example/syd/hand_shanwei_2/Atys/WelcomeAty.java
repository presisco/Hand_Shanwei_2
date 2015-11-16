package com.example.syd.hand_shanwei_2.Atys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
            goHome();//进入程序主界面
        }


    };
    private void goHome() {
        Intent intent=new Intent(WelcomeAty.this,HomeActivity.class);
        startActivity(intent);
        finish();//结束当前的activity
    }
}
