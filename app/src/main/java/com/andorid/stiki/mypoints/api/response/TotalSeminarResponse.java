package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalSeminarResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<TotalSeminar> data;

    public class TotalSeminar {
        @SerializedName("lingkup")
        public String lingkup;

        @SerializedName("total")
        public int total;
    }
}
