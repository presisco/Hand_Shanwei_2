package com.example.syd.hand_shanwei_2.Http_Utils;

import android.content.Context;

import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Model.FloorInfo;

import java.util.List;

/**
 * Created by syd on 2015/11/13.
 */
public class Get_Today_Seats {
    Context context;
    //构造函数
    public Get_Today_Seats(Context context,int Floors){
    }
        //获取各楼层所有座位信息
    public static List<FloorInfo> getFloorInfo(String id,String password ) throws Exception {

        return OrderSeatService.getFloorInfo(id,password);
    }
}
