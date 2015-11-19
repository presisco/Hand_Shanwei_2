package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.BookSeats.SeatInfoAdapter;
import com.example.syd.hand_shanwei_2.HttpService.SearchBookService.SearchBookService;
import com.example.syd.hand_shanwei_2.Model.BookInfo;
import com.example.syd.hand_shanwei_2.Model.BookListState;
import com.example.syd.hand_shanwei_2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by syd on 2015/11/18.
 */
public class SearchBookResultActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;

    private Button mSecondarySearchBtn;
    private EditText mSecondarySearchEditText;
    private Spinner mSearchTypeSpinner;
    private ArrayAdapter<String> mSearchTypeArrayAdapter;
    //结果页面状态
    private BookListState bookListState;

    //搜索结果列表
    private SearchBookResultAdapter mSearchBookResultAdapter;
    private RecyclerView mSearchBookResultRecyclerView;

    private String mPrimaryKeyWord;
    private String mPrimaryType;
    private String mSecondaryKeyWord;
    private String mSecondaryType;

    private Integer mCurPage=0;
    private Integer mTotalPage=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book_result);

        intent=getIntent();
        mSecondarySearchEditText=(EditText)findViewById(R.id.bookSearchResultFilter);
        mSearchBookResultRecyclerView = (RecyclerView) findViewById(R.id.bookSearchResultRecyclerView);
        mSecondarySearchBtn= (Button) findViewById(R.id.bookSecondarySearchButton);
        mSecondarySearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSecondaryKeyWord = mSecondarySearchEditText.getText().toString().trim();
                switch(mSearchTypeSpinner.getSelectedItemPosition())
                {
                    case 0:mSecondaryType=SearchBookConst.SEARCH_TYPE_BOOKNAME;break;
                    case 1:mSecondaryType=SearchBookConst.SEARCH_TYPE_AUTHOR;break;
                    default:return;
                }
                new GetBookListSecondary().execute();
            }
        });

        final String type_item[] = {getResources().getString(R.string.book_name),
                getResources().getString(R.string.book_author)};
        mSearchTypeArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_item);
        mSearchTypeSpinner=(Spinner)findViewById(R.id.filtOptionSpinner);
        mSearchTypeSpinner.setAdapter(mSearchTypeArrayAdapter);
        mSearchTypeSpinner.setSelection(0, true);

        RecyclerView.LayoutManager mRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mSearchBookResultRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mSearchBookResultAdapter=new SearchBookResultAdapter(new ArrayList<BookInfo>(),this);
        mSearchBookResultRecyclerView.setAdapter(mSearchBookResultAdapter);

        // 获得意图
        Intent intent = getIntent();
        // 读取数据
        mPrimaryKeyWord = intent.getStringExtra(SearchBookConst.SEARCH_BOOK_KEY);
        mPrimaryType=intent.getStringExtra(SearchBookConst.SEARCH_TYPE);
        mCurPage=1;
        new GetBookListPrimary().execute();

//        if (code != 2) {
//            queryBook = intent.getStringExtra("queryBook");
//
//            if (100 == code) {
//                //thHost.setCurrentTab(0);
//                TitleSearch = true;
//                onceType = "title";
//                bookListState.currPage = 0;
//                new GetBookList().execute(queryBook, bookListState.currPage + 1
//                        + "");
//                Toast.makeText(SearchBookResultActivity.this, "如果对搜索结果不满意，可以在菜单中选择二次检索",Toast.LENGTH_SHORT).show();
//
//            } else if (200 == code) {
////                thHost.setCurrentTab(0);
//                AuthorSearch = true;
//                onceType = "author";
//                bookListState.currPage = 0;
//                new GetBookList().execute(queryBook, bookListState.currPage + 1
//                        + "");
//                Toast.makeText(SearchBookResultActivity.this, "如果对搜索结果不满意，可以在菜单中选择二次检索",Toast.LENGTH_SHORT).show();
//
//            }
//        } else {
            // books = borrow.findAll();
            // layerRecyclerView.setAdapter(layerAdapter);
            // layerRecyclerView.invalidate();
//            thHost.setCurrentTab(1);

//        }
        // 更改背景颜色
        /*for (int i = 0; i < thHost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) thHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);
            tv.setTextColor(Color.WHITE);
            tv.setTypeface(Typeface.SERIF, 2);
            tv.setTextSize(15);
        }*/
//        thHost.setOnTabChangedListener(new TabChangeListener()); // 设置tab界面跳转的监听，用于每个列表的数据的刷新操作
        // //设置列表的长按监听，主要用于列表的删除操作
//        layerRecyclerView.setOnItemLongClickListener(new ItOnItemLongClickListener());
//        bookRecyclerView.setMode(Mode.PULL_FROM_END);// 设置刷新方式，底部刷新
//        bookListState.currPage = 0;
        // 刷新监听，只要图书没有加载到最后继续刷新
       /* mSearchBookResultRecyclerView.setOnRefreshListener(new OnRefreshListener<RecyclerView>() {
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (bookListState.totalPage <= bookListState.currPage) {
                    Toast.makeText(getApplicationContext(), "没有图书了",
                            Toast.LENGTH_SHORT).show();
                    new GetBookList().execute();
                } else
                    new GetBookList().execute(queryBook, bookListState.currPage + 1
                            + "");
            }
        });*/


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
    }

    private class GetBookListSecondary extends AsyncTask<String, Void, String> { // 加载检索结果
        /**
         * 根据获取到的HTML文件进行解析，获取图书信息，并且在RecyclerView中得到显示
         */
        protected void onPostExecute(String result) {
            if (result != null) {
                // 获取到图书信息，存储到book集合中
                List<BookInfo> list = SearchBookService.getBookList(result);
                if (list != null) {
                    mSearchBookResultAdapter.appendData(list);
                    mSearchBookResultAdapter.notifyDataSetChanged();
                    mTotalPage =SearchBookService.getListState(result).totalPage; // 获取页数信息
                    mCurPage=SearchBookService.getListState(result).currPage;
                } else {
                    Toast.makeText(getApplicationContext(), "没有找到..",
                                Toast.LENGTH_SHORT).show();
                }
            }
            //mSearchBookResultRecyclerView.onRefreshComplete();
            super.onPostExecute(result);
        }

        /**
         * 获取搜索内容 返回解析到的HTML文件 arg0[0] title arg0[1] page
         */

        protected String doInBackground(String... arg0) {
            return SearchBookService.queryTwice(mPrimaryKeyWord, mPrimaryType, mSecondaryKeyWord,
                    mSecondaryType, mCurPage+1);
        }
    }

    private class GetBookListPrimary extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            return SearchBookService.getPage(mPrimaryKeyWord,"",mCurPage);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // 获取到图书信息，存储到book集合中
                List<BookInfo> list = SearchBookService.getBookList(result);
                if (list != null) {
                    mSearchBookResultAdapter.updateDataSet(list);
                    mSearchBookResultAdapter.notifyDataSetChanged();
                    mTotalPage =SearchBookService.getListState(result).totalPage; // 获取页数信息
                    mCurPage=SearchBookService.getListState(result).currPage;
                } else {
                    Toast.makeText(getApplicationContext(), "没有找到..",
                            Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(result);
        }
    }
}
