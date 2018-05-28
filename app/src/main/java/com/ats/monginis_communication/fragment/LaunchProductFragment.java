package com.ats.monginis_communication.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.adapter.FeedbackAdapter;
import com.ats.monginis_communication.adapter.SuggestionAdapter;
import com.ats.monginis_communication.bean.FeedbackData;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.interfaces.LaunchProductInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class LaunchProductFragment extends Fragment implements LaunchProductInterface {

    private RecyclerView rvFeedback;
    DatabaseHandler db;
    FeedbackAdapter adapter;

    ArrayList<FeedbackData> feedbackDataArray;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch_product, container, false);

        rvFeedback = view.findViewById(R.id.rvFeedback);

        db = new DatabaseHandler(getActivity());

        feedbackDataArray = db.getAllSQLiteFeedback();

        adapter = new FeedbackAdapter(feedbackDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvFeedback.setLayoutManager(mLayoutManager);
        rvFeedback.setItemAnimator(new DefaultItemAnimator());
        rvFeedback.setAdapter(adapter);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("FEEDBACK")) {
                    handlePushNotification(intent);
                }
            }
        };

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA_FR")) {
                    handlePushNotification1(intent);
                }
            }
        };


        return view;
    }

    @Override
    public void fragmentGetVisible() {
        db.updateFeedbackRead();

        feedbackDataArray.clear();
        feedbackDataArray = db.getAllSQLiteFeedback();

        adapter = new FeedbackAdapter(feedbackDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvFeedback.setLayoutManager(mLayoutManager);
        rvFeedback.setItemAnimator(new DefaultItemAnimator());
        rvFeedback.setAdapter(adapter);

    }


    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        db.updateFeedbackRead();

        feedbackDataArray.clear();
        feedbackDataArray = db.getAllSQLiteFeedback();

        adapter = new FeedbackAdapter(feedbackDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvFeedback.setLayoutManager(mLayoutManager);
        rvFeedback.setItemAnimator(new DefaultItemAnimator());
        rvFeedback.setAdapter(adapter);


        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("FEEDBACK"));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA_FR"));

    }


    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        Gson gson = new Gson();
        FeedbackData feedbackData = gson.fromJson(intent.getStringExtra("message"), FeedbackData.class);

        String dateStr = feedbackData.getDate();
        try {
            long dateLong = Long.parseLong(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            feedbackData.setDate(sdf.format(dateLong));

        } catch (Exception e) {
        }

        Log.e("Msg ", "------------------- " + feedbackData);
        if (feedbackData != null) {
            feedbackDataArray.add(0, feedbackData);
            adapter.notifyDataSetChanged();
        }
    }

}
