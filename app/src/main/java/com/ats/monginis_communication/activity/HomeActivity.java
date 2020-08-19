package com.ats.monginis_communication.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.ComplaintData;
import com.ats.monginis_communication.bean.ComplaintDetail;
import com.ats.monginis_communication.bean.FeedbackData;
import com.ats.monginis_communication.bean.FeedbackDetail;
import com.ats.monginis_communication.bean.Franchisee;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.LoginData;
import com.ats.monginis_communication.bean.Message;
import com.ats.monginis_communication.bean.MessageData;
import com.ats.monginis_communication.bean.NoticeData;
import com.ats.monginis_communication.bean.NotificationData;
import com.ats.monginis_communication.bean.SchedulerList;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.bean.SuggestionDetail;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.fcm.SharedPrefManager;
import com.ats.monginis_communication.util.PermissionUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llNotices, llMessages, llNotifications, llSuggestion, llComplaint, llFeedback, llInbox;
    private TextView tvNoticesCount, tvMessagesCount, tvNotificationsCount, tvSuggestionCount, tvComplaintCount, tvFeedbackCount, tvNotificationIndicator, tvSuggestionIndicator, tvComplaintIndicator, tvFeedbackIndicator, tvInboxCount;

    int userId, accessRight;
    String userName;

    DatabaseHandler db;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "COMM_APP");
    File f;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        llInbox = findViewById(R.id.llHome_Inbox);
        llNotices = findViewById(R.id.llHome_Notices);
        llMessages = findViewById(R.id.llHome_Messages);
        llNotifications = findViewById(R.id.llHome_Notifications);
        llSuggestion = findViewById(R.id.llHome_Suggestion);
        llComplaint = findViewById(R.id.llHome_Complaint);
        llFeedback = findViewById(R.id.llHome_Feedback);


        tvInboxCount = findViewById(R.id.tvHome_InboxCount);
        tvNoticesCount = findViewById(R.id.tvHome_NoticeCount);
        tvMessagesCount = findViewById(R.id.tvHome_MessagesCount);
        tvNotificationsCount = findViewById(R.id.tvHome_NotificationCount);
        tvSuggestionCount = findViewById(R.id.tvHome_SuggestionCount);
        tvComplaintCount = findViewById(R.id.tvHome_ComplaintCount);
        tvFeedbackCount = findViewById(R.id.tvHome_FeedbackCount);
        tvNotificationIndicator = findViewById(R.id.tvHome_NotificationIndicator);
        tvSuggestionIndicator = findViewById(R.id.tvHome_SuggestionIndicator);
        tvComplaintIndicator = findViewById(R.id.tvHome_ComplaintIndicator);
        tvFeedbackIndicator = findViewById(R.id.tvHome_FeedbackIndicator);

        llInbox.setOnClickListener(this);
        llNotices.setOnClickListener(this);
        llMessages.setOnClickListener(this);
        llNotifications.setOnClickListener(this);
        llSuggestion.setOnClickListener(this);
        llComplaint.setOnClickListener(this);
        llFeedback.setOnClickListener(this);

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

        createFolder();

        db = new DatabaseHandler(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("franchise", "");
        Franchisee userBean = gson.fromJson(json2, Franchisee.class);
        Log.e("User Bean : ", "---------------" + userBean);
        accessRight = pref.getInt("accessRight", 0);
        try {
            if (userBean != null) {
                userId = userBean.getFrId();
                userName = userBean.getFrName();

                Intent intent = getIntent();
                int progressDialogStatus = intent.getIntExtra("ProgressDialog", 1);
                Log.e("progressDialog", "----------------VALUE-------------------" + progressDialogStatus);
                try {
                    if (progressDialogStatus == 1) {

                        Log.e("progressDialog", "-----------------------------------1");

                        getAllMessages(1);
                        getAllNotices(1);
                        getAllNotificationsById(db.getNotificationLastId(), 1);
                        getAllSuggestion(userId, db.getSuggestionLastId(), 1);
                        getAllSuggestionDetails(userId, db.getSuggestionDetailLastId(), 1);
                        getAllComplaint(userId, db.getComplaintLastId(), 1);
                        getAllComplaintDetails(userId, db.getComplaintDetailLastId(), 1);
                        getAllFeedback(db.getFeedbackLastId(), 1);
                        //Log.e("Feedback DEtail", "  lAst ID ---------------------------: " + db.getFeedbackDetailLastId());
                        getAllFeedbackDetails(userId, db.getFeedbackDetailLastId(), 1);
                    } else {
                        Log.e("progressDialog", "-----------------------------------0");
                    }
                } catch (Exception e) {
                    Log.e("Exception :", "-------------progressDialog--------------------------" + e.getMessage());

                    getAllMessages(1);
                    getAllNotices(1);
                    getAllNotificationsById(db.getNotificationLastId(), 1);
                    getAllSuggestion(userId, db.getSuggestionLastId(), 1);
                    getAllSuggestionDetails(userId, db.getSuggestionDetailLastId(), 1);
                    getAllComplaint(userId, db.getComplaintLastId(), 1);
                    getAllComplaintDetails(userId, db.getComplaintDetailLastId(), 1);
                    getAllFeedback(db.getFeedbackLastId(), 1);
                    //Log.e("Feedback DEtail", "  lAst ID ---------------------------: " + db.getFeedbackDetailLastId());
                    getAllFeedbackDetails(userId, db.getFeedbackDetailLastId(), 1);

                }

            } else {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("REFRESH_DATA_FR")) {
                    handlePushNotification(intent);
                }
            }
        };

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    private void handlePushNotification(Intent intent) {

        //Log.e("handlePushNotification", "-----------HOME-------------------------**********");
//        tvMessagesCount.setText("" + db.getMessageUnreadCount());
//        tvNoticesCount.setText("" + db.getNoticeUnreadCount());

        if (db.getInboxMessageUnreadCount() > 0) {
            tvInboxCount.setText("" + db.getInboxMessageUnreadCount());
        } else {
            tvInboxCount.setText("");
        }

        if (db.getNotificationUnreadCount() > 0) {
            tvNotificationsCount.setText("" + db.getNotificationUnreadCount());
        } else {
            tvNotificationsCount.setText("");
        }

        if (db.getSuggestionUnreadCount() > 0) {
            tvSuggestionCount.setText("" + db.getSuggestionUnreadCount());
        } else {
            tvSuggestionCount.setText("");
        }

        if (db.getComplaintUnreadCount() > 0) {
            tvComplaintCount.setText("" + db.getComplaintUnreadCount());
        } else {
            tvComplaintCount.setText("");
        }

        if (db.getFeedbackUnreadCount() > 0) {
            tvFeedbackCount.setText("" + db.getFeedbackUnreadCount());
        } else {
            tvFeedbackCount.setText("");
        }

        if (db.getSuggestionDetailUnreadCount() > 0) {
            tvSuggestionIndicator.setVisibility(View.VISIBLE);
        } else {
            tvSuggestionIndicator.setVisibility(View.GONE);
        }

        if (db.getComplaintDetailUnreadCount() > 0) {
            tvComplaintIndicator.setVisibility(View.VISIBLE);
        } else {
            tvComplaintIndicator.setVisibility(View.GONE);
        }

        if (db.getFeedbackDetailUnreadCount() > 0) {
            tvFeedbackIndicator.setVisibility(View.VISIBLE);
        } else {
            tvFeedbackIndicator.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llHome_Inbox) {
            Intent intent = new Intent(this, InboxActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Notices) {
            Intent intent = new Intent(this, TabActivity.class);
            intent.putExtra("tabId", 0);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Messages) {
            Intent intent = new Intent(this, TabActivity.class);
            intent.putExtra("tabId", 1);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Notifications) {
            Intent intent = new Intent(this, TabActivity.class);
            intent.putExtra("tabId", 2);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Suggestion) {
            Intent intent = new Intent(this, InnerTabActivity.class);
            intent.putExtra("tabId", 0);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Complaint) {
            Intent intent = new Intent(this, InnerTabActivity.class);
            intent.putExtra("tabId", 1);
            startActivity(intent);
        } else if (view.getId() == R.id.llHome_Feedback) {
            Intent intent = new Intent(this, InnerTabActivity.class);
            intent.putExtra("tabId", 2);
            startActivity(intent);
        }
    }


    public void getAllMessages(int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }
            Call<MessageData> messageDataCall = Constants.myInterface.getAllMessages();
            messageDataCall.enqueue(new Callback<MessageData>() {
                @Override
                public void onResponse(Call<MessageData> call, Response<MessageData> response) {
                    try {
                        if (response.body() != null) {
                            MessageData data = response.body();
                            if (data.getInfo().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "" + data.getInfo().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
                                db.removeAllMessages();

                                for (int i = 0; i < data.getMessage().size(); i++) {
//                                    Message messageBean = db.getMessage(data.getMessage().get(i).getMsgId());
//                                    if (messageBean.getMsgId() <= 0) {
//                                        db.addMessage(data.getMessage().get(i));
//                                    }
                                        db.addMessage(data.getMessage().get(i));
                                }
                            }
                        } else {
                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<MessageData> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllNotices(int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }
            Call<NoticeData> noticeDataCall = Constants.myInterface.getAllNotices();
            noticeDataCall.enqueue(new Callback<NoticeData>() {
                @Override
                public void onResponse(Call<NoticeData> call, Response<NoticeData> response) {
                    try {
                        if (response.body() != null) {
                            NoticeData data = response.body();
                            if (data.getInfo().getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "" + data.getInfo().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                commonDialog.dismiss();
//                                for (int i = 0; i < data.getSchedulerList().size(); i++) {
//                                    SchedulerList noticeBean = db.getNotice(data.getSchedulerList().get(i).getSchId());
//                                    if (noticeBean.getSchId() <= 0) {
//                                        db.addNotices(data.getSchedulerList().get(i));
//                                    }
//                                }
                                db.removeAllNotices();
                                for (int i = 0; i < data.getSchedulerList().size(); i++) {


                                }
                            }
                        } else {
                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NoticeData> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllNotificationsById(int id, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<NotificationData>> notificationDataCall = Constants.myInterface.getAllNotificationById(id);
            notificationDataCall.enqueue(new Callback<ArrayList<NotificationData>>() {
                @Override
                public void onResponse(Call<ArrayList<NotificationData>> call, Response<ArrayList<NotificationData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<NotificationData> data = response.body();

                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Notification---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                // NotificationData bean = db.getNotification(data.get(i).getNotificationId());
                                // if (bean.getNotificationId() <= 0) {
                                db.addNotifications(data.get(i));
                                // }
                            }

                            if (db.getNotificationUnreadCount() > 0) {
                                tvNotificationsCount.setText("" + db.getNotificationUnreadCount());
                            } else {
                                tvNotificationsCount.setText("");
                            }

                        } else {
                            commonDialog.dismiss();
                            //Log.e("Notification : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("Notification : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<NotificationData>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("Notification : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllSuggestion(int frId, int sugId, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<SuggestionData>> suggestionDataCall = Constants.myInterface.getAllSuggestion(frId, sugId);
            suggestionDataCall.enqueue(new Callback<ArrayList<SuggestionData>>() {
                @Override
                public void onResponse(Call<ArrayList<SuggestionData>> call, Response<ArrayList<SuggestionData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<SuggestionData> data = response.body();
                            commonDialog.dismiss();
                            for (int i = 0; i < data.size(); i++) {
                                db.addSuggestion(data.get(i));
                            }

                            if (db.getSuggestionUnreadCount() > 0) {
                                tvSuggestionCount.setText("" + db.getSuggestionUnreadCount());
                            } else {
                                tvSuggestionCount.setText("");
                            }

                        } else {
                            commonDialog.dismiss();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SuggestionData>> call, Throwable t) {
                    commonDialog.dismiss();
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllSuggestionDetails(int frId, int sdId, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<SuggestionDetail>> suggestionDataCall = Constants.myInterface.getAllSuggestionDetails(frId, sdId);
            suggestionDataCall.enqueue(new Callback<ArrayList<SuggestionDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<SuggestionDetail>> call, Response<ArrayList<SuggestionDetail>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<SuggestionDetail> data = response.body();
                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Suggestion Details---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                db.addSuggestionDetails(data.get(i));
                            }

                            if (db.getSuggestionDetailUnreadCount() > 0) {
                                tvSuggestionIndicator.setVisibility(View.VISIBLE);
                            } else {
                                tvSuggestionIndicator.setVisibility(View.GONE);
                            }


                        } else {
                            commonDialog.dismiss();
                            // //Log.e("Suggestion : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        // //Log.e("Suggestion : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SuggestionDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    // //Log.e("Suggestion : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllComplaint(int frId, int cId, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<ComplaintData>> complaintDataCall = Constants.myInterface.getAllComplaint(frId, cId);
            complaintDataCall.enqueue(new Callback<ArrayList<ComplaintData>>() {
                @Override
                public void onResponse(Call<ArrayList<ComplaintData>> call, Response<ArrayList<ComplaintData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<ComplaintData> data = response.body();
                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Complaint---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                db.addComplaint(data.get(i));
                            }

                            if (db.getComplaintUnreadCount() > 0) {
                                tvComplaintCount.setText("" + db.getComplaintUnreadCount());
                            } else {
                                tvComplaintCount.setText("");
                            }

                        } else {
                            commonDialog.dismiss();
                            // //Log.e("Suggestion : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        // //Log.e("Suggestion : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ComplaintData>> call, Throwable t) {
                    commonDialog.dismiss();
                    // //Log.e("Suggestion : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllComplaintDetails(int frId, int cdId, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<ComplaintDetail>> complaintDataCall = Constants.myInterface.getAllComplaintDetail(frId, cdId);
            complaintDataCall.enqueue(new Callback<ArrayList<ComplaintDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<ComplaintDetail>> call, Response<ArrayList<ComplaintDetail>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<ComplaintDetail> data = response.body();
                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Complaint Details---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                db.addComplaintDetails(data.get(i));
                            }


                            if (db.getComplaintDetailUnreadCount() > 0) {
                                tvComplaintIndicator.setVisibility(View.VISIBLE);
                            } else {
                                tvComplaintIndicator.setVisibility(View.GONE);
                            }


                        } else {
                            commonDialog.dismiss();
                            // Log.e("Suggestion : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        // Log.e("Suggestion : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ComplaintDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    // Log.e("Suggestion : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllFeedback(int fId, int flag) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<FeedbackData>> feedbackDataCall = Constants.myInterface.getAllFeedback(fId);
            feedbackDataCall.enqueue(new Callback<ArrayList<FeedbackData>>() {
                @Override
                public void onResponse(Call<ArrayList<FeedbackData>> call, Response<ArrayList<FeedbackData>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<FeedbackData> data = response.body();
                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Feedback---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                db.addFeedBack(data.get(i));
                            }

                            if (db.getFeedbackUnreadCount() > 0) {
                                tvFeedbackCount.setText("" + db.getFeedbackUnreadCount());
                            } else {
                                tvFeedbackCount.setText("");
                            }

                        } else {
                            commonDialog.dismiss();
                            // Log.e("Suggestion : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        // Log.e("Suggestion : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<FeedbackData>> call, Throwable t) {
                    commonDialog.dismiss();
                    // Log.e("Suggestion : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    public void getAllFeedbackDetails(int frId, int fdId, int flag) {
        //Log.e("PARAMETERS", "--------------------FRID : " + frId + "-----------fdID : " + fdId);
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            if (flag == 1) {
                commonDialog.show();
            }

            Call<ArrayList<FeedbackDetail>> feedbackDataCall = Constants.myInterface.getAllFeedbackDetail(fdId);
            feedbackDataCall.enqueue(new Callback<ArrayList<FeedbackDetail>>() {
                @Override
                public void onResponse(Call<ArrayList<FeedbackDetail>> call, Response<ArrayList<FeedbackDetail>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<FeedbackDetail> data = response.body();
                            //Log.e("FEED_DETAILS", "*-*----*-*-*-*-*-*-**" + data);
                            commonDialog.dismiss();
                            //Log.e("HOME : ", "INSERTING Feedback Details---------------------------");

                            for (int i = 0; i < data.size(); i++) {
                                db.addFeedbackDetails(data.get(i));
                            }


                            if (db.getFeedbackDetailUnreadCount() > 0) {
                                tvFeedbackIndicator.setVisibility(View.VISIBLE);
                            } else {
                                tvFeedbackIndicator.setVisibility(View.GONE);
                            }

                        } else {
                            commonDialog.dismiss();
                            //Log.e("FEED_DETAILS : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        //Log.e("FEED_DETAILS : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<FeedbackDetail>> call, Throwable t) {
                    commonDialog.dismiss();
                    //Log.e("FEED_DETAILS : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_tray);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            userInfoDialog();
        } else if (id == R.id.menu_tray) {
            //startActivity(new Intent(HomeActivity.this, TrayActivity.class));
            startActivity(new Intent(HomeActivity.this, FrRouteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    public void userInfoDialog() {

        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.custom_user_info_dialog_layout);
        openDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wlp.x = 100;
        wlp.y = 100;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        TextView tvShop = openDialog.findViewById(R.id.tvUserDialog_Shop);
        TextView tvName = openDialog.findViewById(R.id.tvUserDialog_Owner);
        TextView tvMobile = openDialog.findViewById(R.id.tvUserDialog_Mobile);
        TextView tvLogout = openDialog.findViewById(R.id.tvUserDialog_Logout);
        TextView tvSync = openDialog.findViewById(R.id.tvUserDialog_Sync);
        ImageView ivImage = openDialog.findViewById(R.id.ivUserDialog_Image);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json2 = pref.getString("franchise", "");
        Franchisee userBean = gson.fromJson(json2, Franchisee.class);
        //Log.e("User Bean : ", "---------------" + userBean);
        try {
            if (userBean != null) {

                tvShop.setText("" + userBean.getFrName());
                tvName.setText("" + userBean.getFrOwner());
                tvMobile.setText("" + userBean.getFrMob());


                String image = Constants.FR_IMAGE_URL + userBean.getFrImage();
                try {
                    Picasso.with(this)
                            .load(image)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.logo)
                            .into(ivImage);
                } catch (Exception e) {
                }

                tvSync.setPaintFlags(tvLogout.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tvLogout.setPaintFlags(tvLogout.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                tvLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                        builder.setTitle("Logout");
                        builder.setMessage("Are You Sure You Want To Logout?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                updateUserToken(userId, "");

                                editor.clear();
                                editor.commit();

                                db.removeAll();

                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);


                                finish();
                            }
                        });
                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });

                tvSync.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.removeAll();

                        getAllMessages(1);
                        getAllNotices(1);
                        getAllNotificationsById(db.getNotificationLastId(), 1);
                        getAllSuggestion(userId, db.getSuggestionLastId(), 1);
                        getAllSuggestionDetails(userId, db.getSuggestionDetailLastId(), 1);
                        getAllComplaint(userId, db.getComplaintLastId(), 1);
                        getAllComplaintDetails(userId, db.getComplaintDetailLastId(), 1);
                        getAllFeedback(db.getFeedbackLastId(), 1);
                        //Log.e("Feedback DEtail", "  lAst ID ---------------------------: " + db.getFeedbackDetailLastId());
                        getAllFeedbackDetails(userId, db.getFeedbackDetailLastId(), 1);

                        if (accessRight == 1) {
                            String token = SharedPrefManager.getmInstance(HomeActivity.this).getDeviceToken();
                            Log.e("Token : ", "---------" + token);
                            updateUserToken(userId, token);
                        }

                        openDialog.dismiss();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        openDialog.show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Home", "---------------------ON RESUME");

        if (userId > 0) {

            getAllMessages(0);
            getAllNotices(0);
            getAllNotificationsById(db.getNotificationLastId(), 0);
            getAllSuggestion(userId, db.getSuggestionLastId(), 0);
            getAllSuggestionDetails(userId, db.getSuggestionDetailLastId(), 0);
            getAllComplaint(userId, db.getComplaintLastId(), 0);
            getAllComplaintDetails(userId, db.getComplaintDetailLastId(), 0);
            getAllFeedback(db.getFeedbackLastId(), 0);
            //Log.e("Feedback DEtail", "  lAst ID ---------------------------: " + db.getFeedbackDetailLastId());
            getAllFeedbackDetails(userId, db.getFeedbackDetailLastId(), 0);

//            tvMessagesCount.setText("" + db.getMessageUnreadCount());
//            tvNoticesCount.setText("" + db.getNoticeUnreadCount());

            if (db.getInboxMessageUnreadCount() > 0) {
                tvInboxCount.setText("" + db.getInboxMessageUnreadCount());
            } else {
                tvInboxCount.setText("");
            }

            if (db.getNotificationUnreadCount() > 0) {
                tvNotificationsCount.setText("" + db.getNotificationUnreadCount());
            } else {
                tvNotificationsCount.setText("");
            }

            if (db.getSuggestionUnreadCount() > 0) {
                tvSuggestionCount.setText("" + db.getSuggestionUnreadCount());
            } else {
                tvSuggestionCount.setText("");
            }

            if (db.getComplaintUnreadCount() > 0) {
                tvComplaintCount.setText("" + db.getComplaintUnreadCount());
            } else {
                tvComplaintCount.setText("");
            }

            if (db.getFeedbackUnreadCount() > 0) {
                tvFeedbackCount.setText("" + db.getFeedbackUnreadCount());
            } else {
                tvFeedbackCount.setText("");
            }

            if (db.getSuggestionDetailUnreadCount() > 0) {
                tvSuggestionIndicator.setVisibility(View.VISIBLE);
            } else {
                tvSuggestionIndicator.setVisibility(View.GONE);
            }

            if (db.getComplaintDetailUnreadCount() > 0) {
                tvComplaintIndicator.setVisibility(View.VISIBLE);
            } else {
                tvComplaintIndicator.setVisibility(View.GONE);
            }

            if (db.getFeedbackDetailUnreadCount() > 0) {
                tvFeedbackIndicator.setVisibility(View.VISIBLE);
            } else {
                tvFeedbackIndicator.setVisibility(View.GONE);
            }

            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter("REFRESH_DATA_FR"));

        }

    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
        builder.setTitle("Exit Application?");
        builder.setMessage("");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void updateUserToken(int userId, String token) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(HomeActivity.this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.updateFCMToken(0, userId, token);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    Log.e("Response : ", "--------------------" + response.body());
                    commonDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Failure : ", "---------------------" + t.getMessage());
                    t.printStackTrace();

                }
            });

        }

    }
}
