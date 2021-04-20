package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LingkupResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<LingkupModel> data;

    public static class LingkupModel {
        @SerializedName("id_lingkup")
        public String id_lingkup;

        @SerializedName("lingkup")
        public String lingkup;
    }
}
