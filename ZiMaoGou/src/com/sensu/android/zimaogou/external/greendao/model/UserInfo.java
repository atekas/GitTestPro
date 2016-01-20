package com.sensu.android.zimaogou.external.greendao.model;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import com.sensu.android.zimaogou.utils.TextUtils;

/**
 * Entity mapped to table USER_INFO.
 */
public class UserInfo {

    private String name;
    private String sex;
    private String mobile;
    private String uid;
    private String avatar;
    private String email;
    private String token;
    private String isLogin;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public UserInfo() {
    }

    public UserInfo(String name, String sex, String mobile, String uid, String avatar, String email, String token, String isLogin) {
        this.name = name;
        this.sex = sex;
        this.mobile = mobile;
        this.uid = uid;
        this.avatar = avatar;
        this.email = email;
        this.token = token;
        this.isLogin = isLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        String hintMobile = "";
        if(!TextUtils.isEmpty(mobile)) {
            hintMobile = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        }
        return hintMobile;
    }
    public String getRealMobile(){

        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
