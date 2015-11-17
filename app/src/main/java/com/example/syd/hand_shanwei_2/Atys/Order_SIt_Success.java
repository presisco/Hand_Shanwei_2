package com.example.syd.hand_shanwei_2.Atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/16.
 */
public class Order_SIt_Success extends AppCompatActivity {
    TextView order_seat_result_tv,order_infotv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_seat_result_layout);
        Intent intent=getIntent();//包含传过来的预约结果和预约提示信息
        order_infotv= (TextView) findViewById(R.id.order_infotv);
        order_seat_result_tv= (TextView) findViewById(R.id.order_seat_result_tv);
    }
}
