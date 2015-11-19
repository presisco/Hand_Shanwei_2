package com.example.syd.hand_shanwei_2.BookSeats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.Atys.Order_Seat_Process;
import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.FloorName2ID;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.Model.FloorInfo;
import com.example.syd.hand_shanwei_2.Model.SeatInfo;
import com.example.syd.hand_shanwei_2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by syd on 2015/11/13.
 */
public class SelectSeatActivity extends Activity{
    /*//楼层数量
    public static final int FLOORS=12;*/
    //座位显示列数
    private static final Integer COLUMN_COUNT=6;
    //预约日期格式
    private static final String DATE_FORMAT="yyyy/MM/dd";
    public static final String ORDERMODE ="ordermode" ;
    public static final String ORDER_SITNUM ="order_sitNum";
    public static final String ORDER_ROOM ="order_room";
    public static final String ORDER_DATE ="order_date";
    //存储日期
    String mBookDate;
    //Spinner下拉选项
    Spinner spinner;
    //Spinner适配器
    ArrayAdapter<String> arrayAdapter;
    TextView mBookSeatDate;
    TextView nofloorselectedtv;
    int selectfloor_pos;
    String mSelectedFloorName;

    private RecyclerView mSeatRecyclerView;
    private SeatInfoAdapter mSeatInfoAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    SeatInfo[] mDataSet;

    private final String floor_item[] = {"不限", "三楼", "四楼",
                                "五楼", "六楼", "七楼",
                                "八楼", "九楼", "十楼",
                                "十一楼", "十二楼",
                                "图东环楼三楼", "图东环楼四楼"};
    private boolean randomOrder=false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_seat);

        getBookDate();
        intent=new Intent(SelectSeatActivity.this, Order_Seat_Process.class);
        mBookSeatDate = (TextView) findViewById(R.id.bookDateTextView);
        mBookSeatDate.setText(getResources().getString(R.string.order_time)+mBookDate);
        spinner = (Spinner) findViewById(R.id.floorSpinner);
        //楼层选项内容数组

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, floor_item);
        spinner.setAdapter(arrayAdapter);
        nofloorselectedtv = (TextView) findViewById(R.id.noselectFloortv);

        mSeatRecyclerView = (RecyclerView) findViewById(R.id.seatInfoRecyclerView);

        // GridLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mRecyclerViewLayoutManager = new GridLayoutManager(this,COLUMN_COUNT);
        mSeatRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);

        mSeatInfoAdapter = new SeatInfoAdapter(new SeatInfo[0],this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mSeatRecyclerView.setAdapter(mSeatInfoAdapter);

        findViewById(R.id.btn_order_seats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击预约按钮的处理
              /*  Log.d("SelectSeatActivity","Floor_ID:"+FloorName2ID.getID(mSelectedFloorName)
                        +"|Seat_ID:"+mSeatInfoAdapter.getSelectedSeat().id);
                Toast.makeText(SelectSeatActivity.this,"Floor_ID:"+FloorName2ID.getID(mSelectedFloorName)
                        +"|Seat_ID:"+mSeatInfoAdapter.getSelectedSeat().id,Toast.LENGTH_LONG).show();*/
                if (randomOrder){
                    // TODO: 2015/11/18 一键预约
                    Log.i("bacground", "on key order");
                    //0代表一键预约
                    intent.putExtra(SelectSeatActivity.ORDERMODE,0);
                    startActivity(intent);

                }else {
                    if (SeatInfoAdapter.selected==-1){
                        Toast.makeText(SelectSeatActivity.this,"请选择座位！",Toast.LENGTH_SHORT).show();
                    }else {
                        // TODO: 2015/11/18 精确预约 1
                        intent.putExtra(SelectSeatActivity.ORDERMODE,1);
                        intent.putExtra(SelectSeatActivity.ORDER_ROOM,FloorName2ID.getID(mSelectedFloorName));
                        intent.putExtra(SelectSeatActivity.ORDER_SITNUM,mSeatInfoAdapter.getSelectedSeat().id);
                        intent.putExtra(SelectSeatActivity.ORDER_DATE,mBookDate);
                        startActivity(intent);
                    Log.i("bacground","specific order"+FloorName2ID.getID(mSelectedFloorName)+"=="
                            +mSeatInfoAdapter.getSelectedSeat().id+"--"
                            +mBookDate);
                    }


                }

            }
        });

        //选择楼层事件
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SeatInfoAdapter.selected=-1;
                //选择不限
                selectfloor_pos = position;
                mSelectedFloorName = floor_item[position];
                if (position == 0) {
                    randomOrder=true;
                    mSeatRecyclerView.setVisibility(View.GONE);
                    nofloorselectedtv.setVisibility(View.VISIBLE);
                } else {
                    randomOrder=false;
                    mSeatRecyclerView.setVisibility(View.VISIBLE);
                    nofloorselectedtv.setVisibility(View.GONE);
//                    Toast.makeText(SelectSeatActivity.this,getResources().getString(R.string.loading),Toast.LENGTH_SHORT).show();
                    new DummyBackgroundTask().execute();
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
        if (getIntent().getBooleanExtra("random",true)) {
            Log.d("SelectSeatActivity","LaunchRandom");
            //spinner选择不限
        } else {
            Log.d("SelectSeatActivity", "LaunchSelected");
            //spinner选择mSelectedFloorName
            mSelectedFloorName=getIntent().getStringExtra("floorname");
            spinner.setSelection(arrayAdapter.getPosition(mSelectedFloorName),true);
            Log.d("SelectSeatActivity","PreSelected:pos="+arrayAdapter.getPosition(mSelectedFloorName)
                    +" name="+mSelectedFloorName);
        }
    }

    private void onRefreshComplete()
    {
        mSeatInfoAdapter.updateDataSet(mDataSet);
        mSeatInfoAdapter.notifyDataSetChanged();
//        Toast.makeText(SelectSeatActivity.this,getResources().getString(R.string.load_complete),Toast.LENGTH_SHORT).show();
    }

    private class DummyBackgroundTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date beginDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(beginDate);
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
            String date = format.format(calendar.getTime());
            try {
                //OrderSeatService.testUserInfoIsTrue("201100800169", "011796");
                //Date date=new Date(2015,11,18);
               /* OrderSeatService.getYuYueInfo(HttpConnectionService.getHttpClient()
                        , "4", "2015/11/17");*/
                // 发送get请求
              /*  HttpTools.GetHTTPRequest(
                        "http://yuyue.juneberry.cn/BookSeat/BookSeatListForm.aspx",
                        HttpConnectionService.getHttpClient());
                OrderSeatService.subYuYueInfo(HttpConnectionService.getHttpClient()
                        ,"000223","001",date);*/
                UserinfoUtils userinfoUtils=new UserinfoUtils(SelectSeatActivity.this);
                OrderSeatService.testUserInfoIsTrue(userinfoUtils.get_LastId(), userinfoUtils.get_LastPassword());
                //System.out.println(userinfoUtils.get_LastId()+"==="+userinfoUtils.get_LastPassword());
                List<String> seatRaw=OrderSeatService.getYuYueInfo(FloorName2ID.getID(mSelectedFloorName),mBookDate);
                mDataSet=new SeatInfo[seatRaw.size()];
                for (int j=0;j<seatRaw.size();j++){
                    mDataSet[j]=new SeatInfo();
                    mDataSet[j].id=seatRaw.get(j);
                }
                Log.d("SelectSeatActivity","SeatInfoFetched!Total Count:"+seatRaw.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Return a new random list of cheeses
            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            // Tell the Fragment that the refresh has completed
            onRefreshComplete();
        }

    }

    //获取当前时间函数
    private void getBookDate() {
        SimpleDateFormat converter=new SimpleDateFormat(DATE_FORMAT);
/*
        long time=System.currentTimeMillis()+24*60*60*1000;//long now = android.os.SystemClock.uptimeMillis();
        Date d1=new Date(time);
        mBookDate=converter.format(d1);*/

        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        mBookDate=converter.format(calendar.getTime());
        Log.d("getBookDate()",mBookDate);
    }
}

