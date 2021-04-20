package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PeranResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<PeranModel> data;

    public static class PeranModel {
        @SerializedName("id_peran")
        public String id_peran;

        @SerializedName("peran")
        public String peran;
    }
}
