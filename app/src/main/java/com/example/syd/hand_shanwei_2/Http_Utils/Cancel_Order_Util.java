package com.example.syd.hand_shanwei_2.Http_Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.syd.hand_shanwei_2.Atys.HomeActivity;

import org.apache.http.client.HttpClient;

import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;

/**
 * Created by syd on 2015/11/16.
 * 取消预约座位
 */
public class Cancel_Order_Util {
    //取消预约返回状态
    private int res;
    private Context context;
    public static final int NO_ORDER_HISTORY=0;//之前没有过预约
    public static final int CANCEL_FAIL=-1;//之前有过预约,但是解约失败
    public static final int CANCEL_SUCCESS=1;//之前有过预约,解约成功
    private static final int HAS_CANCED =2;//之前有过预约,已经在别处取消
    //是否取消成功
    private boolean isCancelSuccess=false;
    private Handler handler;
    public Cancel_Order_Util(Context context){
        this.context=context;
         handler=new Handler(){
             @Override
             public void handleMessage(Message msg) {
                 switch (msg.what){
                     case NO_ORDER_HISTORY:
                         res=0;
                         break;
                     case CANCEL_FAIL:
                         res=-1;
                         break;
                     case CANCEL_SUCCESS:
                         res=1;
                         break;
                     case HAS_CANCED:
                         res=2;
                         break;
                 }
             }
         };
    }
    //取消预约
    public int Cancel(HttpClient httpClient){
        //先判断是不是有过预约
        UserinfoUtils userinfoUtils=new UserinfoUtils(context);
            if (!userinfoUtils.has_Order_History()){//之前没有预约记录
                return NO_ORDER_HISTORY;
            }else {
                 new Thread(){
                @Override
                public void run() {
                    String cancelres= OrderSeatService.Cancel(OrderSeatService.coreClient);
                    if (cancelres.contains("success")){//取消成功
                        handler.sendEmptyMessage(CANCEL_SUCCESS);
                    }else if (cancelres.contains("empty")){//已经在别处取消
                        handler.sendEmptyMessage(HAS_CANCED);
                    }else {
                        handler.sendEmptyMessage(CANCEL_FAIL);
                    }

                    }
                 }.start();
            }
        return res;
    }

}
