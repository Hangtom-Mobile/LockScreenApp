package com.askhmer.lockscreen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class VideoNewFeed {

    @SerializedName("id")
    private String id;

    @SerializedName("ch_id")
    private String chId;

    @SerializedName("vd_code")
    private String videoId;

    @SerializedName("vd_title")
    private String title;

    @SerializedName("thumail1")
    private String thumnail1;

    @SerializedName("thumail2")
    private String thumail2;

    @SerializedName("duration")
    private String duration;

    @SerializedName("vd_desc")
    private String VideoDesc;

    @SerializedName("create_date")
    private String createDate;

    public VideoNewFeed(String videoId, String title, String thumnail, String thumail2) {
        this.videoId = videoId;
        this.title = title;
        this.thumnail1 = thumnail;
        this.thumail2 = thumail2;
    }

    public VideoNewFeed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChId() {
        return chId;
    }

    public void setChId(String chId) {
        this.chId = chId;
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

    public String getThumnail1() {
        return thumnail1;
    }

    public void setThumnail1(String thumnail1) {
        this.thumnail1 = thumnail1;
    }

    public String getThumail2() {
        return thumail2;
    }

    public void setThumail2(String thumail2) {
        this.thumail2 = thumail2;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getVideoDesc() {
        return VideoDesc;
    }

    public void setVideoDesc(String videoDesc) {
        VideoDesc = videoDesc;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
