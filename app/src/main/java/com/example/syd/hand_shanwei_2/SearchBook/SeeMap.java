package com.example.syd.hand_shanwei_2.SearchBook;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/22.
 */
public class SeeMap extends AppCompatActivity {
    private ImageView map;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.seemaplayout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("地图");
        map= (ImageView) findViewById(R.id.imgshowmap);
        int floor=getIntent().getIntExtra("floor",0);
        showMap(floor);
    }

    private void showMap(int floor) {
        switch (floor){
            case 3:
                map.setImageResource(R.drawable._3floor);
                break;
            case 4:
                map.setImageResource(R.drawable._4floor);
                break;
            case 5:
                map.setImageResource(R.drawable._5floor);
                break;
            case 6:
                map.setImageResource(R.drawable._6floor);
                break;
            case 7:
                map.setImageResource(R.drawable._7floor);
                break;
            case 8:
                map.setImageResource(R.drawable._8floor);
                break;
            case 9:
                map.setImageResource(R.drawable._9floor);
                break;
            case 10:
                map.setImageResource(R.drawable._10floor);
                break;
            case 11:
                map.setImageResource(R.drawable._11floor);
                break;
            case 12:
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
