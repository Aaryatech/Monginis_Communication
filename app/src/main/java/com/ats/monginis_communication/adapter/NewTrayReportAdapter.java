package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.TrayReportsActivity;
import com.ats.monginis_communication.bean.NewTrayReport;
import com.ats.monginis_communication.bean.TrayDetails;

import java.util.ArrayList;

public class NewTrayReportAdapter extends RecyclerView.Adapter<NewTrayReportAdapter.MyViewHolder> {

    private ArrayList<NewTrayReport> trayDetailsList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvOutDate, tvOutSmall, tvOutBig, tvOutLid, tvBalSmall, tvBalBig, tvBalLid;
        RecyclerView recyclerView;

        public MyViewHolder(View view) {
            super(view);
            tvOutDate = view.findViewById(R.id.tvOutDate);
            tvOutSmall = view.findViewById(R.id.tvOutSmall);
            tvOutBig = view.findViewById(R.id.tvOutBig);
            tvOutLid = view.findViewById(R.id.tvOutLid);
            tvBalSmall = view.findViewById(R.id.tvBalSmall);
            tvBalBig = view.findViewById(R.id.tvBalBig);
            tvBalLid = view.findViewById(R.id.tvBalLid);
            recyclerView = view.findViewById(R.id.recyclerView);
        }
    }


    public NewTrayReportAdapter(ArrayList<NewTrayReport> trayDetailsList, Context context) {
        this.trayDetailsList = trayDetailsList;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_new_tray_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewTrayReport details = trayDetailsList.get(position);

        holder.tvOutDate.setText("" + details.getOuttrayDate());
        holder.tvOutSmall.setText("" + details.getOuttraySmall());
        holder.tvOutBig.setText("" + details.getOuttrayBig());
        holder.tvOutLid.setText("" + details.getOuttrayLead());
        holder.tvBalSmall.setText("" + details.getBalSmall());
        holder.tvBalBig.setText("" + details.getBalBig());
        holder.tvBalLid.setText("" + details.getBalLead());

       // Log.e("SUB TRAY", "--------------> " + details.getIntrayyList());

        if (details.getIntrayyList() != null) {
            SubInTrayReportAdapter adapterNew = new SubInTrayReportAdapter((ArrayList<NewTrayReport>) details.getIntrayyList());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            holder.recyclerView.setLayoutManager(mLayoutManager);
            holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
            holder.recyclerView.setAdapter(adapterNew);
        }
    }

    @Override
    public int getItemCount() {
        return trayDetailsList.size();
    }

}
