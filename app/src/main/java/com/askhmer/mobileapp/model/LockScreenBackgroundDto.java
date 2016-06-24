package com.askhmer.mobileapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by soklundy on 6/23/2016.
 */
public class LockScreenBackgroundDto {

    @SerializedName("uid")
    private String uId;

    @SerializedName("lock_basic_price")
    private String lockBasicPrice;

    @SerializedName("lock_view_price")
    private String lockViewPrice;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("url")
    private String webUrl;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLockBasicPrice() {
        return lockBasicPrice;
    }

    public void setLockBasicPrice(String lockBasicPrice) {
        this.lockBasicPrice = lockBasicPrice;
    }

    public String getLockViewPrice() {
        return lockViewPrice;
    }

    public void setLockViewPrice(String lockViewPrice) {
        this.lockViewPrice = lockViewPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

}
