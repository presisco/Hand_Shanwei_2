package com.example.syd.hand_shanwei_2.Atys;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.R;

import org.apache.http.client.HttpClient;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Adapters.ViewAdapter;
import Http_Utils.Get_Today_Seats;
import Local_Utils.UserinfoUtils;
import Service.MD5Tools;
import Service.YuyueService;
import Service.YuyueTools;

/**
 * Created by Admin on 2015/11/12.
 */
public class HomeActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener, ActionBar.TabListener, View.OnClickListener {
    /*//登陆信息进行本地存储
    SharedPreferences sharedPreferences;*/
    //本地信息管理工具
    public static HttpClient client;
    private static MD5Tools md5Tools=new MD5Tools();
    private static YuyueTools yuyueTools=new YuyueTools();
    private YuyueService yuYueService=new YuyueService();
    UserinfoUtils userinfoUtils;
    ActionBar actionBar;
    //存储屏幕高度和宽度
    int height,width;
    //存储三个tab
    List<View> views;
    public ViewPager viewPager;
//    ViewAdapter viewAdapter;
    TextView[] textViews;
    //int[] ids={R.id.title01,R.id.title02,R.id.title03};
    GridView gridView;
    //分别储存每个楼层总共座位数和剩余座位数
    int allseats[],current_seats[];
    //楼层数
    private static final int FLOORS=12;
    //private String tab_title[]={"预约查座","找书","个人中心"};
    //个人中心界面登陆和退出登录按钮
    Button btnlog,btnorder_tomorrow;
    int tab_layout_id[]={R.layout.tab_custom_view01,R.layout.tab_custom_view02,R.layout.tab_custom_view03};
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.home);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setIcon();
        actionBar.show();
        actionBar.setLogo(getResources().getDrawable(R.drawable.welcome));
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_view);
        actionBar.setDisplayShowCustomEnabled(true);
       // actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.drawable.welcome);
        //初始化座位数组
        allseats=new int[FLOORS];
        current_seats=new int[FLOORS];
        //初始化viewPager
        initViews();
        //获取屏幕宽高，计算各自的宽度
        //初始化tab_title
        btnlog= (Button) views.get(2).findViewById(R.id.btn_log_in_out);
        btnlog.setOnClickListener(this);
        for (int i = 0; i <3 ; i++) {
            actionBar.addTab(actionBar.newTab().setCustomView(tab_layout_id[i]).setTabListener(this));
        }
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = (dm.widthPixels-30)/3;//宽度height = dm.heightPixels ;//高度
        height=(dm.heightPixels-60)/4;
        gridView= (GridView) views.get(0).findViewById(R.id.gridview);
        gridView.setColumnWidth(width);
        //初始化楼层数据方格信息
        initGrid();
        userinfoUtils=new UserinfoUtils(this);
        //预约按钮事件
        btnorder_tomorrow= (Button) views.get(0).findViewById(R.id.order_tomorrow);
        btnorder_tomorrow.setOnClickListener(this);
    }
    //初始化tab_title
    /*private void initDots() {
        textViews=new TextView[views.size()];
        for (int i = 0; i <views.size() ; i++) {
            textViews[i]= (TextView) findViewById(ids[i]);
            textViews[i].setVisibility(View.VISIBLE);
            textViews[i].setOnClickListener(new MyOnClickListener(i));
        }
    }*/
    //初始化viewPager
    private void initViews() {
        LayoutInflater layoutInflater= LayoutInflater.from(this);
        views=new ArrayList<View>();
        views.add(layoutInflater.inflate(R.layout.tab01layout, null));
        views.add(layoutInflater.inflate(R.layout.tab02layout, null));
        views.add(layoutInflater.inflate(R.layout.tab03layout, null));
        //System.out.println("11111111111111111111111111111"+views.size());
        ViewAdapter viewAdapter=new ViewAdapter(views);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setOnPageChangeListener(this);

    }
    //初始化楼层数据方格信息
    private void initGrid() {
        //获取今日座位信息
        Toast.makeText(this,"正在更新信息...",Toast.LENGTH_SHORT).show();
        textViews = new TextView[views.size()];
        Get_Today_Seats get_today_seats=new Get_Today_Seats(HomeActivity.this,FLOORS);
        allseats=get_today_seats.get_Allseats();
        current_seats=get_today_seats.get_Remainseats();
        ArrayList<HashMap<String,Object>> items=new ArrayList<>();
        for (int i=0;i<12;i++){
            String s1 = null,s2 = null;
            HashMap<String,Object> hashMap=new HashMap<>();
            //根据每层楼剩余座位数量，改变字体颜色
            if (current_seats[i]<=0){
            }
            switch (i){
                case 0:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="三楼";
                    break;
                case 1:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="四楼";
                    break;
                case 2:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="五楼";
                    break;
                case 3:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="六楼";
                    break;
                case 4:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="七楼";
                    break;
                case 5:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="八楼";
                    break;
                case 6:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="九楼";
                    break;
                case 7:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="十楼";
                    break;
                case 8:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="十一楼";
                    break;
                case 9:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="十二楼";
                    break;
                case 10:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2="图东环楼三楼";
                    break;
                case 11:
                    s1="剩余:               "+allseats[i]+"\n\n总座位：        "+current_seats[i];
                    s2= "图东环楼四楼";
                    break;
            }
            hashMap.put("itemtv1",s1);
            hashMap.put("itemtv2",s2);
            items.add(hashMap);
        }
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,items,R.layout.night_item,new String[]{"itemtv1","itemtv2"},new int[]{R.id.itemtv1,R.id.itemtv2});
        //添加并显示
        gridView.setAdapter(simpleAdapter);
    }
    //判断是否登陆，每次登陆成功之后更新本地登录信息，标记为已经登陆，
    // 每次进行网络连接要读取本地账号和密码，上传进行验证，
    // 如果验证失败，把本地信息清楚，并且标记为未登录

    //三个tab其中一个被选中
    @Override
    public void onPageSelected(int position) {
        actionBar.setSelectedNavigationItem(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. The previous tab's unselect and this tab's select will be
     *            executed in a single transaction. This FragmentTransaction does not support
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. This tab's unselect and the newly selected tab's select
     *            will be executed in a single transaction. This FragmentTransaction does not
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user.
     * Some applications may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            once this method returns. This FragmentTransaction does not support
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     * 按钮点击事件处理
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login://登陆界面的登陆按钮

                break;
            case R.id.order_tomorrow://首页预约明日按钮
                //判断是否登陆,若没登陆，则跳转到登陆界面
                if (userinfoUtils.get_Login_Status()){//之前没有登陆
                    //跳转到登陆界面
                    Toast.makeText(HomeActivity.this,"请登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(HomeActivity.this, Aty_LogIn.class);
                    //标记是预约未登录启动登陆界面
                    intent.putExtra("from",-1);
                    startActivity(intent);
                }else {//之前登陆，直接进入选座界面
                    startActivity(new Intent(HomeActivity.this, Oder_Tomorrow_Seat_Home.class));
                }
                break;
            case R.id.btn_see_collect_book_route://个人中心查看借书路线
                //跳转到查看结束路线
                Intent intent=new Intent(HomeActivity.this,Aty_Book_Route.class);
                startActivity(intent);
                break;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        int position;
        public MyOnClickListener(int i) {
            this.position=i;
        }
        //实现点击tab_title进入相应tab
        @Override
        public void onClick(View v) {
            switch (position){
                case 0:
                        viewPager.setCurrentItem(0);
                    break;
                case 1:
                        viewPager.setCurrentItem(1);
                    break;
                case 2:
                        viewPager.setCurrentItem(2);
                    break;
            }
        }
    }
    //实现按两次退出程序
    @Override
    public void onBackPressed() {
        long click_time=new Date().getTime();
        if (click_time-pre_click_time>2000){
            Toast.makeText(HomeActivity.this,"再按一次退出程序！",Toast.LENGTH_SHORT).show();
            //更新时间
            pre_click_time=click_time;
            return;
        }
        super.onBackPressed();
    }
    long pre_click_time;
}
