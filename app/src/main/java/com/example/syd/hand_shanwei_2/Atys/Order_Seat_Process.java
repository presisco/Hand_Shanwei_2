package com.example.syd.hand_shanwei_2.Atys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.BookSeats.SelectSeatActivity;
import com.example.syd.hand_shanwei_2.HttpService.HttpTools;
import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.R;

import org.apache.http.client.HttpClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by syd on 2015/11/16.
 */
public class Order_Seat_Process extends AppCompatActivity {

    TextView order_seat_result_tv,order_infotv;
    Button button;
    public static final int ORDERSTATUS_TIMEOUT =-1;//链接失败
    public static final int ORDERSTATUS_CONECT_SUCCESS =1;//链接成功
    public static final int ORDERSTATUS_HASORDERED =2;//已经存在有效预约
    public static final int ORDERSTATUS_ONEKEY_FAIL =-11;//一键预约失败
    public static final int ORDERSTATUS_ONE_KEY_SUCCESS =11;//一键预约成功
    public static final int ORDERSTATUS_SPECIFIC_SUCCESS =21;//精确预约成功
    public static final int ORDERSTATUS_SPECIFIC_FAIl =-21;//精确预约失败
    public static final int ORDERSTATUS_SPECIFIC_SEAT_ORDERED =-22;//精确预约此座位已有预约

     String resultspe="";
    int orderstatus;
    ProgressBar progressBar;
    ActionBar actionBar;
    int orderMode=-1;
    String room,sitNum,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.order_seat_result_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("预约座位");
        Intent intent=getIntent();
        orderMode=intent.getIntExtra(SelectSeatActivity.ORDERMODE,-1);
        room=intent.getStringExtra(SelectSeatActivity.ORDER_ROOM);
        sitNum=intent.getStringExtra(SelectSeatActivity.ORDER_SITNUM);
        date=intent.getStringExtra(SelectSeatActivity.ORDER_DATE);
        progressBar= (ProgressBar) findViewById(R.id.orderprogressbar);
        order_infotv= (TextView) findViewById(R.id.order_infotv);
        order_seat_result_tv= (TextView) findViewById(R.id.order_seat_result_tv);
        button= (Button) findViewById(R.id.btnafter_order_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderstatus==ORDERSTATUS_ONEKEY_FAIL
                        || orderstatus==ORDERSTATUS_TIMEOUT
                        ||orderstatus==ORDERSTATUS_SPECIFIC_FAIl) {
                    if (orderMode==0)
                    OneKey_Order();
                    else if (orderMode==1){
                        specific_Order(OrderSeatService.coreClient, room, sitNum, date);
                    }
                    button.setEnabled(false);
                    order_seat_result_tv.setText("正在预约...");
                    order_seat_result_tv.setTextColor(getResources().getColor(R.color.green));
                } else if (orderstatus==ORDERSTATUS_SPECIFIC_SEAT_ORDERED){
                    //重新选择座位
                    startActivity(new Intent( Order_Seat_Process.this, SelectSeatActivity.class));
                }else {
                    onBackPressed();
                }
            }
        });
        if (orderMode==0) {
            OneKey_Order();
        }else if (orderMode==1){
            specific_Order(OrderSeatService.coreClient, room, sitNum, date);
        }
        button.setEnabled(false);
    }

    private void specific_Order(final HttpClient client, final String room, final String sitNo, final String date) {
        progressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                    // 获取当前日期+1
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    Date beginDate = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(beginDate);
                    calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
                    String date1 = format.format(calendar.getTime());
                    //Log.i("bacground","精确预约11111111111111");
                    UserinfoUtils userinfoUtils = new UserinfoUtils(Order_Seat_Process.this);
                try {
                    OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(),userinfoUtils.get_LastPassword());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpTools.GetHTTPRequest(
                            "http://yuyue.juneberry.cn/BookSeat/BookSeatListForm.aspx",
                            OrderSeatService.coreClient);
                    List<String> list = null; // 获取该房间的列表
                    try {
                        list = OrderSeatService.getYuYueInfo("000" + room, date1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (0 == list.size()) {
                        // 没有可用座位
                        resultspe = "empty";
                        message.what = ORDERSTATUS_SPECIFIC_FAIl;
                    } else {
                        try {
                            resultspe = OrderSeatService.subYuYueInfo(OrderSeatService.coreClient,room, sitNo, date);
                        } catch (Exception e) {
                            //链接错误，包括超时
                            Log.i("bacground", "精确预约超时");
                            message.what = ORDERSTATUS_TIMEOUT;
                            e.printStackTrace();
                        }
                        // System.out.println(res);
                        if (resultspe.contains("已经存在有效的预约记录")) {
                            resultspe = "fail";
                            message.what = ORDERSTATUS_HASORDERED;

                        }else {
                            message.what = ORDERSTATUS_SPECIFIC_SUCCESS;
                        }
                        //Log.i("bacground","精确预444444444444444");
                        Log.i("bacground", "精确预约返回结果：" + resultspe);
                        message.obj = resultspe;
                     handler.sendMessage(message);
                    }

                }

        }.start();
    }

    private void OneKey_Order() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                Message message = new Message();
                try {
                    UserinfoUtils userinfoUtils = new UserinfoUtils(Order_Seat_Process.this);
                    boolean b = OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(), userinfoUtils.get_LastPassword());
                    System.out.println(b);
                    OrderSeatService o = new OrderSeatService();
                    String result = OrderSeatService.OneKeySit(OrderSeatService.coreClient);
                    System.out.println(result + "=====");
                    message.what = ORDERSTATUS_CONECT_SUCCESS;
                    message.obj = result;
                } catch (Exception e) {
                    //链接错误，包括超时
                    message.what = ORDERSTATUS_TIMEOUT;
                    orderstatus = ORDERSTATUS_TIMEOUT;
                    e.printStackTrace();

                }
                handler.sendMessage(message);

            }
        }.start();
    }


    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ORDERSTATUS_TIMEOUT://链接错误
                    progressBar.setVisibility(View.INVISIBLE);
                    order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                    order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                    order_infotv.setText(getResources().getText(R.string.order_connection_out_tip));
                    order_infotv.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    orderstatus = ORDERSTATUS_TIMEOUT;
                    button.setText("再试一次");
                    button.setEnabled(true);
                    break;
                case ORDERSTATUS_CONECT_SUCCESS://一键预约连接成功
                    if (msg.obj.toString().contains("empty")){
                        progressBar.setVisibility(View.INVISIBLE);
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                        order_infotv.setText(getResources().getText(R.string.order_empty_tip));
                        orderstatus= ORDERSTATUS_ONEKEY_FAIL;
                        button.setText("再试一次");
                        button.setEnabled(true);

                    }else if (msg.obj.toString().contains("fail")){
                        progressBar.setVisibility(View.INVISIBLE);
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                        order_infotv.setText(getResources().getText(R.string.order_hashistory_tip));
                        orderstatus = ORDERSTATUS_HASORDERED;
                        button.setText("返回");
                        button.setEnabled(true);
                    }
                    else {//一键预约成功
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.green));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_success));
                        progressBar.setVisibility(View.INVISIBLE);
                        order_infotv.setText(msg.obj.toString());
                        orderstatus =ORDERSTATUS_ONE_KEY_SUCCESS;
                        button.setText("返回");
                        button.setEnabled(true);

                    }
                    break;
                // TODO: 2015/11/18
                case ORDERSTATUS_SPECIFIC_FAIl://精确预约失败
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.i("bacground", "精确预约失败");
                    orderstatus=ORDERSTATUS_SPECIFIC_FAIl;
                    button.setText("再试一次");
                    button.setEnabled(true);
                    break;
                case ORDERSTATUS_SPECIFIC_SUCCESS://精确预约成功
                    progressBar.setVisibility(View.INVISIBLE);
                    order_seat_result_tv.setText(getResources().getText(R.string.order_success));

                    order_infotv.setText(room + "楼" + sitNum + "号" + "时间：" + date);
                    orderstatus=ORDERSTATUS_SPECIFIC_SUCCESS;
                    button.setText("返回");
                    button.setEnabled(true);
                    Log.i("bacground", "精确预约成功,预约信息：");
                    break;
                case ORDERSTATUS_SPECIFIC_SEAT_ORDERED://精确预约此座位已有人预约
                    progressBar.setVisibility(View.INVISIBLE);
                    orderstatus=ORDERSTATUS_SPECIFIC_SEAT_ORDERED;
                    button.setText("重新选择座位");
                    button.setEnabled(true);
                    Log.i("bacground","精确预约此座位已有人预约");
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
