package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.FeedbackDetail;
import com.ats.monginis_communication.bean.SuggestionDetail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by MAXADMIN on 1/2/2018.
 */

public class FeedbackDetailAdapter extends RecyclerView.Adapter<FeedbackDetailAdapter.MyViewHolder> {


    private ArrayList<FeedbackDetail> feedbackDetailList;
    private Context context;
    private int id;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOther, tvOtherTime,tvName;
        public LinearLayout llOther, llParent;

        public MyViewHolder(View view) {
            super(view);
            tvOther = view.findViewById(R.id.tvSuggestionDetail_Others);
            tvOtherTime = view.findViewById(R.id.tvSuggestionDetail_OthersDate);
            tvName = view.findViewById(R.id.tvSuggestionDetail_Name);
            llOther = view.findViewById(R.id.llSuggestionDetail_Other);
            llParent = view.findViewById(R.id.llSuggestionDetail_ParentLayout);

        }
    }

    public FeedbackDetailAdapter(ArrayList<FeedbackDetail> feedbackDetailList, Context context,int id) {
        this.feedbackDetailList = feedbackDetailList;
        this.context = context;
        this.id=id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_suggestion_detail_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FeedbackDetail feedback = feedbackDetailList.get(position);

        String dateDisplay = "";
        long millis = 0;
        try {
            String time = feedback.getTime();
            int h = Integer.parseInt(time.substring(0, 2));
            int m = Integer.parseInt(time.substring(3, 5));
            int s = Integer.parseInt(time.substring(6, 7));

            //Log.e("h : " + h, ",  m : " + m + ",  s : " + s);

            String date = feedback.getDate();
            int dd = Integer.parseInt(date.substring(0, 2));
            int mm = Integer.parseInt(date.substring(3, 5));
            int yy = Integer.parseInt(date.substring(6, 10));

            //Log.e("dd : " + dd, ",  mm : " + mm + ",  yy : " + yy);

            Calendar calDate = Calendar.getInstance();
            calDate.set(Calendar.DAY_OF_MONTH, dd);
            calDate.set(Calendar.MONTH, (mm - 1));
            calDate.set(Calendar.YEAR, yy);
            calDate.set(Calendar.HOUR_OF_DAY, h);
            calDate.set(Calendar.MINUTE, m);
            calDate.set(Calendar.SECOND, s);

            millis = calDate.getTimeInMillis();


            Calendar todayCal = Calendar.getInstance();
            todayCal.set(Calendar.HOUR_OF_DAY, 0);
            todayCal.set(Calendar.MINUTE, 0);
            todayCal.set(Calendar.SECOND, 0);

            long todayMillis = todayCal.getTimeInMillis();

            if (millis > todayMillis) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm a");
                dateDisplay = sdf1.format(calDate.getTimeInMillis());

            } else {
                SimpleDateFormat sdf1 = new SimpleDateFormat("d MMM yy, h:mm a");
                dateDisplay = sdf1.format(calDate.getTimeInMillis());
            }


            //Log.e("DATE : ", "------------" + dateDisplay);


        } catch (Exception e) {
        }

        holder.tvName.setVisibility(View.VISIBLE);

        if (feedback.getFrId() == id) {
            holder.llParent.setGravity(Gravity.RIGHT);
            holder.llParent.setPadding(45, 0, 0, 0);
            holder.llOther.setBackgroundColor(Color.parseColor("#cee6ef"));
            holder.tvOther.setText(feedback.getMessage());
            holder.tvOtherTime.setText(dateDisplay);
            holder.tvName.setText(feedback.getFrName());
        } else {
            holder.llParent.setGravity(Gravity.LEFT);
            holder.llParent.setPadding(0, 0, 45, 0);
            holder.llOther.setBackgroundColor(Color.parseColor("#e9e9e9"));
            holder.tvOther.setText(feedback.getMessage());
            holder.tvOtherTime.setText(dateDisplay);
            holder.tvName.setText(feedback.getFrName());
        }

    }

    @Override
    public int getItemCount() {
        return feedbackDetailList.size();
    }


}
