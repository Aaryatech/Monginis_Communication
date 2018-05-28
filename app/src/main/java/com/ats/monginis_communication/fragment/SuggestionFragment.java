package com.ats.monginis_communication.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.ats.monginis_communication.activity.AddSuggestionActivity;
import com.ats.monginis_communication.activity.InnerTabActivity;
import com.ats.monginis_communication.activity.SuggestionDetailActivity;
import com.ats.monginis_communication.adapter.NotificationAdapter;
import com.ats.monginis_communication.adapter.SuggestionAdapter;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.NotificationData;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.interfaces.LaunchProductInterface;
import com.ats.monginis_communication.interfaces.SuggestionInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SuggestionFragment extends Fragment implements SuggestionInterface, View.OnClickListener {

    private RecyclerView rvSuggestion;
    DatabaseHandler db;
    SuggestionAdapter adapter;
    private FloatingActionButton fabAddSuggestion;

    ArrayList<SuggestionData> suggestionDataArray;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        rvSuggestion = view.findViewById(R.id.rvSuggestion);
        fabAddSuggestion = view.findViewById(R.id.fabAddSuggestion);
        fabAddSuggestion.setOnClickListener(this);

        db = new DatabaseHandler(getActivity());

        suggestionDataArray = db.getAllSQLiteSuggestions();
        adapter = new SuggestionAdapter(suggestionDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvSuggestion.setLayoutManager(mLayoutManager);
        rvSuggestion.setItemAnimator(new DefaultItemAnimator());
        rvSuggestion.setAdapter(adapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("SUGGESTION")) {
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
        Log.e("SUGGESTION", "  ON PAUSE");

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SUGG DET", "  ON RESUME");

        db.updateSuggestionRead();

        suggestionDataArray = db.getAllSQLiteSuggestions();
        adapter = new SuggestionAdapter(suggestionDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvSuggestion.setLayoutManager(mLayoutManager);
        rvSuggestion.setItemAnimator(new DefaultItemAnimator());
        rvSuggestion.setAdapter(adapter);


        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("SUGGESTION"));

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA_FR"));


    }


    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        Gson gson = new Gson();
        SuggestionData suggestionData = gson.fromJson(intent.getStringExtra("message"), SuggestionData.class);

        // int refreshId = intent.getIntExtra("refresh", 0);
        Log.e("Msg " + suggestionData, "------------------- ");
        if (suggestionData != null) {

            suggestionDataArray.add(0, suggestionData);
            adapter.notifyDataSetChanged();
        }
    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fragmentGetVisible() {
        db.updateSuggestionRead();

        Log.e("SUGGESTION FRG", "---------------VISIBLE--------------");

        suggestionDataArray.clear();
        suggestionDataArray = db.getAllSQLiteSuggestions();
        adapter = new SuggestionAdapter(suggestionDataArray, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvSuggestion.setLayoutManager(mLayoutManager);
        rvSuggestion.setItemAnimator(new DefaultItemAnimator());
        rvSuggestion.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fabAddSuggestion) {
            startActivity(new Intent(getContext(), AddSuggestionActivity.class));
        }
    }


}
