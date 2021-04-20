package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TotalPoinResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<TotalPoin> data;

    public class TotalPoin {
        @SerializedName("nrp")
        public String nrp;

        @SerializedName("poin")
        public int poin;
    }
}
