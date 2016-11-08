package com.askhmer.lockscreen.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by soklundy on 11/6/2016.
 */

public class TopUpDetail {

    @SerializedName("topup_id")
    private String topUpId;

    @SerializedName("topup_point")
    private String topUpPoint;

    @SerializedName("topup_charge")
    private String topUpCharge;

    @SerializedName("topup_name")
    private String topName;

    @SerializedName("topup_image")
    private String topUpImage;

    @SerializedName("topup_color")
    private String topUpColor;

    public String getTopUpId() {
        return topUpId;
    }

    public void setTopUpId(String topUpId) {
        this.topUpId = topUpId;
    }

    public String getTopUpPoint() {
        return topUpPoint;
    }

    public void setTopUpPoint(String topUpPoint) {
        this.topUpPoint = topUpPoint;
    }

    public String getTopUpCharge() {
        return topUpCharge;
    }

    public void setTopUpCharge(String topUpCharge) {
        this.topUpCharge = topUpCharge;
    }

    public String getTopName() {
        return topName;
    }

    public void setTopName(String topName) {
        this.topName = topName;
    }

    public String getTopUpImage() {
        return topUpImage;
    }

    public void setTopUpImage(String topUpImage) {
        this.topUpImage = topUpImage;
    }

    public String getTopUpColor() {
        return topUpColor;
    }

    public void setTopUpColor(String topUpColor) {
        this.topUpColor = topUpColor;
    }
}
