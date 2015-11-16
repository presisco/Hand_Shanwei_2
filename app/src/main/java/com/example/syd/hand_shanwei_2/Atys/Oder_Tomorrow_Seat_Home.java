package com.example.syd.hand_shanwei_2.Atys;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.syd.hand_shanwei_2.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by syd on 2015/11/13.
 */
public class Oder_Tomorrow_Seat_Home extends Activity{
    /*//楼层数量
    public static final int FLOORS=12;*/
    //存储年月日
    int year,month,day;
    //Spinner下拉选项
    Spinner spinner;
    //Spinner适配器
    ArrayAdapter<String> arrayAdapter;
    TextView tvorderTime;
    //显示选择的楼层内的教室
    GridView gridView;
    View oldview;
    boolean firstclick;
    TextView nofloorselectedtv;
    int selectfloor_pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_tomorrow_seat_home);
        firstclick=true;
        //获取当前时间
        get_CurrentTime();
        //初始化数据
        //显示预约明日
        tvorderTime= (TextView) findViewById(R.id.tvordertime);
        tvorderTime.setText(year+"-"+month+"-"+day+1);
        spinner= (Spinner) findViewById(R.id.spinner);
        //楼层选项内容数组
        final String floor_item[]={"不限","三楼","四楼","五楼","六楼","七楼","八楼","九楼","十楼","十一楼","十二楼","图东环楼三楼","图东环楼四楼"};
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,floor_item);
        spinner.setAdapter(arrayAdapter);
        nofloorselectedtv= (TextView) findViewById(R.id.noselectFloortv);
        gridView= (GridView) findViewById(R.id.gridview_seats);
        //初始化gridview
        init_GridView();
        findViewById(R.id.btn_order_seats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择不限进行一键预约
                if (selectfloor_pos==0){

                }
            }
        });
        //选择楼层事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //选择不限
                selectfloor_pos=position;
                if (position==0){
                    gridView.setVisibility(View.GONE);
                    nofloorselectedtv.setVisibility(View.VISIBLE);
                }else {
                    gridView.setVisibility(View.VISIBLE);
                    nofloorselectedtv.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //后退按键
        findViewById(R.id.btnback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_GridView() {
        ArrayList<HashMap<String,Object>> items=new ArrayList<>();
        for (int i=0;i<40;i++){
          /*  String s1 = null,s2 = null;*/
            HashMap<String,Object> hashMap=new HashMap<>();
            hashMap.put("seat_number",String.format("%03d",i+1));
            items.add(hashMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,items,R.layout.seats_grid,new String[]{"seat_number"},new int[]{R.id.tv_classroom});
        //添加并显示
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (firstclick){
                    view.findViewById(R.id.tv_classroom).setBackgroundColor(getResources().getColor(R.color.my));
                    firstclick=false;
                 }else {
                    view.findViewById(R.id.tv_classroom).setBackgroundColor(getResources().getColor(R.color.my));
                    oldview.findViewById(R.id.tv_classroom).setBackground(getResources().getDrawable(R.drawable.classroom));
                }
                oldview=view;
            }
        });
    }

    //获取当前时间函数
    private void get_CurrentTime() {
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH)+1;
        day=calendar.get(Calendar.DAY_OF_MONTH);
    }
}
