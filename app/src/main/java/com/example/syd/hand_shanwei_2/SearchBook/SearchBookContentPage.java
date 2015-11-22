package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.syd.hand_shanwei_2.R;

/**
 * Created by Presisco on 2015/9/28.
 */
public class SearchBookContentPage extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = SearchBookContentPage.class.getSimpleName();
    private static final Integer MAX_HISTORY_COUNT=10;
    private Button mSearchByAuthorBtn;
    private Button mSearchByBooknameBtn;

    private AutoCompleteTextView mSearchKeyInput;
    ArrayAdapter<String> mSearchHistoryAdapter;
    BookSearchHistoryHelper mHistoryHelper;

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
        mSearchByAuthorBtn = (Button) view.findViewById(R.id.searchByAuthorBtn);
        mSearchByBooknameBtn = (Button) view.findViewById(R.id.searchByBookNameBtn);
        mSearchKeyInput = (AutoCompleteTextView) view.findViewById(R.id.searchKeyInput);
        mSearchByBooknameBtn.setOnClickListener(this);
        mSearchByAuthorBtn.setOnClickListener(this);
        initAutoComplete("history", mSearchKeyInput);  //搜索历史的存储显示
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
            case R.id.searchByAuthorBtn:
                saveHistory();
                Intent intent1 = new Intent(getActivity(),SearchBookResultActivity.class);
                intent1.putExtra(SearchBookConst.SEARCH_BOOK_KEY, mSearchKeyInput.getText().toString().trim());
                intent1.putExtra(SearchBookConst.SEARCH_TYPE, SearchBookConst.SEARCH_TYPE_AUTHOR);
                startActivity(intent1);
                // TODO: 2015/11/18  
                break;
            case R.id.searchByBookNameBtn:
                // TODO: 2015/11/18
                saveHistory();
                Intent intent = new Intent(getActivity(), SearchBookResultActivity.class);
                intent.putExtra(SearchBookConst.SEARCH_BOOK_KEY, mSearchKeyInput.getText().toString().trim());
                intent.putExtra(SearchBookConst.SEARCH_TYPE, SearchBookConst.SEARCH_TYPE_BOOKNAME);
                startActivity(intent);
                break;
        }
    }
    private void saveHistory() {
        String text = mSearchKeyInput.getText().toString();
        mHistoryHelper.addSearchHistory(text);
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
                                  final AutoCompleteTextView autoCompleteTextView) {
        mHistoryHelper =new BookSearchHistoryHelper(getActivity(),null,1);

        mSearchHistoryAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, mHistoryHelper.querySearchHistory(MAX_HISTORY_COUNT));
        autoCompleteTextView.setAdapter(mSearchHistoryAdapter);
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
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AutoCompleteTextView)v).showDropDown();
            }
        });
    }
}
