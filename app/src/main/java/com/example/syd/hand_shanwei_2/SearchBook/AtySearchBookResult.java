package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.syd.hand_shanwei_2.Adapters.MyListviewAdapter;
import com.example.syd.hand_shanwei_2.Atys.MainActivity;
import com.example.syd.hand_shanwei_2.HttpService.OrderSeatService.OrderSeatService;
import com.example.syd.hand_shanwei_2.HttpService.SearchBookService.SearchBookService;
import com.example.syd.hand_shanwei_2.Model.Book;
import com.example.syd.hand_shanwei_2.Model.BookListState;
import com.example.syd.hand_shanwei_2.R;

import java.util.List;

/**
 * Created by syd on 2015/11/18.
 */
public class AtySearchBookResult extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    ListView bookresultview;
    Button btncanceltwice,btntwice;
    private int code;
    private String queryBook;
    private boolean TitleSearch;
    private boolean AuthorSearch;
    private List<Book> books;
    private boolean twiceSearch;
    private BookListState bookListState;
    private MyListviewAdapter myListviewAdapter;
    private boolean twiceSearchFlag = true; // 增加的二次搜索是否搜索到标志域
    String twiceKeyword, onceType, twiceType;// twiceKeyword第二次搜索的关键字、onceType第一次搜索的类型、twiceType第二次搜索的类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchbookresult);
        bookListState=new BookListState(0,100);
        intent=getIntent();
        bookresultview= (ListView) findViewById(R.id.bookresultlistview);
        btncanceltwice= (Button) findViewById(R.id.btncanceltwice );
        btntwice= (Button) findViewById(R.id.btntwicesearch);
        btntwice.setOnClickListener(this);
        btncanceltwice.setOnClickListener(this);
        // 获得意图
        Intent intent = getIntent();
        // 读取数据
        code = intent.getIntExtra("request", 100);
        if (code != 2) {
            queryBook = intent.getStringExtra("queryBook");

            if (100 == code) {
                //thHost.setCurrentTab(0);
                TitleSearch = true;
                onceType = "title";
                bookListState.currPage = 0;
                new GetBookList().execute(queryBook, bookListState.currPage + 1
                        + "");
                Toast.makeText(AtySearchBookResult.this, "如果对搜索结果不满意，可以在菜单中选择二次检索",Toast.LENGTH_SHORT).show();

            } else if (200 == code) {
//                thHost.setCurrentTab(0);
                AuthorSearch = true;
                onceType = "author";
                bookListState.currPage = 0;
                new GetBookList().execute(queryBook, bookListState.currPage + 1
                        + "");
                Toast.makeText(AtySearchBookResult.this, "如果对搜索结果不满意，可以在菜单中选择二次检索",Toast.LENGTH_SHORT).show();

            }
        } else {
            // books = borrow.findAll();
            // layerListView.setAdapter(layerAdapter);
            // layerListView.invalidate();
//            thHost.setCurrentTab(1);

        }
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
//        layerListView.setOnItemLongClickListener(new ItOnItemLongClickListener());
//        bookListView.setMode(Mode.PULL_FROM_END);// 设置刷新方式，底部刷新
        bookListState.currPage = 0;
        // 刷新监听，只要图书没有加载到最后继续刷新
       /* bookresultview.setOnRefreshListener(new OnRefreshListener<ListView>() {
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
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
    private class GetBookList extends AsyncTask<String, Void, String[]> { // 加载检索结果
        /**
         * 根据获取到的HTML文件进行解析，获取图书信息，并且在listview中得到显示
         */
        protected void onPostExecute(String[] result) {
            if (result != null) {
                // 获取到图书信息，存储到book集合中
                List<Book> list = SearchBookService.getBookList(result[0]);
               // Log.i("bac", list.size()+"");
                if (list != null) {
                    if (bookListState.currPage == 0) {
                        // 如果是第一次刷新列表，需要先建立适配器
                        myListviewAdapter= new MyListviewAdapter(AtySearchBookResult.this,list);
                        bookresultview.setAdapter(myListviewAdapter);
                    } else {
                        // 如果不是第一次搜索，需要在原来的集合基础上添加新的集合
                        myListviewAdapter.appendList(list);
                    }
                    bookListState =SearchBookService.getListState(result[0]); // 获取页数信息
                } else {
                    // 如果没有html文件，发出toast信息
                    if (twiceSearch == false || twiceSearch == true
                            && twiceSearchFlag == false)
                        Toast.makeText(getApplicationContext(), "没有找到..",
                                Toast.LENGTH_SHORT).show();
                    else if (twiceSearch == true && twiceSearchFlag == true)
                        twiceSearchFlag = false;
                }
            }
            //bookresultview.onRefreshComplete();
            super.onPostExecute(result);
        }

        /**
         * 获取搜索内容 返回解析到的HTML文件 arg0[0] title arg0[1] page
         */

        protected String[] doInBackground(String... arg0) {
            if (arg0.length == 0)
                return null;
            String str = "";
            // 通过判断TitleSearch AuthorSearch twiceSearch的值，决定调用的方法
            // 进行书籍名称的检索
            if (TitleSearch && !twiceSearch) {
                str = SearchBookService.getPage(arg0[0], "title",
                        Integer.parseInt(arg0[1]));
            } else
                // 进行按照作者进行检索
                if (AuthorSearch && !twiceSearch) {
                    str = SearchBookService.getPage(arg0[0], "author",
                            Integer.parseInt(arg0[1]));
                    Log.i("bac",str);
                } else
                    // 进行二次检索
                    if (twiceSearch) {
                        str = SearchBookService.queryTwice(queryBook, onceType, twiceKeyword,
                                twiceType, Integer.parseInt(arg0[1]));
                    }
            return new String[] { str };
        }
    }
}
