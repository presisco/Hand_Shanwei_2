package com.example.syd.hand_shanwei_2.BookSeats;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.Local_Utils.UserinfoUtils;
import com.example.syd.hand_shanwei_2.R;
import com.example.syd.hand_shanwei_2.Model.FloorInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Presisco on 2015/9/28.
 */
public class BookSeatsContentPage extends Fragment{
    private static boolean isfirstin=true;
    private static final String LOG_TAG = BookSeatsContentPage.class.getSimpleName();
    private static final Integer COLUMN_COUNT=3;
    private static  List<FloorInfo> floorInfos;
    /**
     * The {@link android.support.v4.widget.SwipeRefreshLayout} that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * The {@link android.support.v7.widget.RecyclerView} that displays the content that should be refreshed.
     */
    private RecyclerView mFloorInfoRecyclerView;
    private FloorInfoAdapter mFloorInfoAdapter;
    private RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    private FloorInfo[] mDataSet;

    /**
     * return a new instance of the fragment
     */
    public static Fragment newInstance() {
        BookSeatsContentPage fragment = new BookSeatsContentPage();

        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab01layout, container, false);
        //Test Data Gen
        mDataSet=new FloorInfo[0];
        new DummyBackgroundTask().execute();
        genTestData();
        if (isfirstin){
            //mDataSet=new FloorInfo[12];
            initiateRefresh();
        }
        // Retrieve the SwipeRefreshLayout and ListView instances
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.curFloorsSwipeRefresh);

        // BEGIN_INCLUDE (change_colors)
        // Set the color scheme of the SwipeRefreshLayout by providing 4 color resource ids
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        // END_INCLUDE (change_colors)

        mFloorInfoRecyclerView = (RecyclerView) view.findViewById(R.id.curFloorsRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mRecyclerViewLayoutManager = new GridLayoutManager(getActivity(),COLUMN_COUNT);
        mFloorInfoRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);

        mFloorInfoAdapter = new FloorInfoAdapter(mDataSet,getContext());
        // Set CustomAdapter as the adapter for RecyclerView.
        mFloorInfoRecyclerView.setAdapter(mFloorInfoAdapter);
        // END_INCLUDE(initializeRecyclerView)

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        genTestData();

        // BEGIN_INCLUDE (setup_refreshlistener)
        /**
         * Implement {@link SwipeRefreshLayout.OnRefreshListener}. When users do the "swipe to
         * refresh" gesture, SwipeRefreshLayout invokes
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}. In
         * {@link SwipeRefreshLayout.OnRefreshListener#onRefresh onRefresh()}, call a method that
         * refreshes the content. Call the same method in response to the Refresh action from the
         * action bar.
         */

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");

                initiateRefresh();
            }
        });
        // END_INCLUDE (setup_refreshlistener)
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void initiateRefresh() {
        //Log.i(LOG_TAG, "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        Toast.makeText(getActivity(),"正在加载座位信息...",Toast.LENGTH_SHORT).show();
        new DummyBackgroundTask().execute();
    }
    // END_INCLUDE (initiate_refresh)

    // BEGIN_INCLUDE (refresh_complete)
    /**
     * When the AsyncTask finishes, it calls onRefreshComplete(), which updates the data in the
     * ListAdapter and turns off the progress bar.
     */
    private void onRefreshComplete() {
       // Log.i(LOG_TAG, "onRefreshComplete");

        Toast.makeText(getActivity(),"加载完成",Toast.LENGTH_SHORT).show();
        // Remove all items from the ListAdapter, and then replace them with the new items
       // mDataSet=null;
        mFloorInfoAdapter.updateDataSet(mDataSet);
        mFloorInfoAdapter.notifyDataSetChanged();

        // Stop the refreshing indicator
        mSwipeRefreshLayout.setRefreshing(false);
    }
    // END_INCLUDE (refresh_complete)

    /**
     * Dummy {@link AsyncTask} which simulates a long running task to fetch new cheeses.
     */
    private class DummyBackgroundTask extends AsyncTask<Void, Void, Integer> {

        static final int TASK_DURATION = 3 * 1000; // 3 seconds

        @Override
        protected Integer doInBackground(Void... params) {

            OrderSeatService yuyueService=new OrderSeatService();
            // 获取当前日期+1
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
                UserinfoUtils userinfoUtils=new UserinfoUtils(getActivity());
                //System.out.println(userinfoUtils.get_LastId()+"==="+userinfoUtils.get_LastPassword());
                floorInfos=OrderSeatService.getFloorInfo(userinfoUtils.get_LastId(),userinfoUtils.get_LastPassword());
                isfirstin=false;
                mDataSet=new FloorInfo[12];
               for (int j=0;j<12;j++){
                    mDataSet[j]=new FloorInfo();
                    mDataSet[j].total=floorInfos.get(j).total;
                    mDataSet[j].rest=floorInfos.get(j).rest;
                    mDataSet[j].layer=floorInfos.get(j).layer;
                    System.out.println(mDataSet[j].total+"=="+mDataSet[j].layer+"=="+mDataSet[j].rest);
                }

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

    private void genTestData() {
        this.mDataSet =new FloorInfo[12];
        if (isfirstin) {
            for (int i = 0; i < 12;i++) {
               mDataSet[i] = new FloorInfo();
                mDataSet[i].layer = i + " Fl";
                mDataSet[i].total = 100;
                mDataSet[i].rest = 100;
            }
        }
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    //private static int i=0;
}
