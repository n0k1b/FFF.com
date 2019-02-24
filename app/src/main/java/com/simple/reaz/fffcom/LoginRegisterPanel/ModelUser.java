package com.simple.reaz.fffcom.LoginRegisterPanel;

import com.google.gson.annotations.SerializedName;

public class ModelUser {
    @SerializedName("response")
    String response;
    @SerializedName("user_mobile")
    String user_mobile;
    @SerializedName("user_name")
    String user_name;
    @SerializedName("password")
    String password;
    @SerializedName("pro_image")
    String pro_image;

    public ModelUser(String user_mobile, String user_name, String password, String pro_image) {
        this.user_mobile = user_mobile;
        this.user_name = user_name;
        this.password = password;
        this.pro_image = pro_image;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPro_image() {
        return pro_image;
    }

    public void setPro_image(String pro_image) {
        this.pro_image = pro_image;
    }
}
