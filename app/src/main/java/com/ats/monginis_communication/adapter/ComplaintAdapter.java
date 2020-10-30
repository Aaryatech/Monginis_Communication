package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.ComplaintDetailActivity;
import com.ats.monginis_communication.bean.ComplaintData;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by MAXADMIN on 1/2/2018.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

    private ArrayList<ComplaintData> complaintList;
    private Context context;
    DatabaseHandler db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date, count;
        public LinearLayout llBack;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvComplaintItem_Title);
            desc = view.findViewById(R.id.tvComplaintItem_Desc);
            date = view.findViewById(R.id.tvComplaintItem_Date);
            llBack = view.findViewById(R.id.llComplaintItem);
            ivImage = view.findViewById(R.id.ivComplaintItem_Image);
            count = view.findViewById(R.id.tvComplaintItem_Count);
        }
    }

    public ComplaintAdapter(ArrayList<ComplaintData> complaintList, Context context) {
        this.complaintList = complaintList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_complaint_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ComplaintData complaint = complaintList.get(position);
        holder.title.setText(complaint.getTitle());
        holder.desc.setText(complaint.getDescription());

        String time=complaint.getTime();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
            SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm:ss");

            time=sdf.format(sdf1.parse(complaint.getTime()));

        }catch (Exception e){}

        holder.date.setText(complaint.getDate()+" "+time);

        db = new DatabaseHandler(context);

        String image = Constants.COMPLAINT_IMAGE_URL + complaint.getPhoto1();
        try {
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.ivImage);
        } catch (Exception e) {
        }

        holder.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("COMPLAINT ID : ", "******--------------" + complaint.getComplaintId());
                Intent intent = new Intent(context, ComplaintDetailActivity.class);
                intent.putExtra("complaintId", complaint.getComplaintId());
                intent.putExtra("refresh", 0);
                context.startActivity(intent);
            }
        });


        if (db.getComplaintDetailUnreadCount(complaint.getComplaintId()) > 0) {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText("" + db.getComplaintDetailUnreadCount(complaint.getComplaintId()));
        } else {
            holder.count.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

}
