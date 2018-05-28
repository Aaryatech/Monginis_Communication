package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.FeedbackDetailActivity;
import com.ats.monginis_communication.activity.SuggestionDetailActivity;
import com.ats.monginis_communication.bean.FeedbackData;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MAXADMIN on 1/2/2018.
 */

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private ArrayList<FeedbackData> feedbackList;
    private Context context;
    DatabaseHandler db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date, count;
        public LinearLayout llBack;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvFeedbackItem_Title);
            desc = view.findViewById(R.id.tvFeedbackItem_Desc);
            date = view.findViewById(R.id.tvFeedbackItem_Date);
            llBack = view.findViewById(R.id.llFeedbackItem);
            ivImage = view.findViewById(R.id.ivFeedbackItem_Image);
            count = view.findViewById(R.id.tvFeedbackItem_Count);
        }
    }

    public FeedbackAdapter(ArrayList<FeedbackData> feedbackList, Context context) {
        this.feedbackList = feedbackList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_feedback_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final FeedbackData feedback = feedbackList.get(position);
        holder.title.setText(feedback.getTitle());
        holder.desc.setText(feedback.getDescription());
        holder.date.setText(feedback.getDate());

        db = new DatabaseHandler(context);

        String image = Constants.FEEDBACK_IMAGE_URL + feedback.getPhoto();
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
                Intent intent = new Intent(context, FeedbackDetailActivity.class);
                intent.putExtra("feedbackId", feedback.getFeedbackId());
                context.startActivity(intent);
            }
        });

        if (db.getFeedbackDetailUnreadCount(feedback.getFeedbackId()) > 0) {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText("" + db.getFeedbackDetailUnreadCount(feedback.getFeedbackId()));
        } else {
            holder.count.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }


}
