package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.bean.SuggestionDetail;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MAXADMIN on 30/1/2018.
 */

public class SuggestionDetailAdapter extends RecyclerView.Adapter<SuggestionDetailAdapter.MyViewHolder> {

    private ArrayList<SuggestionDetail> suggestionDetailList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOther, tvOtherTime, tvName;
        public LinearLayout llOther, llParent;

        public MyViewHolder(View view) {
            super(view);
            tvOther = view.findViewById(R.id.tvSuggestionDetail_Others);
            tvOtherTime = view.findViewById(R.id.tvSuggestionDetail_OthersDate);
            llOther = view.findViewById(R.id.llSuggestionDetail_Other);
            llParent = view.findViewById(R.id.llSuggestionDetail_ParentLayout);
            tvName = view.findViewById(R.id.tvSuggestionDetail_Name);

        }
    }

    public SuggestionDetailAdapter(ArrayList<SuggestionDetail> suggestionDetailList, Context context) {
        this.suggestionDetailList = suggestionDetailList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_suggestion_detail_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SuggestionDetail suggestion = suggestionDetailList.get(position);

        String dateDisplay = "";
        long millis = 0;
        String dispDate="";
        try {

            SimpleDateFormat sdfTime1 = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat sdfTime2 = new SimpleDateFormat("hh:mm a");

            SimpleDateFormat sdfDate1 = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat sdfDate2 = new SimpleDateFormat("dd MMM yyyy");

            Date d = sdfTime1.parse(suggestion.getTime());
            Date d1 = sdfDate1.parse(suggestion.getDate());
            Date d2 = new Date();

            try {
                    dispDate = sdfDate2.format(d1.getTime()) + " " + sdfTime2.format(d.getTime());
            } catch (Exception e) {
            }

            String time = suggestion.getTime();
            int h = Integer.parseInt(time.substring(0, 2));
            int m = Integer.parseInt(time.substring(3, 5));
            int s = Integer.parseInt(time.substring(6, 7));

            //Log.e("h : " + h, ",  m : " + m + ",  s : " + s);

            String date = suggestion.getDate();
            //Log.e("DATE : ","*******************"+date);
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
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy, h:mm a");
                dateDisplay = sdf1.format(calDate.getTimeInMillis());
            }


            //Log.e("DATE : ", "------------" + dateDisplay);


        } catch (Exception e) {
        }

        holder.tvName.setVisibility(View.VISIBLE);

        if (suggestion.getIsAdmin() == 0) {
            holder.llParent.setGravity(Gravity.RIGHT);
            holder.llParent.setPadding(45, 0, 0, 0);
            holder.llOther.setBackgroundColor(Color.parseColor("#cee6ef"));
           // holder.tvName.setVisibility(View.GONE);
        } else {
            holder.llParent.setGravity(Gravity.LEFT);
            holder.llParent.setPadding(0, 0, 45, 0);
            holder.llOther.setBackgroundColor(Color.parseColor("#e9e9e9"));
            //holder.tvName.setVisibility(View.VISIBLE);
        }

        holder.tvOther.setText(suggestion.getMessage());
        holder.tvOtherTime.setText(dispDate);
        //Log.e("NAME - ","--------------> "+suggestion.getFrName());
        holder.tvName.setText(""+suggestion.getFrName());

    }

    @Override
    public int getItemCount() {
        return suggestionDetailList.size();
    }


}
