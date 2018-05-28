package com.ats.monginis_communication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.adapter.InboxMessageAdapter;
import com.ats.monginis_communication.adapter.MessageAdapter;
import com.ats.monginis_communication.bean.InboxMessage;
import com.ats.monginis_communication.bean.Message;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.google.gson.Gson;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {

    private RecyclerView rvMessage;
    InboxMessageAdapter adapter;

    DatabaseHandler db;

    ArrayList<InboxMessage> messageArray;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        rvMessage = findViewById(R.id.rvInboxList);

        db = new DatabaseHandler(this);

        messageArray = db.getAllInboxMessages();

        adapter = new InboxMessageAdapter(messageArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMessage.setLayoutManager(mLayoutManager);
        rvMessage.setItemAnimator(new DefaultItemAnimator());
        rvMessage.setAdapter(adapter);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("INBOX")) {
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

        db.updateInboxMessageRead();

    }

    @Override
    public void onPause() {
        Log.e("INBOX", "  ON PAUSE");
        db.updateInboxMessageRead();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Inbox", "  ON RESUME");

        db.updateInboxMessageRead();

        messageArray.clear();
        messageArray = db.getAllInboxMessages();

        adapter = new InboxMessageAdapter(messageArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvMessage.setLayoutManager(mLayoutManager);
        rvMessage.setItemAnimator(new DefaultItemAnimator());
        rvMessage.setAdapter(adapter);


        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("INBOX"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("REFRESH_DATA_FR"));

        db.updateInboxMessageRead();
    }

    private void handlePushNotification(Intent intent) {

        Log.e("handlePushNotification", "------------------------------------**********");
        Gson gson = new Gson();

        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("message");
        String date = intent.getStringExtra("date");

        Log.e("Inbox " + title, "------------------- ");
        if (title != null) {

            InboxMessage inboxMessage = new InboxMessage(title, msg, date, 1);

            messageArray.add(0, inboxMessage);
            adapter.notifyDataSetChanged();
        }
        db.updateInboxMessageRead();
    }

    private void handlePushNotification1(Intent intent) {
        Log.e("handlePushNotification1", "------------------------------------**********");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InboxActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("ProgressDialog", 0);
        startActivity(intent);
        finish();
    }
}
