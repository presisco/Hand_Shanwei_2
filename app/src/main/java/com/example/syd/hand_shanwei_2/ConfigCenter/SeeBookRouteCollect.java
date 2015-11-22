package com.example.syd.hand_shanwei_2.ConfigCenter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.syd.hand_shanwei_2.Adapters.MyBookRootListViewAdapter;
import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/20.
 */
public class SeeBookRouteCollect extends AppCompatActivity {
    private ActionBar actionBar;
    private MyBookRootListViewAdapter myBookRootListViewAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.see_collected_book_route);
        actionBar.setTitle("借书收藏路线");
        actionBar.setDisplayHomeAsUpEnabled(true);
        listView= (ListView) findViewById(R.id.seebookroutelistview);
        myBookRootListViewAdapter=new MyBookRootListViewAdapter(this);
        listView.setAdapter(myBookRootListViewAdapter);
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
