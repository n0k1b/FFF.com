package com.simple.reaz.fffcom.JobPost;

import com.google.gson.annotations.SerializedName;

public class Model_Jobpost {
    @SerializedName("id")
    private String id;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("description")
    private String description;
    @SerializedName("category")
    private String category;
    @SerializedName("sub_category")
    private String sub_category;
    @SerializedName("image")
    private String image;
    @SerializedName("price")
    private String price;
    @SerializedName("location")
    private String location;
    @SerializedName("response")
    String response;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Model_Jobpost(String id, String mobile, String description, String cat, String sub_cat, String image, String price) {
        this.id = id;
        this.mobile = mobile;
        this.description = description;
        this.category = cat;
        this.sub_category = sub_cat;
        this.image = image;
        this.price = price;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
