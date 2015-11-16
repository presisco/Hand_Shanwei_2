package com.example.syd.hand_shanwei_2.Atys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.R;

import Http_Utils.Login_Util;
import Local_Utils.UserinfoUtils;

/**
 * Created by syd on 2015/11/15.
 */
public class Aty_LogIn extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    EditText etid,etps;
    TextView errortv;
    String id,ps;
    //获取从哪里启动,0代表个人中心点击登陆，-1代表预约未登录跳转，默认为0
    int from=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.aty_login);
        actionBar.setTitle("用户登录");
        from=getIntent().getIntExtra("from",0);
        etid= (EditText) findViewById(R.id.etid);
        etps= (EditText) findViewById(R.id.etps);
        errortv= (TextView) findViewById(R.id.errortv);
        etid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etid.getText())&&!TextUtils.isEmpty(etps.getText())){
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
                if (!TextUtils.isEmpty(etid.getText())&&!TextUtils.isEmpty(etps.getText())){
                    errortv.setText("");
                }
            }
        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = etid.getText().toString();
                ps = etid.getText().toString();
                if (TextUtils.isEmpty(etid.getText()) || TextUtils.isEmpty(etps.getText())) {
                    errortv.setText("请输入账号及密码");
                } else {
                    Login_Util login_util = new Login_Util();
                    boolean status = login_util.login(id, ps);
                    if (status) {
                        Toast.makeText(Aty_LogIn.this, "登陆失败,请重试", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Aty_LogIn.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        //更新登录信息
                        UserinfoUtils userinfoUtils = new UserinfoUtils(Aty_LogIn.this);
                        userinfoUtils.refresh_Login_Status(false);
                        userinfoUtils.save_CureentLogin_Info(id, ps);
                        if (from == -1) {
                            startActivity(new Intent(Aty_LogIn.this, Oder_Tomorrow_Seat_Home.class));
                            finish();
                        } else if (from == 0) {
                            HomeActivity homeActivity = new HomeActivity();
                            homeActivity.viewPager.setCurrentItem(2);
                            startActivity(new Intent(Aty_LogIn.this, HomeActivity.class));
                            finish();
                        }


                    }
                }
            }
        });


    }
}
