package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by syd on 2015/11/18.
 */
public class SearchBookResultActivity extends AppCompatActivity implements SearchBookResultAdapter.OnUpdateDataInterface{

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
    private Boolean mIsPrimarySearch=true;

    private Integer mCurPage=0;
    private Integer mTotalPage=0;
    private Integer lastVisibileItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book_result);

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
                mIsPrimarySearch=false;
                mCurPage=1;
                Toast.makeText(SearchBookResultActivity.this, "正在查找", Toast.LENGTH_SHORT).show();
                new GetBookList().execute();
            }
        });

        final String type_item[] = {getResources().getString(R.string.book_name),
                getResources().getString(R.string.book_author)};
        mSearchTypeArrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type_item);
        mSearchTypeSpinner=(Spinner)findViewById(R.id.filtOptionSpinner);
        mSearchTypeSpinner.setAdapter(mSearchTypeArrayAdapter);
        mSearchTypeSpinner.setSelection(0, true);

        final RecyclerView.LayoutManager mRecyclerViewLayoutManager = new GridLayoutManager(this, 1);
        mSearchBookResultRecyclerView.setLayoutManager(mRecyclerViewLayoutManager);
        mSearchBookResultAdapter = new SearchBookResultAdapter(new ArrayList<BookInfo>(), this, this);
        mSearchBookResultAdapter.hideFooter();
        mSearchBookResultRecyclerView.setAdapter(mSearchBookResultAdapter);

        // 获得意图
        // 读取数据
        Intent intent=getIntent();
        mPrimaryKeyWord = intent.getStringExtra(SearchBookConst.SEARCH_BOOK_KEY);
        mPrimaryType=intent.getStringExtra(SearchBookConst.SEARCH_TYPE);
        mCurPage=1;
        Toast.makeText(SearchBookResultActivity.this, "正在查找", Toast.LENGTH_SHORT).show();
        new GetBookList().execute();
    }

    @Override
    public void onUpdate()
    {
        mCurPage+=1;
        Toast.makeText(SearchBookResultActivity.this, "正在查找", Toast.LENGTH_SHORT).show();
        new GetBookList().execute();
    }

    private class GetBookList extends AsyncTask<String, Void, String> { // 加载检索结果
        /**
         * 根据获取到的HTML文件进行解析，获取图书信息，并且在RecyclerView中得到显示
         */

        public List<BookInfo> list;

        protected void onPostExecute(String result) {
            //mSearchBookResultRecyclerView.onRefreshComplete();
            if (result != null) {
                // 获取到图书信息，存储到book集合中

                Log.d("SearchBookResult", "Book count at this page:" + list.size());
                if (list != null) {
                    if (mCurPage != 1) {
                        mSearchBookResultAdapter.appendData(list);
                    } else {
                        mSearchBookResultAdapter.updateDataSet(list);
                    }

                    if (mCurPage == mTotalPage)
                        mSearchBookResultAdapter.hideFooter();
                    else
                        mSearchBookResultAdapter.showFooter();

                    mSearchBookResultAdapter.notifyDataSetChanged();
                    mTotalPage = SearchBookService.getListState(result).totalPage; // 获取页数信息
                    mCurPage = SearchBookService.getListState(result).currPage;
                    Log.d("SearchBookResult", "SecondarySearch Result Count:" + mSearchBookResultAdapter.getItemCount());
                } else {
                    Toast.makeText(getApplicationContext(), "没有找到..",
                            Toast.LENGTH_SHORT).show();
                }
            }
            super.onPostExecute(result);
        }

        /**
         * 获取搜索内容 返回解析到的HTML文件 arg0[0] title arg0[1] page
         */

        protected String doInBackground(String... arg0) {
            String result = "";
            if (mIsPrimarySearch)
                result = SearchBookService.getPage(mPrimaryKeyWord, mPrimaryType, mCurPage);
            else
                result = SearchBookService.queryTwice(mPrimaryKeyWord, mPrimaryType, mSecondaryKeyWord,
                    mSecondaryType, mCurPage);
            list = SearchBookService.getBookList(result);
            return result;
        }
    }
}
