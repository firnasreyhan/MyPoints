package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class KegiatanResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<KegiatanModel> data;

    public class KegiatanModel implements Serializable {
        @SerializedName("id_kegiatan")
        public String idKegiatan;

        @SerializedName("lingkup")
        public String lingkup;

        @SerializedName("jenis")
        public String jenis;

        @SerializedName("judul")
        public String judul;

        @SerializedName("pembicara")
        public String pembicara;

        @SerializedName("deskripsi")
        public String deskripsi;

        @SerializedName("tanggal_acara")
        public String tanggalAcara;

        @SerializedName("jam_mulai")
        public String jamMulai;

        @SerializedName("jam_selesai")
        public String jamSelesai;

        @SerializedName("poster")
        public String poster;

        @SerializedName("tangal_data")
        public String tangalData;

        @SerializedName("kuota")
        public String kuota;
    }
}
