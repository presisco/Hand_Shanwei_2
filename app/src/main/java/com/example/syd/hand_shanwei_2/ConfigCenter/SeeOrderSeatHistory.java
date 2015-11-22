package com.example.syd.hand_shanwei_2.ConfigCenter;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.syd.hand_shanwei_2.Adapters.MyOrderHistoryListViewAdapter;
import com.example.syd.hand_shanwei_2.R;

/**
 * Created by syd on 2015/11/20.
 */
public class SeeOrderSeatHistory extends AppCompatActivity {
    private Button cancelOrder;
    private ActionBar actionBar;
    private ListView listView;
//    String cancelresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar=getSupportActionBar();
        setContentView(R.layout.orderhistorylayout);
        actionBar.setTitle("预约记录");
        actionBar.setDisplayHomeAsUpEnabled(true);
        View v= LayoutInflater.from(this).inflate(R.layout.orderhistorylistviewitemview,null);
        /*cancelOrder= (Button) v.findViewById(R.id.cancel_order);
        //取消预约
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("bac","cancel");
              cancelOrder();

            }
        });
*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listView= (ListView) findViewById(R.id.orderhistorylistview);
        MyOrderHistoryListViewAdapter m=new MyOrderHistoryListViewAdapter(this);
        listView.setAdapter(m);
    }



}
