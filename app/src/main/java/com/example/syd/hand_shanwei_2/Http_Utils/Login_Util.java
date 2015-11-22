package com.example.syd.hand_shanwei_2.Http_Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.Atys.Aty_LogIn;
import com.example.syd.hand_shanwei_2.Atys.HomeActivity;
import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.OrderSeats.SelectSeatActivity;

/**
 * Created by syd on 2015/11/15.
 */
public class Login_Util {
     private  static boolean Threadhas_stop=false;
    static Handler handler;
    private static Context context;
    //登陆是否成功
    static boolean islog_in_Success =false;
    public Login_Util(Context context){
        this.context=context;
    }

}
