package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.InboxMessage;
import com.ats.monginis_communication.bean.Message;
import com.ats.monginis_communication.bean.SchedulerList;

import java.util.ArrayList;

/**
 * Created by MAXADMIN on 5/3/2018.
 */

public class InboxMessageAdapter extends RecyclerView.Adapter<InboxMessageAdapter.MyViewHolder> {

    private ArrayList<InboxMessage> messageList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date;
        public LinearLayout llBack;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvNoticeItem_Title);
            desc = view.findViewById(R.id.tvNoticeItem_Desc);
            date = view.findViewById(R.id.tvNoticeItem_Date);
            llBack = view.findViewById(R.id.llNoticeItem);
        }
    }

    public InboxMessageAdapter(ArrayList<InboxMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notice_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        InboxMessage msg = messageList.get(position);
        holder.title.setText(msg.getTitle());
        holder.desc.setText(msg.getMessage());
        holder.date.setText(msg.getDate());

        if (position % 2 == 0) {
            holder.llBack.setBackgroundColor(Color.parseColor("#F5F1EE"));
        } else {
            holder.llBack.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


}
