package com.example.syd.hand_shanwei_2.BookSeats;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.R;
import com.example.syd.hand_shanwei_2.Model.FloorInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Presisco on 2015/9/28.
 */
public class BookSeatsContentPage extends Fragment{

    private static final String LOG_TAG = BookSeatsContentPage.class.getSimpleName();
    private static final Integer COLUMN_COUNT=3;
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
        genTestData();

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
        Log.i(LOG_TAG, "initiateRefresh");

        /**
         * Execute the background task, which uses {@link android.os.AsyncTask} to load the data.
         */
        new DummyBackgroundTask().execute();
    }
    // END_INCLUDE (initiate_refresh)

    // BEGIN_INCLUDE (refresh_complete)
    /**
     * When the AsyncTask finishes, it calls onRefreshComplete(), which updates the data in the
     * ListAdapter and turns off the progress bar.
     */
    private void onRefreshComplete() {
        Log.i(LOG_TAG, "onRefreshComplete");

        // Remove all items from the ListAdapter, and then replace them with the new items
        mDataSet=null;
        genTestData();

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
            // Sleep for a small amount of time to simulate a background-task
            try {
                Thread.sleep(TASK_DURATION);
            } catch (InterruptedException e) {
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
        mDataSet=new FloorInfo[12];
        for(int i=0;i<12;++i)
        {
            mDataSet[i]=new FloorInfo();
            mDataSet[i].mFloorName=i+" Fl";
            mDataSet[i].mCap=100;
            mDataSet[i].mCurrent=(int)Math.random()*100;
        }
    }
}
