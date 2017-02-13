package com.askhmer.lockscreen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by medayi01 on 1/12/2017.
 */

public class VideoNewFeed {

    @SerializedName("wr_id")
    private String id;

    @SerializedName("wr_subject")
    private String subject;

    @SerializedName("wr_hit")
    private String hit;

    @SerializedName("wr_name")
    private String uploadName;

    @SerializedName("wr_datetime")
    private String datatime;

    @SerializedName("bf_file")
    private String image;

    public VideoNewFeed(String id, String subject, String hit, String uploadName, String datatime, String image) {
        this.id = id;
        this.subject = subject;
        this.hit = hit;
        this.uploadName = uploadName;
        this.datatime = datatime;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getUploadName() {
        return uploadName;
    }

    public void setUploadName(String uploadName) {
        this.uploadName = uploadName;
    }

    public String getDatatime() {
        return datatime;
    }

    public void setDatatime(String datatime) {
        this.datatime = datatime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
