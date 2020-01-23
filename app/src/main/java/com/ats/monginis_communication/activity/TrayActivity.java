package com.ats.monginis_communication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.ComplaintDetail;
import com.ats.monginis_communication.bean.Franchisee;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.TrayDetails;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.util.PermissionUtil;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrayActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDay1_Date, tvDay2_Date1, tvDay2_Date2, tvDay3_Date1, tvDay3_Date2, tvDay3_Date3,
            tvDay2_Small, tvDay2_Big, tvDay2_Large, tvDay2_XL,
            tvDay3_Small1, tvDay3_Big1, tvDay3_Large1, tvDay3_XL1, tvDay3_Small2, tvDay3_Big2, tvDay3_Large2, tvDay3_XL2,
            tvSubmit, tvDay1_Small, tvDay1_Big, tvDay1_Large, tvDay1_XL,
            tvDay1_Id, tvDay2_Id, tvDay3_Id;

    private EditText edDay2_Small, edDay2_Big, edDay2_Large, edDay2_XL,
            edDay3_Small, edDay3_Big, edDay3_Large, edDay3_XL;

    private LinearLayout llDay1, llDay2, llDay3;

    int frId;
    String todaysDate;

    ArrayList<TrayDetails> day1Array = new ArrayList<>();
    ArrayList<TrayDetails> day2Array = new ArrayList<>();
    ArrayList<TrayDetails> day3Array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray);
        setTitle("Tray");

        tvDay1_Date = findViewById(R.id.tvTray_Day1_Date);
        tvDay2_Date1 = findViewById(R.id.tvTray_Day2_Date1);
        tvDay2_Date2 = findViewById(R.id.tvTray_Day2_Date2);
        tvDay3_Date1 = findViewById(R.id.tvTray_Day3_Date1);
        tvDay3_Date2 = findViewById(R.id.tvTray_Day3_Date2);
        tvDay3_Date3 = findViewById(R.id.tvTray_Day3_Date3);
        tvDay2_Small = findViewById(R.id.tvTray_Day2_Small);
        tvDay2_Big = findViewById(R.id.tvTray_Day2_Big);
        tvDay2_Large = findViewById(R.id.tvTray_Day2_Large);
        tvDay2_XL = findViewById(R.id.tvTray_Day2_XL);
        tvDay3_Small1 = findViewById(R.id.tvTray_Day3_Small1);
        tvDay3_Big1 = findViewById(R.id.tvTray_Day3_Big1);
        tvDay3_Large1 = findViewById(R.id.tvTray_Day3_Large1);
        tvDay3_XL1 = findViewById(R.id.tvTray_Day3_XL1);
        tvDay3_Small2 = findViewById(R.id.tvTray_Day3_Small2);
        tvDay3_Big2 = findViewById(R.id.tvTray_Day3_Big2);
        tvDay3_Large2 = findViewById(R.id.tvTray_Day3_Large2);
        tvDay3_XL2 = findViewById(R.id.tvTray_Day3_XL2);
        tvSubmit = findViewById(R.id.tvTray_Submit);
        tvDay1_Small = findViewById(R.id.tvTray_Day1_Small);
        tvDay1_Big = findViewById(R.id.tvTray_Day1_Big);
        tvDay1_Large = findViewById(R.id.tvTray_Day1_Large);
        tvDay1_XL = findViewById(R.id.tvTray_Day1_XL);

        tvDay1_Id = findViewById(R.id.tvTray_Day1_Id);
        tvDay2_Id = findViewById(R.id.tvTray_Day2_Id);
        tvDay3_Id = findViewById(R.id.tvTray_Day3_Id);

        edDay2_Small = findViewById(R.id.edTray_Day2_Small);
        edDay2_Big = findViewById(R.id.edTray_Day2_Big);
        edDay2_Large = findViewById(R.id.edTray_Day2_Large);
        edDay2_XL = findViewById(R.id.edTray_Day2_XL);
        edDay3_Small = findViewById(R.id.edTray_Day3_Small);
        edDay3_Big = findViewById(R.id.edTray_Day3_Big);
        edDay3_Large = findViewById(R.id.edTray_Day3_Large);
        edDay3_XL = findViewById(R.id.edTray_Day3_XL);

        llDay1 = findViewById(R.id.llTray_Day1);
        llDay2 = findViewById(R.id.llTray_Day2);
        llDay3 = findViewById(R.id.llTray_Day3);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("franchise", "");
        Franchisee userBean = gson.fromJson(json2, Franchisee.class);
        Log.e("User Bean : ", "---------------" + userBean);

        try {
            if (userBean != null) {
                frId = userBean.getFrId();
                getServerDate();
                getTrayDetails(frId, 0);
            }
        } catch (Exception e) {
            frId = 0;
        }

        tvSubmit.setOnClickListener(this);

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

    }


    public void getTrayDetails(int frId, int isSameDay) {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<ArrayList<TrayDetails>> trayListCall = Constants.myInterface.getTrayDetailsByFranchise(frId, isSameDay);
            trayListCall.enqueue(new Callback<ArrayList<TrayDetails>>() {
                @Override
                public void onResponse(Call<ArrayList<TrayDetails>> call, Response<ArrayList<TrayDetails>> response) {
                    try {
                        if (response.body() != null) {
                            ArrayList<TrayDetails> data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY : ", "Tray Details---------------------------" + data);

                            day1Array.clear();
                            day2Array.clear();
                            day3Array.clear();

                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getTrayStatus() == 1) {
                                        day1Array.add(data.get(i));
                                    } else if (data.get(i).getTrayStatus() == 2) {
                                        day2Array.add(data.get(i));
                                    } else if (data.get(i).getTrayStatus() == 3) {
                                        day3Array.add(data.get(i));
                                    }
                                }
                            } else {
                                llDay1.setVisibility(View.GONE);
                                llDay2.setVisibility(View.GONE);
                                llDay3.setVisibility(View.GONE);
                                tvSubmit.setVisibility(View.GONE);
                            }

                            Log.e("Tray Data", " DAY 1 : " + day1Array);
                            Log.e("Tray Data", " DAY 2 : " + day2Array);
                            Log.e("Tray Data", " DAY 3 : " + day3Array);

                            //-------------DAY_1-----------------------------

                            if (day1Array.size() > 0) {
                                int pos = day1Array.size() - 1;

                                llDay1.setVisibility(View.VISIBLE);


                                tvDay1_Id.setText("" + day1Array.get(pos).getTranDetailId());

                                tvDay1_Small.setText("" + day1Array.get(pos).getOuttraySmall());
                                tvDay1_Big.setText("" + day1Array.get(pos).getOuttrayBig());
                                tvDay1_Large.setText("" + day1Array.get(pos).getOuttrayLead());
                                tvDay1_XL.setText("" + day1Array.get(pos).getOuttrayExtra());

                                tvDay1_Date.setText("" + day1Array.get(pos).getOuttrayDate());
                            } else {
                                tvDay1_Id.setText("0");
                            }

                            //-------------DAY_2-----------------------------

                            if (day2Array.size() > 0) {

                                int pos = day2Array.size() - 1;

                                if (todaysDate != null) {

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                    Log.e("InTray : ", "--------DATE----" + day2Array.get(pos).getIntrayDate());
                                    Date outTrayDate = sdf.parse(day2Array.get(pos).getOuttrayDate());
                                    Date currentDate = sdf.parse(todaysDate);

                                    Log.e("In Tray Date : ", "------" + outTrayDate);
                                    Log.e("Todays Date : ", "------" + todaysDate);
                                    Log.e("Date Match : ", "------------" + currentDate.compareTo(outTrayDate));

                                    if (currentDate.compareTo(outTrayDate) == 0) {
                                        tvSubmit.setVisibility(View.GONE);
                                    } else {
                                        tvSubmit.setVisibility(View.VISIBLE);
                                    }
                                }


                                llDay2.setVisibility(View.VISIBLE);

                                tvDay2_Id.setText("" + day2Array.get(pos).getTranDetailId());

                                tvDay2_Small.setText("" + day2Array.get(pos).getOuttraySmall());
                                tvDay2_Big.setText("" + day2Array.get(pos).getOuttrayBig());
                                tvDay2_Large.setText("" + day2Array.get(pos).getOuttrayLead());
                                tvDay2_XL.setText("" + day2Array.get(pos).getOuttrayExtra());

                                tvDay2_Date1.setText("" + day2Array.get(pos).getOuttrayDate());

                                tvDay2_Date2.setText("" + todaysDate);

                                try {
                                    int small = day2Array.get(pos).getOuttraySmall() - (day2Array.get(pos).getIntraySmall() + day2Array.get(pos).getIntraySmall1());
                                    int big = day2Array.get(pos).getOuttrayBig() - (day2Array.get(pos).getIntrayBig() + day2Array.get(pos).getIntrayBig1());
                                    int large = day2Array.get(pos).getOuttrayLead() - (day2Array.get(pos).getIntrayLead() + day2Array.get(pos).getIntrayLead1());
                                    int xl = day2Array.get(pos).getOuttrayExtra() - (day2Array.get(pos).getIntrayExtra() + day2Array.get(pos).getIntrayExtra1());

                                    edDay2_Small.setText("" + small);
                                    edDay2_Big.setText("" + big);
                                    edDay2_Large.setText("" + large);
                                    edDay2_XL.setText("" + xl);
                                } catch (Exception e) {
                                }
                            } else {
                                tvDay2_Id.setText("0");

                                edDay2_Small.setText("0");
                                edDay2_Big.setText("0");
                                edDay2_Large.setText("0");
                                edDay2_XL.setText("0");
                            }

                            //-------------DAY_3-----------------------------

                            if (day3Array.size() > 0) {
                                int pos = day3Array.size() - 1;

                                llDay3.setVisibility(View.VISIBLE);

                                tvDay3_Id.setText("" + day3Array.get(pos).getTranDetailId());

                                tvDay3_Small1.setText("" + day3Array.get(pos).getOuttraySmall());
                                tvDay3_Big1.setText("" + day3Array.get(pos).getOuttrayBig());
                                tvDay3_Large1.setText("" + day3Array.get(pos).getOuttrayLead());
                                tvDay3_XL1.setText("" + day3Array.get(pos).getOuttrayExtra());

                                tvDay3_Small2.setText("" + day3Array.get(pos).getIntraySmall());
                                tvDay3_Big2.setText("" + day3Array.get(pos).getIntrayBig());
                                tvDay3_Large2.setText("" + day3Array.get(pos).getIntrayLead());
                                tvDay3_XL2.setText("" + day3Array.get(pos).getIntrayExtra());


                                tvDay3_Date1.setText("" + day3Array.get(pos).getOuttrayDate());

                                tvDay3_Date2.setText("" + day3Array.get(pos).getIntrayDate());

                                tvDay3_Date3.setText("" + todaysDate);

                                try {
                                    int small = day3Array.get(pos).getOuttraySmall() - (day3Array.get(pos).getIntraySmall() + day3Array.get(pos).getIntraySmall1());
                                    int big = day3Array.get(pos).getOuttrayBig() - (day3Array.get(pos).getIntrayBig() + day3Array.get(pos).getIntrayBig1());
                                    int large = day3Array.get(pos).getOuttrayLead() - (day3Array.get(pos).getIntrayLead() + day3Array.get(pos).getIntrayLead1());
                                    int xl = day3Array.get(pos).getOuttrayExtra() - (day3Array.get(pos).getIntrayExtra() + day3Array.get(pos).getIntrayExtra1());

                                    edDay3_Small.setText("" + small);
                                    edDay3_Big.setText("" + big);
                                    edDay3_Large.setText("" + large);
                                    edDay3_XL.setText("" + xl);

                                } catch (Exception e) {
                                }

                            } else {
                                tvDay3_Id.setText("0");

                                edDay3_Small.setText("0");
                                edDay3_Big.setText("0");
                                edDay3_Large.setText("0");
                                edDay3_XL.setText("0");

                            }


                        } else {
                            tvSubmit.setVisibility(View.GONE);
                            commonDialog.dismiss();
                            Log.e("TRAY : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                        tvSubmit.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<TrayDetails>> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();
                    tvSubmit.setVisibility(View.GONE);

                }
            });
        } else {
            tvSubmit.setVisibility(View.GONE);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvTray_Submit) {
            try {

                int tray2Small = Integer.parseInt(tvDay2_Small.getText().toString());
                int tray2Big = Integer.parseInt(tvDay2_Big.getText().toString());
                int tray2Large = Integer.parseInt(tvDay2_Large.getText().toString());
                int tray2XL = Integer.parseInt(tvDay2_XL.getText().toString());

                int small1 = Integer.parseInt(tvDay3_Small1.getText().toString());
                int small2 = Integer.parseInt(tvDay3_Small2.getText().toString());

                int big1 = Integer.parseInt(tvDay3_Big1.getText().toString());
                int big2 = Integer.parseInt(tvDay3_Big2.getText().toString());

                int large1 = Integer.parseInt(tvDay3_Large1.getText().toString());
                int large2 = Integer.parseInt(tvDay3_Large2.getText().toString());

                int xl1 = Integer.parseInt(tvDay3_XL1.getText().toString());
                int xl2 = Integer.parseInt(tvDay3_XL2.getText().toString());

                int tray3Small = small1 - small2;
                int tray3Big = big1 - big2;
                int tray3Large = large1 - large2;
                int tray3XL = xl1 - xl2;

//                int tray3Small = (Integer.parseInt(tvDay3_Small1.getText().toString())) - (Integer.parseInt(tvDay3_Small2.getText().toString()));
//                int tray3Big = (Integer.parseInt(tvDay3_Big1.getText().toString())) - (Integer.parseInt(tvDay3_Big2.getText().toString()));
//                int tray3Large = (Integer.parseInt(tvDay3_Large1.getText().toString())) - (Integer.parseInt(tvDay3_Big2.getText().toString()));
//                int tray3XL = (Integer.parseInt(tvDay3_XL1.getText().toString())) - (Integer.parseInt(tvDay3_Big2.getText().toString()));

                Log.e("tray2Small : ", "------------" + tray2Small);
                Log.e("tray2Big : ", "------------" + tray2Big);
                Log.e("tray2Large : ", "------------" + tray2Large);
                Log.e("tray2XL : ", "------------" + tray2XL);

                Log.e("tray3Small : ", "------------(" + tvDay3_Small1.getText().toString() + "-" + tvDay3_Small2.getText().toString() + ")=" + tray3Small);
                Log.e("tray3Big : ", "------------(" + tvDay3_Big1.getText().toString() + "-" + tvDay3_Big2.getText().toString() + ")=" + tray3Big);
                Log.e("tray3Large : ", "------------(" + tvDay3_Large1.getText().toString() + "-" + tvDay3_Large2.getText().toString() + ")=" + tray3Large);
                Log.e("tray3XL : ", "------------(" + tvDay3_XL1.getText().toString() + "-" + tvDay3_XL2.getText().toString() + ")=" + tray3XL);

                //-------------------------PARAMETERS----------------------------
                int header1 = Integer.parseInt(tvDay1_Id.getText().toString());
                int header2 = Integer.parseInt(tvDay2_Id.getText().toString());
                int header3 = Integer.parseInt(tvDay3_Id.getText().toString());

                int inTray_Small = Integer.parseInt(edDay2_Small.getText().toString());
                int inTray_Big = Integer.parseInt(edDay2_Big.getText().toString());
                int inTray_Large = Integer.parseInt(edDay2_Large.getText().toString());
                int inTray_XL = Integer.parseInt(edDay2_XL.getText().toString());

                int inTray_Small1 = Integer.parseInt(edDay3_Small.getText().toString());
                int inTray_Big1 = Integer.parseInt(edDay3_Big.getText().toString());
                int inTray_Large1 = Integer.parseInt(edDay3_Large.getText().toString());
                int inTray_XL1 = Integer.parseInt(edDay3_XL.getText().toString());

                Log.e("inTray_Small : ", "------------" + inTray_Small);
                Log.e("inTray_Big : ", "------------" + inTray_Big);
                Log.e("inTray_Large : ", "------------" + inTray_Large);
                Log.e("inTray_XL : ", "------------" + inTray_XL);

                Log.e("inTray_Small1 : ", "------------" + inTray_Small1);
                Log.e("inTray_Big1 : ", "------------" + inTray_Big1);
                Log.e("inTray_Large1 : ", "------------" + inTray_Large1);
                Log.e("inTray_XL1 : ", "------------" + inTray_XL1);


                if (inTray_Small > tray2Small) {
                    edDay2_Small.setError("Error");
                    edDay2_Small.requestFocus();
                } else if (inTray_Big > tray2Big) {
                    edDay2_Big.setError("Error");
                    edDay2_Big.requestFocus();

                    edDay2_Small.setError(null);

                } else if (inTray_Large > tray2Large) {
                    edDay2_Large.setError("Error");
                    edDay2_Large.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);

                } else if (inTray_XL > tray2XL) {
                    edDay2_XL.setError("Error");
                    edDay2_XL.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);

                } else if (inTray_Small1 > tray3Small) {
                    edDay3_Small.setError("Error");
                    edDay3_Small.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);
                    edDay2_XL.setError(null);

                } else if (inTray_Big1 > tray3Big) {
                    edDay3_Big.setError("Error");
                    edDay3_Big.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);
                    edDay2_XL.setError(null);
                    edDay3_Small.setError(null);

                } else if (inTray_Large1 > tray3Large) {
                    edDay3_Large.setError("Error");
                    edDay3_Large.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);
                    edDay2_XL.setError(null);
                    edDay3_Small.setError(null);
                    edDay3_Big.setError(null);

                } else if (inTray_XL1 > tray3XL) {
                    edDay3_XL.setError("Error");
                    edDay3_XL.requestFocus();

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);
                    edDay2_XL.setError(null);
                    edDay3_Small.setError(null);
                    edDay3_Big.setError(null);
                    edDay3_Large.setError(null);

                } else {

                    edDay2_Small.setError(null);
                    edDay2_Big.setError(null);
                    edDay2_Large.setError(null);
                    edDay2_XL.setError(null);
                    edDay3_Small.setError(null);
                    edDay3_Big.setError(null);
                    edDay3_Large.setError(null);
                    edDay3_XL.setError(null);

                    insertTrayIn(header1, header2, header3, inTray_Small, inTray_Big, inTray_Large, inTray_XL, inTray_Small1, inTray_Big1, inTray_Large1, inTray_XL1);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void insertTrayIn(int header1, int header2, int header3, int small, int big, int large, int xl, int small1, int big1, int large1, int xl1) {

        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            final Call<Info> infoCall = Constants.myInterface.saveInTray(header1, header2, big, small, large, xl, header3, big1, small1, large1, xl1);
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info info = response.body();
                            if (info.getError()) {
                                commonDialog.dismiss();
                                Toast.makeText(TrayActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                                Log.e("Tray : Submit", "   ERROR---" + info.getMessage());
                            } else {

                                commonDialog.dismiss();
                                Toast.makeText(TrayActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                llDay1.setVisibility(View.GONE);
                                llDay2.setVisibility(View.GONE);
                                llDay3.setVisibility(View.GONE);

                                getServerDate();
                                getTrayDetails(frId, 0);

                            }
                        } else {
                            commonDialog.dismiss();
                            Toast.makeText(TrayActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            Log.e("Tray : Submit", "   NULL---");

                        }

                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Toast.makeText(TrayActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        Log.e("Tray : Submit", "   Exception---" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Toast.makeText(TrayActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    Log.e("Tray : Submit", "   ONFailure---" + t.getMessage());
                    t.printStackTrace();
                }
            });


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void getServerDate() {
        if (Constants.isOnline(this)) {
            final CommonDialog commonDialog = new CommonDialog(this, "Loading", "Please Wait...");
            commonDialog.show();

            Call<Info> infoCall = Constants.myInterface.getServerDate();
            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            commonDialog.dismiss();
                            Log.e("TRAY : ", "Info Date---------------------------" + data);

                            todaysDate = data.getMessage();
                        } else {
                            commonDialog.dismiss();
                            Log.e("TRAY : ", " NULL");
                        }
                    } catch (Exception e) {
                        commonDialog.dismiss();
                        Log.e("TRAY : ", " Exception : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    commonDialog.dismiss();
                    Log.e("TRAY : ", " onFailure : " + t.getMessage());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_tray_report);
        item.setVisible(true);
        MenuItem itemDriver = menu.findItem(R.id.menu_driver);
        itemDriver.setVisible(true);
        MenuItem itemProfile = menu.findItem(R.id.menu_profile);
        itemProfile.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_tray_report) {
            startActivity(new Intent(TrayActivity.this, TrayReportsActivity.class));
        }else  if (id == R.id.menu_driver) {
            Intent intent=new Intent(TrayActivity.this,DriverInfoActivity.class);
            intent.putExtra("frId",frId);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
