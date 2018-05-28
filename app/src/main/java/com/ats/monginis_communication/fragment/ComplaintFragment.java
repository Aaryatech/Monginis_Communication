package com.ats.monginis_communication.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.AddComplaintActivity;
import com.ats.monginis_communication.activity.AddSuggestionActivity;
import com.ats.monginis_communication.activity.ComplaintDetailActivity;
import com.ats.monginis_communication.activity.InnerTabActivity;
import com.ats.monginis_communication.activity.SuggestionDetailActivity;
import com.ats.monginis_communication.adapter.ComplaintAdapter;
import com.ats.monginis_communication.adapter.SuggestionAdapter;
import com.ats.monginis_communication.bean.ComplaintData;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.interfaces.ComplaintInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintFragment extends Fragment implements ComplaintInterface, View.OnClickListener {

    private RecyclerView rvComplaint;
    DatabaseHandler db;
    FloatingActionButton fabAdd;

    ComplaintAdapter adapter;

    ArrayList<ComplaintData> complaintDataArrayList;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);

        rvComplaint = view.findViewById(R.id.rvComplaint);
        fabAdd = view.findViewById(R.id.fabAddComplaint);
        fabAdd.setOnClickListener(this);

        db = new DatabaseHandler(getActivity());

        complaintDataArrayList = db.getAllSQLiteComplaints();

        adapter = new ComplaintAdapter(complaintDataArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvComplaint.setLayoutManager(mLayoutManager);
        rvComplaint.setItemAnimator(new DefaultItemAnimator());
        rvComplaint.setAdapter(adapter);

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
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }


    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fragmentGetVisible() {
        db.updateComplaintRead();

        complaintDataArrayList.clear();
        complaintDataArrayList = db.getAllSQLiteComplaints();
        adapter = new ComplaintAdapter(complaintDataArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvComplaint.setLayoutManager(mLayoutManager);
        rvComplaint.setItemAnimator(new DefaultItemAnimator());
        rvComplaint.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        db.updateComplaintRead();

        complaintDataArrayList.clear();
        complaintDataArrayList = db.getAllSQLiteComplaints();
        adapter = new ComplaintAdapter(complaintDataArrayList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvComplaint.setLayoutManager(mLayoutManager);
        rvComplaint.setItemAnimator(new DefaultItemAnimator());
        rvComplaint.setAdapter(adapter);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA_FR"));

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabAddComplaint) {
            startActivity(new Intent(getContext(), AddComplaintActivity.class));
        }
    }


}
