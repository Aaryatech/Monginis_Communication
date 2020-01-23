package com.ats.monginis_communication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.adapter.DriverInfoAdapter;
import com.ats.monginis_communication.adapter.TrayReportAdapter;
import com.ats.monginis_communication.bean.DriverInfo;
import com.ats.monginis_communication.bean.TrayDetails;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.util.PermissionUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DriverInfoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);
        setTitle("Driver Info");

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

        recyclerView = findViewById(R.id.recyclerView);

        try {
            int frId = getIntent().getIntExtra("frId", 0);
            getDriverInfo(frId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getDriverInfo(int frId) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<DriverInfo>> infoCall = Constants.myInterface.getDriverInfoByFr(frId);
            infoCall.enqueue(new Callback<ArrayList<DriverInfo>>() {
                @Override
                public void onResponse(Call<ArrayList<DriverInfo>> call, Response<ArrayList<DriverInfo>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<DriverInfo> data = response.body();
                            commonDialog.dismiss();
                            Log.e("Driver Info : ", "Info Date---------------------------" + data);

                            adapter = new DriverInfoAdapter(data, DriverInfoActivity.this);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DriverInfoActivity.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);

                        } else {
                            commonDialog.dismiss();
                            Log.e("Driver Info : ", " NULL");
                            Toast.makeText(DriverInfoActivity.this, "No Data Available!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("Driver Info : ", " Exception : " + e.getMessage());
                        Toast.makeText(DriverInfoActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<DriverInfo>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("Driver Info : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(DriverInfoActivity.this, "Unable to process!", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
