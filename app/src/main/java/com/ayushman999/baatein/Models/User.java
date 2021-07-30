package com.ayushman999.baatein.Models;

public class User {
    private String uid,name,phoneNum,profileImg;

    public User()
    {
        //needed
    }
    public User(String uid, String name, String phoneNum, String profileImg) {
        this.uid = uid;
        this.name = name;
        this.phoneNum = phoneNum;
        this.profileImg = profileImg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
