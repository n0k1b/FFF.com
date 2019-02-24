package com.simple.reaz.fffcom.Home;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mir Reaz Uddin on 12-Jan-19.
 */
public class ModelCategories  {
    @SerializedName("id")
    private String id;
    @SerializedName("cat_image")
    private String cat_image;
    @SerializedName("cat_name")
    private String cat_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }
}

