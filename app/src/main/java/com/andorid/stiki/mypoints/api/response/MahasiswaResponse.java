package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MahasiswaResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<MahasiswaModel> data;

    public static class MahasiswaModel {
        @SerializedName("nrp")
        public String nrp;

        @SerializedName("nama")
        public String nama;

        @SerializedName("email")
        public String email;

        @SerializedName("prodi")
        public String prodi;

        @SerializedName("angkatan")
        public String angkatan;

        @SerializedName("nilai")
        public String nilai;

        @SerializedName("tanggal_validasi")
        public String tanggal_validasi;

        @SerializedName("status")
        public String status;
    }
}
