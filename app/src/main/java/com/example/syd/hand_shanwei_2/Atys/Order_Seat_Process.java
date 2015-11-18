package com.example.syd.hand_shanwei_2.Atys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.R;
/**
 * Created by syd on 2015/11/16.
 */
public class Order_Seat_Process extends AppCompatActivity {

    TextView order_seat_result_tv,order_infotv;
    Button button;
    boolean success=false;
    ProgressBar progressBar;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.order_seat_result_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("预约座位");
        //Intent intent=getIntent();//包含传过来的预约结果和预约提示信息
        progressBar= (ProgressBar) findViewById(R.id.orderprogressbar);
        order_infotv= (TextView) findViewById(R.id.order_infotv);
        order_seat_result_tv= (TextView) findViewById(R.id.order_seat_result_tv);
        button= (Button) findViewById(R.id.btnafter_order_result);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!success){
                    start_Order();
                    button.setEnabled(false);
                    order_seat_result_tv.setText("正在预约...");
                    order_seat_result_tv.setTextColor(getResources().getColor(R.color.green));
                }else {
                    onBackPressed();
                }
            }
        });
        start_Order();
        button.setEnabled(false);
    }

    private void start_Order() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                Message message=new Message();
                try {
                    UserinfoUtils userinfoUtils=new UserinfoUtils(Order_Seat_Process.this);
                    boolean b=OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(),userinfoUtils.get_LastPassword());
                    System.out.println(b);
                    OrderSeatService o=new OrderSeatService();
                    String result=OrderSeatService.OneKeySit(OrderSeatService.coreClient);
                    System.out.println(result+"=====");
                    message.what=1;
                    message.obj=result;
                } catch (Exception e) {
                    //链接错误，包括超时
                    message.what=-1;
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
                case -1://链接错误
                    progressBar.setVisibility(View.INVISIBLE);
                    order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                    order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                    order_infotv.setText(getResources().getText(R.string.order_connection_out_tip));
                    order_infotv.setVisibility(View.VISIBLE);
                    button.setVisibility(View.VISIBLE);
                    success=false;
                    button.setText("再试一次");
                    button.setEnabled(true);
                    break;
                case 1:
                    if (msg.obj.toString().contains("empty")){
                        progressBar.setVisibility(View.INVISIBLE);
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                        order_infotv.setText(getResources().getText(R.string.order_empty_tip));
                        button.setText("再试一次");
                        button.setEnabled(true);

                    }else if (msg.obj.toString().contains("fail")){
                        progressBar.setVisibility(View.INVISIBLE);
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.red));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_fail));
                        order_infotv.setText(getResources().getText(R.string.order_hashistory_tip));
                        success=true;
                        button.setText("返回");
                        button.setEnabled(true);
                    }
                    else {
                        order_infotv.setVisibility(View.VISIBLE);
                        order_seat_result_tv.setTextColor(getResources().getColor(R.color.green));
                        order_seat_result_tv.setText(getResources().getText(R.string.order_success));
                        order_infotv.setText(msg.obj.toString());
                        success=true;
                        button.setText("返回");
                        button.setEnabled(true);

                    }
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
