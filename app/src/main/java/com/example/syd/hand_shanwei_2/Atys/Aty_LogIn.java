package com.example.syd.hand_shanwei_2.Atys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.OrderSeats.SelectSeatActivity;
import com.example.syd.hand_shanwei_2.R;

import com.example.syd.hand_shanwei_2.Http_Utils.Login_Util;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;

/**
 * Created by syd on 2015/11/15.
 */
public class Aty_LogIn extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    EditText etid,etps;
    TextView errortv;
    String id,ps;
    Handler handler;
    //获取从哪里启动,0代表个人中心点击登陆，-1代表预约未登录跳转，默认为0
    int from=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        setContentView(R.layout.aty_login);
        actionBar.setTitle("用户登录");
        from = getIntent().getIntExtra("from", 0);
        etid = (EditText) findViewById(R.id.etid);
        etps = (EditText) findViewById(R.id.etps);
        errortv = (TextView) findViewById(R.id.errortv);
        etid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etid.getText()) && !TextUtils.isEmpty(etps.getText())) {
                    errortv.setText("");
                }

            }
        });
        etps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etid.getText()) && !TextUtils.isEmpty(etps.getText())) {
                    errortv.setText("");
                }
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = etid.getText().toString();
                ps = etps.getText().toString();
                if (TextUtils.isEmpty(etid.getText()) || TextUtils.isEmpty(etps.getText())) {
                    errortv.setText("请输入账号及密码");
                } else {
                   login(id, ps, from);
                    Log.i("bac","id:"+id+"ps:"+ps);
                }
            }
        });
    }
    public void login(final String id, final String password, final int from){
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.obj.toString().contains("fail")) {
                    Toast.makeText(Aty_LogIn.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                } else if(msg.obj.toString().contains("success")) {
                    Toast.makeText(Aty_LogIn.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //更新登录信息
                    UserinfoUtils userinfoUtils = new UserinfoUtils(Aty_LogIn.this);
                    userinfoUtils.refresh_Login_Status(true);
                    userinfoUtils.save_CureentLogin_Info(id,password);
                    if (from == -1) {
                       startActivity(new Intent(Aty_LogIn.this, SelectSeatActivity.class));
                        finish();
                    } else if (from == 0) {
                        Intent intent=new Intent(new Intent(Aty_LogIn.this,HomeActivity.class));
                        intent.putExtra("pos",2);
//                            HomeActivity homeActivity = new HomeActivity();
//                            homeActivity.viewPager.setCurrentItem(2);
                      startActivity(intent);
                        finish();
                    }

//                    context.startActivity(new Intent(context, HomeActivity.class));
                }else {
                    Toast.makeText(Aty_LogIn.this, "网络连接失败", Toast.LENGTH_SHORT).show();

                }
            }

        };
        //验证登陆信息是否符合,验证成功则发送handler消息
        new Thread(){

            @Override
            public void run() {
                Message message=new Message();
                message.what=123;
                //调用服务器验证信息接口
                try {
                    Log.i("bac","id"+id+"pa:"+ps);
                    boolean b= OrderSeatService.testUserInfoIsTrue(id, password);
                    Log.i("bac",b+"");
                    if (b){
                        message.obj="success";

                    }else {
                        message.obj="fail";
                    }


                } catch (Exception e) {
                    message.obj="error";
                    e.printStackTrace();

                }
                handler.sendMessage(message);
            }
        }.start();

    }
    }
