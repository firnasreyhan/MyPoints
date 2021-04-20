package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JenisResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<JenisModel> data;

    public static class JenisModel {
        @SerializedName("id_jenis")
        public String id_jenis;

        @SerializedName("jenis")
        public String jenis;
    }
}
