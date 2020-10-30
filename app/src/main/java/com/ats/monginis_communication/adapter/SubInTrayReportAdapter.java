package com.ats.monginis_communication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.NewTrayReport;

import java.util.ArrayList;

public class SubInTrayReportAdapter extends RecyclerView.Adapter<SubInTrayReportAdapter.MyViewHolder> {

    private ArrayList<NewTrayReport> trayDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvInDate,tvInSmall,tvInBig,tvInLid;

        public MyViewHolder(View view) {
            super(view);
            tvInDate = view.findViewById(R.id.tvInDate);
            tvInSmall = view.findViewById(R.id.tvInSmall);
            tvInBig = view.findViewById(R.id.tvInBig);
            tvInLid = view.findViewById(R.id.tvInLid);
        }
    }


    public SubInTrayReportAdapter(ArrayList<NewTrayReport> trayDetailsList) {
        this.trayDetailsList = trayDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sub_intray_report, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewTrayReport details = trayDetailsList.get(position);

        holder.tvInDate.setText("" + details.getIntrayDate());
        holder.tvInSmall.setText("" + details.getIntraySmall());
        holder.tvInBig.setText("" + details.getIntrayBig());
        holder.tvInLid.setText("" + details.getIntrayLead());
    }

    @Override
    public int getItemCount() {
        return trayDetailsList.size();
    }

}
