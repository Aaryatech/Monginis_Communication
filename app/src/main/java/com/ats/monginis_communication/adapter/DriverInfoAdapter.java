package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.DriverInfo;
import com.ats.monginis_communication.bean.TrayDetails;

import java.util.ArrayList;

public class DriverInfoAdapter extends RecyclerView.Adapter<DriverInfoAdapter.MyViewHolder> {

    private ArrayList<DriverInfo> driverList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvMobile,tvVeh;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvMobile = view.findViewById(R.id.tvMobile);
            tvVeh = view.findViewById(R.id.tvVeh);
        }
    }


    public DriverInfoAdapter(ArrayList<DriverInfo> driverList,Context context) {
        this.driverList = driverList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_driver_info, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final DriverInfo info = driverList.get(position);

        holder.tvName.setText("" + info.getDriverName());
        holder.tvMobile.setText("" + info.getMobile1());
        holder.tvVeh.setText("" + info.getVehNo());

        holder.tvMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = info.getMobile1();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                int result = context.checkCallingOrSelfPermission(android.Manifest.permission.CALL_PHONE);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(intent);
                } else {
                    //  Toast.makeText(getActivity(), "Device not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

}