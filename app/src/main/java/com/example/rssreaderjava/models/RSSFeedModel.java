package com.example.rssreaderjava.models;

public class RSSFeedModel {
    public String title;
    public boolean isShowSeeLater;
    private String link;
    public String description;

    public RSSFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.isShowSeeLater = true;
    }

    public boolean isShowSeeLater() {
        return isShowSeeLater;
    }

    public void setShowSeeLater(boolean showSeeLater) {
        isShowSeeLater = showSeeLater;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
