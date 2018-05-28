package com.ats.monginis_communication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.adapter.FeedbackDetailAdapter;
import com.ats.monginis_communication.adapter.SuggestionDetailAdapter;
import com.ats.monginis_communication.bean.ComplaintDetail;
import com.ats.monginis_communication.bean.FeedbackData;
import com.ats.monginis_communication.bean.FeedbackDetail;
import com.ats.monginis_communication.bean.Franchisee;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.bean.SuggestionDetail;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvFeedbackDetail;
    DatabaseHandler db;
    FeedbackDetailAdapter adapter;

    private LinearLayout llSend;
    private EditText edMessage;

    ImageView ivHeaderImage;

    int feedbackId = 0;
    FeedbackData data = new FeedbackData();

    int userId, refresh;
    String userName = "";

    ArrayList<FeedbackDetail> feedbackDetailArrayList;

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);

        rvFeedbackDetail = findViewById(R.id.rvFeedbackChat);
        edMessage = findViewById(R.id.edFeedbackDetail_Chat);
        llSend = findViewById(R.id.llFeedbackDetail_Send);
        ivHeaderImage = findViewById(R.id.ivFeedbackDetail_header);

        llSend.setOnClickListener(this);

        db = new DatabaseHandler(this);


        Toolbar toolbar = findViewById(R.id.toolbar_FeedbackDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("franchise", "");
        Franchisee userBean = gson.fromJson(json2, Franchisee.class);
        try {
            if (userBean != null) {
                userId = userBean.getFrId();
                userName = userBean.getFrName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Intent mIntent = getIntent();
            feedbackId = mIntent.getIntExtra("feedbackId", 0);
            refresh = mIntent.getIntExtra("refresh", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (feedbackId > 0) {
            data = db.getFeedback(feedbackId);
        }

        Log.e("FEEDBACK DATA", "------------" + data);


        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar_FeedbackDetail);
        collapsingToolbar.setTitle(data.getTitle());
        collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#ffffff"));

        String image = Constants.FEEDBACK_IMAGE_URL + data.getPhoto();
        try {
            Picasso.with(this)
                    .load(image)
                    .placeholder(android.R.color.transparent)
                    .error(android.R.color.transparent)
                    .into(ivHeaderImage);
        } catch (Exception e) {
        }


        feedbackDetailArrayList = db.getAllSQLiteFeedbackDetails(feedbackId);

        adapter = new FeedbackDetailAdapter(feedbackDetailArrayList, this,userId);
        rvFeedbackDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvFeedbackDetail.setLayoutManager(mLayoutManager);
        rvFeedbackDetail.setItemAnimator(new DefaultItemAnimator());
        rvFeedbackDetail.setAdapter(adapter);
        if (adapter.getItemCount() > 0)
            rvFeedbackDetail.scrollToPosition(adapter.getItemCount() - 1);


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("FEEDBACK_DETAIL")) {
                    handlePushNotification(intent);
                }
            }
        };

        if (refresh == 1) {
            adapter.notifyDataSetChanged();
            if (adapter.getItemCount() > 0)
                rvFeedbackDetail.scrollToPosition(adapter.getItemCount() - 1);
        }

    }

    @Override
    protected void onPause() {
        db.updateFeedbackDetailRead(feedbackId);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        feedbackDetailArrayList.clear();
        feedbackDetailArrayList = db.getAllSQLiteFeedbackDetails(feedbackId);

        adapter = new FeedbackDetailAdapter(feedbackDetailArrayList, this,userId);
        rvFeedbackDetail.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvFeedbackDetail.setLayoutManager(mLayoutManager);
        rvFeedbackDetail.setItemAnimator(new DefaultItemAnimator());
        rvFeedbackDetail.setAdapter(adapter);
        if (adapter.getItemCount() > 0)
            rvFeedbackDetail.scrollToPosition(adapter.getItemCount() - 1);

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("FEEDBACK_DETAIL"));

        db.updateFeedbackDetailRead(feedbackId);
    }

    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        Gson gson = new Gson();
        FeedbackDetail feedbackDetail = gson.fromJson(intent.getStringExtra("message"), FeedbackDetail.class);

        int fId = intent.getIntExtra("feedbackId", 0);
        int refreshId = intent.getIntExtra("refresh", 0);
        Log.e("Msg " + feedbackDetail, "Feedback id " + fId + "  Refresh : " + refreshId);
        if (feedbackDetail != null && fId != 0 && refreshId == 1) {

            if (feedbackId == fId) {
                feedbackDetailArrayList.add(feedbackDetail);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 1) {
                    rvFeedbackDetail.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llFeedbackDetail_Send) {

            String msg = edMessage.getText().toString();
            if (msg.isEmpty()) {
            } else {

                FeedbackDetail feedbackDetail = new FeedbackDetail(0, feedbackId, msg, 0, userId, userName, "", "2018-01-01", "00:00:00", 1);
                FeedbackDetail feedbackDetailDB = new FeedbackDetail(0, feedbackId, msg, 0, userId, userName, "", "2018-01-01", "00:00:00", 1);


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                String date = sdf.format(Calendar.getInstance().getTimeInMillis());
                String time = sdfTime.format(Calendar.getInstance().getTimeInMillis());

                Log.e("Feedback DETAIL", " LAST ID : " + db.getFeedbackDetailLastId());
                int lastId = db.getFeedbackDetailLastId() + 1;
                feedbackDetail.setTime(time);
                feedbackDetail.setDate(date);
                feedbackDetail.setFeedbackDetailId(lastId);

                db.addFeedbackDetails(feedbackDetail);
                edMessage.setText("");

                feedbackDetailArrayList.add(feedbackDetail);
                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 0)
                    rvFeedbackDetail.scrollToPosition(adapter.getItemCount() - 1);


                saveFeedbackDetail(feedbackDetailDB);

            }
        }
    }

    public void saveFeedbackDetail(final FeedbackDetail feedbackDetail) {
        if (Constants.isOnline(this)) {

            Call<Info> infoCall = Constants.myInterface.saveFeedbackDetail(feedbackDetail);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            if (data.getError()) {
                                Log.e("Feedback Detail : ", " ERROR : " + data.getMessage());
                            } else {
                                Log.e("Feedback Detail : ", " SUCCESS");
                                edMessage.setText("");

                            }
                        } else {
                            Log.e("Feedback Detail : ", " NULL");
                        }
                    } catch (Exception e) {
                        Log.e("Feedback Detail : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    Log.e("Feedback Detail : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }
}
