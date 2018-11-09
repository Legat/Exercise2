package com.example.peter.exercise2;

import android.content.Context;

import com.example.peter.exercise2.Models.News;
import com.example.peter.exercise2.Models.Result;

import java.util.ArrayList;
import java.util.List;

public class BaseConverter {
     private ArrayList<NewsEntity> list;
     public BaseConverter(){

        list = new ArrayList<>();
    }

    public ArrayList<NewsEntity> toDataBase(List<Result> reslist){
           int count = 1;
          for(Result news: reslist){
             NewsEntity newsdata = new NewsEntity();
             newsdata.setId(count);
             if(news.getSubsection() != ""){
              newsdata.setCategory(news.getSubsection());
             } else{
                 newsdata.setCategory("");
             }
             newsdata.setFulltext(news.getAbstract());
             newsdata.setPublishDate(news.getPublishedDate());
             newsdata.setTitle(news.getTitle());
             newsdata.setUrl(news.getUrl());
             if(!news.getMultimedia().isEmpty()){
                 newsdata.setMultimediqUrl(news.getMultimedia().get(3).getUrl());
             } else {
                 newsdata.setMultimediqUrl("");
             }
             list.add(newsdata);
             count++;
         }

        return list;
    }
}
