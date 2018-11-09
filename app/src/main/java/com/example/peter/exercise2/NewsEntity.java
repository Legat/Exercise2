package com.example.peter.exercise2;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "news")
public class NewsEntity {
    @PrimaryKey()
    private int id;
    private String category;  // subsection
    private String title;
    private String fulltext;

    @ColumnInfo(name = "publish_date")
    private String publishDate;

    @ColumnInfo(name = "multimedia_url")
    private String multimediqUrl;

    private String url;     // news Url

    public NewsEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getMultimediqUrl() {
        return multimediqUrl;
    }

    public void setMultimediqUrl(String multimediqUrl) {
        this.multimediqUrl = multimediqUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
