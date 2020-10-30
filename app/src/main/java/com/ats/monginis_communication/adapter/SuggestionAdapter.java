package com.ats.monginis_communication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.monginis_communication.R;
import com.ats.monginis_communication.activity.AddSuggestionActivity;
import com.ats.monginis_communication.activity.InnerTabActivity;
import com.ats.monginis_communication.activity.SuggestionDetailActivity;
import com.ats.monginis_communication.activity.TabActivity;
import com.ats.monginis_communication.bean.Info;
import com.ats.monginis_communication.bean.SuggestionData;
import com.ats.monginis_communication.common.CommonDialog;
import com.ats.monginis_communication.constants.Constants;
import com.ats.monginis_communication.db.DatabaseHandler;
import com.ats.monginis_communication.fragment.SuggestionFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MAXADMIN on 30/1/2018.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private ArrayList<SuggestionData> suggestionList;
    private Context context;
    DatabaseHandler db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, desc, date, count;
        public LinearLayout llBack;
        public ImageView ivImage;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tvSuggestionItem_Title);
            desc = view.findViewById(R.id.tvSuggestionItem_Desc);
            date = view.findViewById(R.id.tvSuggestionItem_Date);
            llBack = view.findViewById(R.id.llSuggestionItem);
            ivImage = view.findViewById(R.id.ivSuggestionItem_Image);
            count = view.findViewById(R.id.tvSuggestionItem_Count);
        }
    }

    public SuggestionAdapter(ArrayList<SuggestionData> suggestionList, Context context) {
        this.suggestionList = suggestionList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_suggestion_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SuggestionData suggestion = suggestionList.get(position);
        holder.title.setText(suggestion.getTitle());
        holder.desc.setText(suggestion.getDescription());

        String time=suggestion.getTime();
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("hh:mm a");
            SimpleDateFormat sdf1=new SimpleDateFormat("HH:mm:ss");

            time=sdf.format(sdf1.parse(suggestion.getTime()));

        }catch (Exception e){}

        holder.date.setText(suggestion.getDate()+" "+time);

        db = new DatabaseHandler(context);

        String image = Constants.SUGGESTION_IMAGE_URL + suggestion.getPhoto();
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
                Intent intent = new Intent(context, SuggestionDetailActivity.class);
                intent.putExtra("suggestionId", suggestion.getSuggestionId());
                intent.putExtra("refresh", 0);
                context.startActivity(intent);
            }
        });

        if (db.getSuggestionDetailUnreadCount(suggestion.getSuggestionId()) > 0) {
            holder.count.setVisibility(View.VISIBLE);
            holder.count.setText("" + db.getSuggestionDetailUnreadCount(suggestion.getSuggestionId()));
        } else {
            holder.count.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }


}
