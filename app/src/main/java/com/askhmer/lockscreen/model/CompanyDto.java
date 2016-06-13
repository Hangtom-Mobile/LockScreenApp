package com.askhmer.lockscreen.model;

/**
 * Created by Longdy on 6/11/2016.
 */
public class CompanyDto {
    private int companyId;
    private String companyName;
    private String bannerImage;
    private int imgTest;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public int getImgTest() {
        return imgTest;
    }

    public void setImgTest(int imgTest) {
        this.imgTest = imgTest;
    }
}
