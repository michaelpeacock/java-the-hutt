package com.mse.android.javathehutt;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

/**
 * Created by jamie2 on 12/15/15.
 */
public class MonitorTripFragment extends Fragment {

    private RecyclerView mTripRecyclerView;
    private MonitorTripAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_trip, container, false);
        mTripRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mTripRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        List<TripClass> trips = null;
        MapDataSingleton instance = MapDataSingleton.getInstance(getActivity());
        trips = (List<TripClass>) instance.getTrips();
        if (mAdapter == null) {
            mAdapter = new MonitorTripAdapter(trips);
            mTripRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class MonitorTripHolder extends RecyclerView.ViewHolder
                              implements View.OnClickListener {

        private ImageView mImageView;
        private TextView mTitleTextView;
        private TextView mDepartureDateTextView;
        private TextView mArrivalDateTextView;
        private CheckBox mTripCompleteCheckBox;

        private TripClass mTripClass;

        public void bindTrip(TripClass tripClass) {
            mTripClass = tripClass;
            mTitleTextView.setText(mTripClass.getVesselName());
            Date date = mTripClass.getDepartDate();
            System.out.println("raw date = "+date.toString());
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = formatter.format(date);
            mDepartureDateTextView.setText(s);
            date = mTripClass.getReturnDate();
            s = formatter.format(date);
            mArrivalDateTextView.setText(s);
            Date now = new Date();

            if (now.getDate() > date.getDate() && false == mTripClass.isCompleted())
            {
                mDepartureDateTextView.setTextColor(Color.rgb(255,0,0));
                mArrivalDateTextView.setTextColor(Color.rgb(255,0,0));
                mTitleTextView.setTextColor(Color.rgb(255,0,0));
            }
            else
            {
                mDepartureDateTextView.setTextColor(Color.rgb(0,255,50));
                mArrivalDateTextView.setTextColor(Color.rgb(0,255,50));
                mTitleTextView.setTextColor(Color.rgb(0,255,50));
            }
            mTripCompleteCheckBox.setChecked(mTripClass.isCompleted());

        }
        public MonitorTripHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_trip_vessel_name_text_view);
            mDepartureDateTextView = (TextView) itemView.findViewById(R.id.list_item_departure_date_text_view);
            mArrivalDateTextView = (TextView) itemView.findViewById(R.id.list_item_arrival_date_text_view);
            mTripCompleteCheckBox = (CheckBox) itemView.findViewById(R.id.list_trip_complete);

        }

        public void onClick(View v) {

        }
    }

    private class MonitorTripAdapter extends RecyclerView.Adapter<MonitorTripHolder> {

        private List<TripClass> mTrips;

        public MonitorTripAdapter(List<TripClass> trips) {
            mTrips = trips;
        }

        @Override
        public MonitorTripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                        .inflate(R.layout.list_item_trip, parent, false);
            return new MonitorTripHolder(view);
        }

        @Override
        public void onBindViewHolder(MonitorTripHolder holder, int position) {
            TripClass tripClass = mTrips.get(position);
            holder.bindTrip(tripClass);
        }

        @Override
        public int getItemCount() {
            return mTrips.size();
        }
    }
}
