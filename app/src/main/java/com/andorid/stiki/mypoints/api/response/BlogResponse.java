package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class BlogResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<BlogModel> data;

    public static class BlogModel implements Serializable {
        @SerializedName("id_blog")
        public String id_blog;

        @SerializedName("judul")
        public String judul;

        @SerializedName("url")
        public String url;
    }
}
