package com.example.syd.hand_shanwei_2.ConfigCenter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/20.
 */
public class SeeAboutOur extends AppCompatActivity {
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.about_our_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("关于我们");
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
