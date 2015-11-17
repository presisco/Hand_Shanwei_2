package com.example.syd.hand_shanwei_2.BookSeats;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.syd.hand_shanwei_2.Model.FloorInfo;
import com.example.syd.hand_shanwei_2.R;

/**
 * Created by presisco on 2015/11/16.
 */
public class SeatInfoAdapter extends RecyclerView.Adapter<SeatInfoAdapter.ViewHolder> {
    private static final String TAG = "FloorInfoAdapter";
    private Context parent;
    private FloorInfo[] mDataSet;
    private int selected=-1;
    private FloorInfo mSelectedSeat;
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
    public SeatInfoAdapter(FloorInfo[] dataSet,Context context) {
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
        final CardView cardView=viewHolder.getCardView();
        TextView op = (TextView) cardView.findViewById(R.id.seatIdTextView);
        op.setText(mDataSet[position].layer);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected!=-1)
                    curSelected.setCardBackgroundColor(parent.getResources().getColor(R.color.default_background_color));
                cardView.setCardBackgroundColor(parent.getResources().getColor(R.color.cardview_picked_color));
                selected=position;
                curSelected=cardView;
                mSelectedSeat=mDataSet[position];
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

    public FloorInfo getSelectedSeat()
    {
        return mSelectedSeat;
    }
}
