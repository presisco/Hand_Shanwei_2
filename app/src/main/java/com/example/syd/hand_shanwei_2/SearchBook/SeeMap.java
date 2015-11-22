package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/22.
 */
public class SeeMap extends AppCompatActivity {
    private ImageView map;
    private ActionBar actionBar;
    private TextView tvbookmapinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.seemaplayout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("地图");
        map= (ImageView) findViewById(R.id.imgshowmap);
        tvbookmapinfo= (TextView) findViewById(R.id.tvmapbookinfo);
        Intent intent=getIntent();
        String floor=intent.getStringExtra("floor");
        String bookname=intent.getStringExtra("bookname");
        String boomcode=intent.getStringExtra("bookcode");
        tvbookmapinfo.setText(bookname+"\n"+boomcode);
        showMap(floor);
        Log.i("bac",floor);
    }
    private void showMap(String floor) {
        switch (floor){
            case "3楼":
                map.setImageResource(R.drawable._3floor);
                break;
            case "4楼":
                map.setImageResource(R.drawable._4floor);
                break;
            case "5楼":
                map.setImageResource(R.drawable._5floor);
                break;
            case "6楼":
                map.setImageResource(R.drawable._6floor);
                break;
            case "7楼":
                map.setImageResource(R.drawable._7floor);
                break;
            case "8楼":
                map.setImageResource(R.drawable._8floor);
                break;
            case "9楼":
                map.setImageResource(R.drawable._9floor);
                break;
            case "10楼":
                map.setImageResource(R.drawable._10floor);
                break;
            case "11楼":
                map.setImageResource(R.drawable._11floor);
                break;
            case "12楼":
                map.setImageResource(R.drawable._12floor);
                break;
            default:
                map.setBackgroundColor(getResources().getColor(R.color.red));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
