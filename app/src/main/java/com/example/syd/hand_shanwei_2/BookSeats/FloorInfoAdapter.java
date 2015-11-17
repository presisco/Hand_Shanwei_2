package com.example.syd.hand_shanwei_2.BookSeats;

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
        TextView op = (TextView) viewHolder.getCardView().findViewById(R.id.floorNameTextView);
        op.setText(mDataSet[position].layer);
        op = (TextView) viewHolder.getCardView().findViewById(R.id.floorCapTextView);
        op.setText(parent.getResources().getString(R.string.floor_info_cap)+mDataSet[position].total);
        op = (TextView) viewHolder.getCardView().findViewById(R.id.floorCurrentTextView);
        op.setText(parent.getResources().getString(R.string.floor_info_cur)+mDataSet[position].rest);

        viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mSelectSeatIntent = new Intent(parent, SelectSeatActivity.class);
                mSelectSeatIntent.putExtra("random", false);
                mSelectSeatIntent.putExtra("floorid", mDataSet[position].layer);
                parent.startActivity(mSelectSeatIntent);
                Log.d(TAG, "Element " + position + " clicked.");
            }
        });

    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}
