package com.askhmer.mobileapp.model;

/**
 * Created by soklundy on 6/20/2016.
 */
public class SignupInformation {

    private String phoneNumber;
    private String cashSideId;
    private String password;
    private String name;
    private String gender;
    private String age;
    private String location;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCashSideId() {
        return cashSideId;
    }

    public void setCashSideId(String cashSideId) {
        this.cashSideId = cashSideId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
