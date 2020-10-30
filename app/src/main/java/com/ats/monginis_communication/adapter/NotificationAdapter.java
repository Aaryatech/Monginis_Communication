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
import com.ats.monginis_communication.activity.ImageZoomActivity;
import com.ats.monginis_communication.bean.NotificationData;
import com.ats.monginis_communication.bean.SchedulerList;
import com.ats.monginis_communication.constants.Constants;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by MAXADMIN on 30/1/2018.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private ArrayList<NotificationData> notificationList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date;
        public LinearLayout llBack;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvNotificationItem_Title);
            desc = view.findViewById(R.id.tvNotificationItem_Desc);
            date = view.findViewById(R.id.tvNotificationItem_Date);
            llBack = view.findViewById(R.id.llNotificationItem);
            ivImage = view.findViewById(R.id.ivNotificationItem_Image);
        }
    }

    public NotificationAdapter(ArrayList<NotificationData> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_notification_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationData notification = notificationList.get(position);
        holder.title.setText(notification.getSubject());
        holder.desc.setText(notification.getDescription());

//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        long dtVal = Long.parseLong(notification.getDate());
//        String date = sdf.format(dtVal);
        Log.e("Notification : ", "   DATE : -------" + notification.getDate());
        holder.date.setText("" + notification.getDate());

       // final String image = Constants.NOTIFICATION_IMAGE_URL + notification.getPhoto();

        String image=Constants.NOTIFICATION_IMAGE_URL + notification.getPhoto();
        if (notification.getSubject().equalsIgnoreCase("New cake added to album")){
            image = Constants.ALBUM_IMAGE_URL + notification.getPhoto();
        }else{
            image = Constants.NOTIFICATION_IMAGE_URL + notification.getPhoto();
        }

        try {
            Picasso.with(context)
                    .load(image)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .resize(150,150)
                    .into(holder.ivImage);
        } catch (Exception e) {
        }

        final String finalImage = image;
        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageZoomActivity.class);
                intent.putExtra("image", finalImage);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
