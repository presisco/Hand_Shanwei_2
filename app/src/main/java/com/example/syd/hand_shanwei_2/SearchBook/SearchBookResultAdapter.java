package com.example.syd.hand_shanwei_2.SearchBook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.syd.hand_shanwei_2.Model.BookInfo;
import com.example.syd.hand_shanwei_2.R;

import java.util.List;

;

/**
 * Created by presisco on 2015/11/19.
 */

public class SearchBookResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SearchBookResultAdapter";
    private Context parent;
    private List<BookInfo> mDataSet;
    private OnUpdateDataInterface parentUpdateInterface;
    private static final Integer TYPE_FOOTER=1;
    private static final Integer TYPE_ITEM=0;
    private Boolean mShowFooter = true;

    public interface OnUpdateDataInterface{
        public void onUpdate();
    }
// BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        public ItemViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            cardView = (CardView) v.findViewById(R.id.bookInfoCardView);
        }

        public CardView getCardView() {
            return cardView;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        private final View rootView;
        public FooterViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            rootView = v;
        }

        public View getRootView() {
            return rootView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public SearchBookResultAdapter(List<BookInfo> dataSet, Context context, OnUpdateDataInterface source) {
        parentUpdateInterface = source;
        parent = context;
        mDataSet = dataSet;
    }

    public void updateDataSet(List<BookInfo> dataSet)
    {   mDataSet = dataSet;}

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v=null;
        if(viewType==TYPE_ITEM){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.book_info_cardview, viewGroup, false);
            return new ItemViewHolder(v);
        }
        else if(viewType==TYPE_FOOTER){
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recyclerview_update_footer, viewGroup, false);
            return new FooterViewHolder(v);
        }
        return null;
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        if(viewHolder instanceof ItemViewHolder) {
            final CardView cardView = ((ItemViewHolder)viewHolder).getCardView();
            ((TextView) cardView.findViewById(R.id.floorNameTextView))
                    .setText(mDataSet.get(position).name);
            ((TextView) cardView.findViewById(R.id.bookIdTextView))
                    .setText(mDataSet.get(position).code);
            ((TextView) cardView.findViewById(R.id.bookAuthorTextView))
                    .setText(mDataSet.get(position).detail);
            ((TextView) cardView.findViewById(R.id.floorUsageTextView))
                    .setText(parent.getResources().getString(R.string.floor_info_cap)
                            + mDataSet.get(position).total
                            + parent.getResources().getString(R.string.book_unit) + ";"
                            + parent.getResources().getString(R.string.floor_info_cur)
                            + mDataSet.get(position).rest
                            + parent.getResources().getString(R.string.book_unit));
            ((TextView) cardView.findViewById(R.id.bookPosTextView))
                    .setText(mDataSet.get(position).marcno);
            //点击"地图"的响应
            ((ImageView) cardView.findViewById(R.id.bookMapImageView)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 2015/11/22
                   // Log.i("bac","查看地图");
                    Intent intent=new Intent(parent,SeeMap.class);
                    intent.putExtra("floor",mDataSet.get(position).marcno);
                    intent.putExtra("bookname",mDataSet.get(position).name);
                    intent.putExtra("bookcode",mDataSet.get(position).code);
                    parent.startActivity(intent);
                }
            });
            //点击"收藏"的响应
            final ToggleButton toggleButton=(ToggleButton) cardView.findViewById(R.id.btnsavemap);
            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            BookRouteHistoryHelper bookRouteHistoryHelper = new BookRouteHistoryHelper(parent);
                    if (toggleButton.isChecked()){
                    toggleButton.setBackgroundResource(R.drawable.add_down);
                        boolean b=bookRouteHistoryHelper.insertBookRoute(mDataSet.get(position).name,mDataSet.get(position).code,mDataSet.get(position).detail,mDataSet.get(position).marcno);
                        if (b){
                            Toast.makeText(parent,"收藏路线",Toast.LENGTH_SHORT).show();
                        }
                        Log.i("bac", "搜藏" + b);
                    }else {
                        Toast.makeText(parent,"取消收藏",Toast.LENGTH_SHORT).show();
                        toggleButton.setBackgroundResource(R.drawable.add_on);
                        bookRouteHistoryHelper.deleteBookRoute(mDataSet.get(position).code);
                    }
                    // TODO: 2015/11/22

                }
            });
        }
        else if(viewHolder instanceof FooterViewHolder)
        {
            View rootView=((FooterViewHolder)viewHolder).getRootView();
            rootView.findViewById(R.id.recyclerViewFooterTextView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentUpdateInterface.onUpdate();
                }
            });
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mShowFooter)
            return mDataSet.size() + 1;
        else
            return mDataSet.size();
    }
    @Override
    public int getItemViewType(int position){
        if(position+1==getItemCount())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }
    public void appendData(List<BookInfo> append)
    {
        mDataSet.addAll(append);
    }
    public void showFooter() {
        mShowFooter = true;
    }

    public void hideFooter() {
        mShowFooter = false;
    }
}