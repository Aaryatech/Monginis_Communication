package com.ats.monginis_communication.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.adapter.FeedbackAdapter;
import com.ats.monginis_communication.adapter.NewTrayReportAdapter;
import com.ats.monginis_communication.adapter.TrayReportAdapter;
import com.ats.monginis_communication.bean.Franchisee;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.NewTrayReport;
import com.ats.monginis_communication.bean.TrayDetails;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrayReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvReportList;
    TrayReportAdapter adapter;
    private EditText edFromDate, edToDate;
    private ImageView ivSearch;

    int frId, yyyy, mm, dd;
    long fromMillis, toMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray_reports);
        setTitle("Tray Report");

        rvReportList = findViewById(R.id.rvTrayReportsList);
        edFromDate = findViewById(R.id.edTrayReport_FromDate);
        edToDate = findViewById(R.id.edTrayReport_ToDate);
        ivSearch = findViewById(R.id.ivTrayReport_Search);


        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("franchise", "");
        Franchisee userBean = gson.fromJson(json2, Franchisee.class);
        Log.e("User Bean : ", "---------------" + userBean);
        try {
            if (userBean != null) {
                frId = userBean.getFrId();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String todaysDate = sdf.format(Calendar.getInstance().getTimeInMillis());
                edFromDate.setText(todaysDate);
                edToDate.setText(todaysDate);

                //getTrayReport(todaysDate, todaysDate, frId);
                getTrayReportNew(todaysDate, todaysDate, frId);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivSearch.setOnClickListener(this);
        edFromDate.setOnClickListener(this);
        edToDate.setOnClickListener(this);
    }

    public void getTrayReport(String fromDate, String toDate, int frId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayDetails>> infoCall = Constants.myInterface.getTrayDetailReport(fromDate, toDate, frId, 2);
            infoCall.enqueue(new Callback<ArrayList<TrayDetails>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayDetails>> call, Response<ArrayList<TrayDetails>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayDetails> data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", "Info Date---------------------------" + data);

                            adapter = new TrayReportAdapter(data);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayReportsActivity.this);
                            rvReportList.setLayoutManager(mLayoutManager);
                            rvReportList.setItemAnimator(new DefaultItemAnimator());
                            rvReportList.setAdapter(adapter);

                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY Report : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayDetails>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY Report : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }



    public void getTrayReportNew(String fromDate, String toDate, int frId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<NewTrayReport>> infoCall = Constants.myInterface.getFrWiseTrayReportNew(fromDate, toDate, frId, 2);
            infoCall.enqueue(new Callback<ArrayList<NewTrayReport>>() {
                @Override
                public void onResponse(Call<ArrayList<NewTrayReport>> call, Response<ArrayList<NewTrayReport>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<NewTrayReport> data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", "Info Date---------------------------" + data);

                            NewTrayReportAdapter adapterNew = new NewTrayReportAdapter(data,TrayReportsActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TrayReportsActivity.this);
                            rvReportList.setLayoutManager(mLayoutManager);
                            rvReportList.setItemAnimator(new DefaultItemAnimator());
                            rvReportList.setAdapter(adapterNew);

                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY Report : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY Report : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<NewTrayReport>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY Report : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edTrayReport_FromDate) {
            int yr, mn, dy;
            if (fromMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(fromMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(this, fromDateListener, yr, mn, dy);
            dialog.show();
        } else if (view.getId() == R.id.edTrayReport_ToDate) {
            int yr, mn, dy;
            if (toMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(toMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog = new DatePickerDialog(this, toDateListener, yr, mn, dy);
            dialog.show();
        } else if (view.getId() == R.id.ivTrayReport_Search) {
            String fromDt = edFromDate.getText().toString();
            String toDt = edToDate.getText().toString();

            //getTrayReport(fromDt, toDt, frId);
            getTrayReportNew(fromDt, toDt, frId);

        }
    }


    DatePickerDialog.OnDateSetListener fromDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edFromDate.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromMillis = calendar.getTimeInMillis();
        }
    };

    DatePickerDialog.OnDateSetListener toDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edToDate.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toMillis = calendar.getTimeInMillis();
        }
    };

}
