package com.example.syd.hand_shanwei_2.OrderSeats;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.Model.SeatInfo;
import com.example.syd.hand_shanwei_2.R;

/**
 * Created by presisco on 2015/11/16.
 */
public class SeatInfoAdapter extends RecyclerView.Adapter<SeatInfoAdapter.ViewHolder> {
    private static final String TAG = "SeatInfoAdapter";
    private Context parent;
    private SeatInfo[] mDataSet;
    //设置为静态
    public static int selected=-1;
    private   SeatInfo mSelectedSeat;
    private CardView curSelected;
// BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            cardView = (CardView) v.findViewById(R.id.seatInfoCardView);
        }

        public CardView getCardView() {
            return cardView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public SeatInfoAdapter(SeatInfo[] dataSet,Context context) {
        parent=context;
        mDataSet = dataSet;
    }

    public void updateDataSet(SeatInfo[] dataSet)
    {   mDataSet=dataSet;}

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.seat_info_cardview, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        final CardView cardView=viewHolder.getCardView();
        TextView op = (TextView) cardView.findViewById(R.id.seatIdTextView);
        if(op!=null) {
            op.setText(mDataSet[position].id);
            if(selected!=-1) {
                if (mDataSet[position].id != mSelectedSeat.id) {
                    cardView.setCardBackgroundColor(parent.getResources().getColor(R.color.default_background_color));
                }
                else {
                    //更新选中项CardView引用
                    cardView.setCardBackgroundColor(parent.getResources().getColor(R.color.cardview_picked_color));
                    curSelected=cardView;
                }
            }
            //设置点击监听
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //恢复之前选中项的颜色
                    if (selected != -1)
                        curSelected.setCardBackgroundColor(parent.getResources().getColor(R.color.default_background_color));
                    //设置选中项颜色并记录
                    cardView.setCardBackgroundColor(parent.getResources().getColor(R.color.cardview_picked_color));
                    selected = position;
                    Log.i("bacground",selected+"9999999999999");
                    curSelected = cardView;
                    mSelectedSeat = mDataSet[position];
                    Log.d(TAG, "Element " + position + " clicked.");
                }
            });
        }
        else
        {
            Log.d(TAG,"Element "+ position +"cant fint TextView");
        }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
    public SeatInfo getSelectedSeat()
    {
        return mSelectedSeat;
    }
}
