package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PoinMahasiswaResponse extends BaseResponse{
    @SerializedName("data")
    public ArrayList<PoinMahasiswaModel> data;

    public static class PoinMahasiswaModel implements Serializable {
        @SerializedName("id_jenis")
        public String id_jenis;

        @SerializedName("jenis")
        public String jenis;

        @SerializedName("jumlah")
        public int jumlah;

        @SerializedName("poin")
        public int poin;
    }
}
