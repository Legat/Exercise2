package com.example.peter.exercise2.Models;

import java.io.Serializable;
import java.util.Date;

public class NewsItem implements Serializable {
    private final Category category;
    private final String fullText;
    private final String imageUrl;
    private final String previewText;
    private final Date publishDate;
    private final String title;

    public NewsItem(String title, String imageUrl, Category category, Date publishDate, String previewText, String fullText) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.fullText = fullText;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public Category getCategory() {
        return this.category;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public String getPreviewText() {
        return this.previewText;
    }

    public String getFullText() {
        return this.fullText;
    }
}
