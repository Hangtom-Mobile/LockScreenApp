package com.askhmer.lockscreen.model;

/**
 * Created by Longdy on 6/11/2016.
 */
public class CompanyDto {
    private int companyId;
    private String companyName;
    private String bannerImage;
    private String urlWebsite;
    private int imgTest;

    public String getUrlWebsite() {
        return urlWebsite;
    }

    public void setUrlWebsite(String urlWebsite) {
        this.urlWebsite = urlWebsite;
    }

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

    public CompanyDto(String bannerImage, String urlWebsite){
        this.bannerImage = bannerImage;
        this.urlWebsite = urlWebsite;
    }
}
