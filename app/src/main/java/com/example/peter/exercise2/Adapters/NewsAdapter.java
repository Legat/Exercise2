package com.example.peter.exercise2.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.peter.exercise2.Models.Result;
import com.example.peter.exercise2.NewsDetailsActivity;
import com.example.peter.exercise2.NewsEntity;
import com.example.peter.exercise2.R;


import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder>  {
    private Context context;
    private String multimediaUrl;
    private int screenWidth;
    private List<NewsEntity> newsList;
    public static final int REQUEST_CODE = 0;
  //  private List<Result> resList;

    public NewsAdapter(Context context, List<NewsEntity> newsList, int screenWidth) {
        this.context = context;
        this.newsList = newsList;
        this.screenWidth = screenWidth;
    }

    @NonNull
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.newsitem, parent,false);
        return new NewsHolder(view);
      //  return new NewsHolder(LayoutInflater.from(context).inflate(R.layout.newsitem, parent, false));
    }

    public void onBindViewHolder(@NonNull final NewsHolder holder, int position) {
        final NewsEntity news = newsList.get(position);
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            holder.cardplace.setLayoutParams(new LinearLayout.LayoutParams(screenSize(), LinearLayout.LayoutParams.WRAP_CONTENT));
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            holder.cardplace.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        if(news.getCategory()!= null && news.getCategory() != ""){
            holder.categorytxt.setText(news.getCategory());
        } else {
            holder.categorytxt.setVisibility(View.GONE);
        }
        holder.titletxt.setText(news.getTitle());
        holder.previewtxt.setText(news.getFulltext());
        holder.datetxt.setText(news.getPublishDate());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_action_holder);
      //  requestOptions.centerCrop();
        if(news.getMultimediqUrl() != ""){
            multimediaUrl = news.getMultimediqUrl();
            Glide.with(context).setDefaultRequestOptions(requestOptions)
                    .load(multimediaUrl)
                    .into(holder.imageNews);
        } else{
            holder.imageNews.setBackgroundResource(R.drawable.ic_action_holder);
        }

        holder.cardplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //  String name = news.getCategory().getName();
            //  Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
              Intent intent = new Intent(context, NewsDetailsActivity.class);
           //   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              intent.putExtra(NewsEntity.class.getSimpleName(), news.getId());
            //  intent.putExtra(Result.class.getSimpleName(), news.getUrl());
            //  intent.putExtra(Result.class.getName(), multimediaUrl);
              //context.startActivity(intent);
                Activity ListActivity = (Activity) context;
                ListActivity.startActivityForResult(intent, REQUEST_CODE);

              //   context.getApplicationContext().startActivity(intent);
            }
        });
    }

    public int getItemCount() {
        return newsList.size();
    }


    public void onAdapterResult(int requestCode, int resultCode, Intent data){
        Toast.makeText(context,"Contact",Toast.LENGTH_LONG);
    }

    public class NewsHolder extends RecyclerView.ViewHolder{
        private TextView categorytxt;
        private TextView datetxt;
        private ImageView imageNews;
        private TextView previewtxt;
        private TextView titletxt;
        private CardView cardplace;

        private NewsHolder(View itemView) {
            super(itemView);
            titletxt =  itemView.findViewById(R.id.tittle);
            categorytxt =  itemView.findViewById(R.id.category);
            previewtxt =  itemView.findViewById(R.id.previwnews);
            datetxt =  itemView.findViewById(R.id.publish_date);
            imageNews =  itemView.findViewById(R.id.image_news);
            cardplace = itemView.findViewById(R.id.cardplace);

        }


    }

    public void filter(List<NewsEntity> filterList){
        newsList = filterList;
        notifyDataSetChanged();
    }

    public void delete(int id){
        int position = 0;
        for(NewsEntity news : newsList){
            if(news.getId() == id){
              position = newsList.indexOf(news);
              newsList.remove(news);
              break;
            }
        }
       notifyItemRemoved(position);
    }

    public void edit(int id, String title, String fullText, String publishText){
      int position = 0;
      for(NewsEntity news: newsList){
          if(news.getId() == id){
             position = newsList.indexOf(news);
             newsList.get(position).setTitle(title);
             newsList.get(position).setFulltext(fullText);
             newsList.get(position).setPublishDate(publishText);
             break;
          }
      }
      notifyItemChanged(position);
    }

    private int screenSize(){
        DisplayMetrics metrix = context.getResources().getDisplayMetrics();
        int width = metrix.widthPixels/2;
        return width;
    }

}

