package com.example.syd.hand_shanwei_2.OrderSeats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.R;

import com.example.syd.hand_shanwei_2.Model.FloorInfo;

/**
 * Created by presisco on 2015/11/16.
 */
public class FloorInfoAdapter extends RecyclerView.Adapter<FloorInfoAdapter.ViewHolder> {
    private static final String TAG = "FloorInfoAdapter";
    private Context parent;
    private FloorInfo[] mDataSet;

// BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            cardView = (CardView) v.findViewById(R.id.floorInfoCardView);
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
    public FloorInfoAdapter(FloorInfo[] dataSet,Context context) {
        parent=context;
        mDataSet = dataSet;
    }
    public void updateDataSet(FloorInfo[] dataset)
    {
        mDataSet=dataset;
    }
    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.library_floor_info_cardview, viewGroup, false);
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
        TextView op1 = (TextView) viewHolder.getCardView().findViewById(R.id.bookNameTextView);
        if (op1!=null){
        op1.setText(mDataSet[position].layer);
        }
        TextView op2 = (TextView) viewHolder.getCardView().findViewById(R.id.bookIdTextView);
        if (op2!=null){

        op2.setText(parent.getResources().getString(R.string.floor_info_cap)+mDataSet[position].total);
        }
        TextView op3 = (TextView) viewHolder.getCardView().findViewById(R.id.bookCountTextView);
        if (op3!=null){
        op3.setText(parent.getResources().getString(R.string.floor_info_cur) + mDataSet[position].rest);
        }
        if (mDataSet[position].rest<=0)
        //viewHolder.getCardView().setCardBackgroundColor(R.color.red);
        {
            op1.setTextColor(parent.getResources().getColor(R.color.red));
            op2.setTextColor(parent.getResources().getColor(R.color.red));
            op3.setTextColor(parent.getResources().getColor(R.color.red));
        }else if (mDataSet[position].rest<10){
            op1.setTextColor(parent.getResources().getColor(R.color.orange));
            op2.setTextColor(parent.getResources().getColor(R.color.orange));
            op3.setTextColor(parent.getResources().getColor(R.color.orange));
        }else {
            op1.setTextColor(parent.getResources().getColor(R.color.black));
            op2.setTextColor(parent.getResources().getColor(R.color.black));
            op3.setTextColor(parent.getResources().getColor(R.color.black));
        }
       /* viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mSelectSeatIntent = new Intent(parent, SelectSeatActivity.class);
                mSelectSeatIntent.putExtra("random", false);
                mSelectSeatIntent.putExtra("floorname", mDataSet[position].layer);
                parent.startActivity(mSelectSeatIntent);
                Log.d(TAG, "Element " + position + " clicked.");
            }
        });*/

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
