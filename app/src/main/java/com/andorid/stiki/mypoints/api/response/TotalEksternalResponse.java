package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalEksternalResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<TotalEksternal> data;

    public class TotalEksternal {
        @SerializedName("eksternal")
        public int eksternal;
    }
}
