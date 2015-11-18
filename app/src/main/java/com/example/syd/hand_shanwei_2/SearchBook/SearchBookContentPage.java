package com.example.syd.hand_shanwei_2.SearchBook;

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
import android.content.SharedPreferences;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Presisco on 2015/9/28.
 */
public class SearchBookContentPage extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = SearchBookContentPage.class.getSimpleName();
    Button btnsearchbyauthor,btnsearchbybookname;
    //EditText etsearchbook;
    private AutoCompleteTextView et_search; //输入文本框
    String queryBook = "";//检索书名
    public static Fragment newInstance() {
        SearchBookContentPage fragment = new SearchBookContentPage();
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//    }
    /**
     * Fragment初始化入口，用于绘制界面和初始化，代码在return view和View view中间插入
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab02layout, container, false);
        return view;
    }

    /**
     * 界面绘制完毕，在super.onViewCreated()后加入代码
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnsearchbyauthor= (Button) view.findViewById(R.id.searchbyauthor);
        btnsearchbybookname= (Button) view.findViewById(R.id.searchbybookname);
        et_search= (AutoCompleteTextView) view.findViewById(R.id.etsearchbook);
        btnsearchbybookname.setOnClickListener(this);
        btnsearchbyauthor.setOnClickListener(this);
        initAutoComplete("history", et_search);  //搜索历史的存储显示
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchbyauthor:
                Intent intent1 = new Intent(getActivity(),AtySearchBookResult.class);
                intent1.putExtra("queryBook", et_search.getText().toString().trim());
                intent1.putExtra("request", 200);
                startActivity(intent1);
                // TODO: 2015/11/18  
                break;
            case R.id.searchbybookname:
                // TODO: 2015/11/18
                Intent intent = new Intent(getActivity(), AtySearchBookResult.class);
                intent.putExtra("queryBook", et_search.getText().toString().trim());
                intent.putExtra("request", 100);
                startActivity(intent);
                break;
        }
    }
    private void saveHistory(String field,
                             AutoCompleteTextView autoCompleteTextView) {
        String text = autoCompleteTextView.getText().toString();
        SharedPreferences sp = getActivity().getSharedPreferences("network_url", 0);
        String longhistory = sp.getString(field, "nothing");
        if (!longhistory.contains(text + ",")) {
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, text + ",");
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    /**
     * 初始化AutoCompleteTextView，最多显示5项提示，使 AutoCompleteTextView在一开始获得焦点时自动提示
     *
     * @param field
     *            保存在sharedPreference中的字段名
     * @param autoCompleteTextView
     *            要操作的AutoCompleteTextView
     */
    private void initAutoComplete(String field,
                                  AutoCompleteTextView autoCompleteTextView) {
        SharedPreferences sp = getActivity().getSharedPreferences("network_url", 0);
        String longhistory = sp.getString("history", "nothing");
        String[] histories = longhistory.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, histories);
        // 只保留最近的10条的记录
        if (histories.length > 10) {
            String[] newHistories = new String[5];
            System.arraycopy(histories, 0, newHistories, 0, 10);
            adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,newHistories);
        }
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView
                .setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        AutoCompleteTextView view = (AutoCompleteTextView) v;
                        if (hasFocus) {
                            view.showDropDown();
                        }
                    }
                });
    }
}
