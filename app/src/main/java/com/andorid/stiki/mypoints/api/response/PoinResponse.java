package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PoinResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<PoinModel> data;

    public static class PoinModel {
        @SerializedName("id_poin")
        public String id_poin;

        @SerializedName("id_jenis")
        public String id_jenis;

        @SerializedName("id_peran")
        public String id_peran;

        @SerializedName("id_lingkup")
        public String id_lingkup;

        @SerializedName("poin")
        public int poin;
    }
}
