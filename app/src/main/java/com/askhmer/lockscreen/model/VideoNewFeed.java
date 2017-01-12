package com.askhmer.lockscreen.model;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class VideoNewFeed {

    private String videoId;
    private String title;
    private String thumnail;
    private String videoUrl;

    public VideoNewFeed(String videoId, String title, String thumnail, String videoUrl) {
        this.videoId = videoId;
        this.title = title;
        this.thumnail = thumnail;
        this.videoUrl = videoUrl;
    }

    public VideoNewFeed() {
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
