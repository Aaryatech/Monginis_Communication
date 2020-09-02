package com.ats.monginis_communication.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.TrayActivity;
import com.ats.monginis_communication.bean.RouteWiseData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FrRouteAdapter extends RecyclerView.Adapter<FrRouteAdapter.MyViewHolder> {

    private ArrayList<RouteWiseData> routeList;
    private Context context;

    public FrRouteAdapter(ArrayList<RouteWiseData> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvVeh, tvRoute, tvTrayStatus, tvMobile1, tvMobile2, tvMobile3;

        public MyViewHolder(View view) {
            super(view);
            tvDate = view.findViewById(R.id.tvDate);
            tvVeh = view.findViewById(R.id.tvVeh);
            tvMobile1 = view.findViewById(R.id.tvMobile1);
            tvMobile2 = view.findViewById(R.id.tvMobile2);
            tvMobile3 = view.findViewById(R.id.tvMobile3);
            tvRoute = view.findViewById(R.id.tvRoute);
            tvTrayStatus = view.findViewById(R.id.tvTrayStatus);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_fr_route, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final RouteWiseData model = routeList.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        holder.tvVeh.setText("" + model.getVehNo() + " " + model.getDriverName());
        holder.tvRoute.setText("" + model.getRouteName());

        try {

            if (model.getMobile1() == "") {
                holder.tvMobile1.setVisibility(View.GONE);
            } else {
                holder.tvMobile1.setVisibility(View.VISIBLE);
                holder.tvMobile1.setText("" + model.getMobile1());
            }

            if (model.getMobile2().isEmpty()) {
                holder.tvMobile2.setVisibility(View.GONE);
            } else {
                holder.tvMobile2.setVisibility(View.VISIBLE);
                holder.tvMobile2.setText("" + model.getMobile2());
            }

          /*  if (model.getMobile3() == "") {
                holder.tvMobile3.setVisibility(View.GONE);
            } else {
                holder.tvMobile3.setVisibility(View.VISIBLE);
                holder.tvMobile3.setText("" + model.getMobile1());
            }*/


            holder.tvMobile1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + model.getMobile1()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(callIntent);
                }
            });

            holder.tvMobile2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + model.getMobile2()));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(callIntent);
                }
            });


        } catch (Exception e) {
        }

        // holder.tvMobile.setText("" + model.getVehIntime());

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");


        try {
            Date date = sdf1.parse(model.getTranDate());
            holder.tvDate.setText("" + sdf2.format(date.getTime()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvTrayStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrayActivity.class);
                intent.putExtra("tranId", model.getTranId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }


}
