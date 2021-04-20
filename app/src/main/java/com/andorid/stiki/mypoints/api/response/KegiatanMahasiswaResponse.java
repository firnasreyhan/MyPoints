package com.andorid.stiki.mypoints.api.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class KegiatanMahasiswaResponse extends BaseResponse {
    @SerializedName("data")
    public ArrayList<KegiatanMahasiswaModel> data;

    public static class KegiatanMahasiswaModel implements Serializable {
        @SerializedName("id_tugas_khusus")
        public String id_tugas_khusus;

        @SerializedName("jenis")
        public String jenis;

        @SerializedName("peran")
        public String peran;

        @SerializedName("lingkup")
        public String lingkup;

        @SerializedName("judul")
        public String judul;

        @SerializedName("pembicara")
        public String pembicara;

        @SerializedName("kegiatan")
        public String kegiatan;

        @SerializedName("tanggal_acara")
        public String tanggal_acara;

        @SerializedName("foto")
        public String foto;

        @SerializedName("poin")
        public String poin;
    }
}
