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
import com.ats.monginis_communication.adapter.MessageAdapter;
import com.ats.monginis_communication.adapter.NotificationAdapter;
import com.ats.monginis_communication.bean.Message;
import com.ats.monginis_communication.bean.NotificationData;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.interfaces.NotificationsInterface;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment implements NotificationsInterface {

    private RecyclerView rvNotification;
    NotificationAdapter adapter;

    DatabaseHandler db;

    ArrayList<NotificationData> notificationArray;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        rvNotification = view.findViewById(R.id.rvNotification);

        db = new DatabaseHandler(getActivity());

        notificationArray = db.getAllSqliteNotifications();

        adapter = new NotificationAdapter(notificationArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvNotification.setLayoutManager(mLayoutManager);
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(adapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("NOTIFICATION")) {
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
    public void onPause() {

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void fragmentGetVisible() {
        db.updateNotificationRead();

        notificationArray.clear();
        notificationArray = db.getAllSqliteNotifications();

        adapter = new NotificationAdapter(notificationArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvNotification.setLayoutManager(mLayoutManager);
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        db.updateNotificationRead();

        notificationArray.clear();
        notificationArray = db.getAllSqliteNotifications();

        adapter = new NotificationAdapter(notificationArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvNotification.setLayoutManager(mLayoutManager);
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(adapter);

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("NOTIFICATION"));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA_FR"));

    }

    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        Gson gson = new Gson();
        NotificationData notificationData = gson.fromJson(intent.getStringExtra("message"), NotificationData.class);

        String dateStr = notificationData.getDate();
        try {
            long dateLong = Long.parseLong(dateStr);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            notificationData.setDate(sdf.format(dateLong));

        } catch (Exception e) {
        }

        Log.e("Msg ", "------------------- " + notificationData);
        if (notificationData != null) {
            notificationArray.add(0, notificationData);
            adapter.notifyDataSetChanged();
        }
    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        adapter.notifyDataSetChanged();
    }

}
