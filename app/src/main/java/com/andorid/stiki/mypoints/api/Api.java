package com.andorid.stiki.mypoints.api;

import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.andorid.stiki.mypoints.api.response.JenisResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;
import com.andorid.stiki.mypoints.api.response.LingkupResponse;
import com.andorid.stiki.mypoints.api.response.MahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.PeranResponse;
import com.andorid.stiki.mypoints.api.response.PoinMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.PoinResponse;
import com.andorid.stiki.mypoints.api.response.TotalEksternalResponse;
import com.andorid.stiki.mypoints.api.response.TotalPoinResponse;
import com.andorid.stiki.mypoints.api.response.TotalSeminarResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @POST("api_add_mahasiswa.php")
    @FormUrlEncoded
    Call<BaseResponse> postMahasiswa(
            @Field("nrp") String nrp,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("prodi") String prodi,
            @Field("angkatan") String angkatan
    );

    @POST("api_get_detail_mahasiswa.php")
    @FormUrlEncoded
    Call<MahasiswaResponse> getDetailMahasiswa(
            @Field("email") String email
    );

    @POST("api_get_blog.php")
    @FormUrlEncoded
    Call<BlogResponse> getBlog(
            @Field("nrp") String nrp
    );

    @POST("api_get_kegiatan_mahasiswa.php")
    @FormUrlEncoded
    Call<KegiatanMahasiswaResponse> getKegiatanMahasiswa(
            @Field("nrp") String nrp,
            @Field("id_jenis") String id_jenis
    );

    @POST("api_add_blog.php")
    @FormUrlEncoded
    Call<BaseResponse> postBlog(
            @Field("nrp") String nrp,
            @Field("judul") String judul,
            @Field("url") String url
    );

    @POST("api_update_blog.php")
    @FormUrlEncoded
    Call<BaseResponse> updateBlog(
            @Field("id_blog") String id_blog,
            @Field("judul") String judul,
            @Field("url") String url
    );

    @POST("api_get_jenis.php")
    Call<JenisResponse> getJenis();

    @POST("api_get_peran.php")
    @FormUrlEncoded
    Call<PeranResponse> getPeran(
            @Field("id_jenis") String id_jenis
    );

    @POST("api_get_poin_mahasiswa.php")
    @FormUrlEncoded
    Call<PoinMahasiswaResponse> getPoinMahasiswa(
            @Field("nrp") String nrp
    );

    @POST("api_get_total_poin_mahasiswa.php")
    @FormUrlEncoded
    Call<TotalPoinResponse> getTotalPoin(
            @Field("nrp") String nrp
    );

    @POST("api_get_total_eksternal.php")
    @FormUrlEncoded
    Call<TotalEksternalResponse> getTotalEksternal(
            @Field("nrp") String nrp
    );

    @POST("api_delete_tugas_khusus.php")
    @FormUrlEncoded
    Call<BaseResponse> deleteTugasKhusus(
            @Field("id_tugas_khusus") String id_tugas_khusus
    );

    @POST("api_delete_blog.php")
    @FormUrlEncoded
    Call<BaseResponse> deleteBlog(
            @Field("id_blog") String id_blog
    );

    @POST("api_get_total_seminar.php")
    @FormUrlEncoded
    Call<TotalSeminarResponse> getTotalSeminar(
            @Field("nrp") String nrp
    );

    @POST("api_get_lingkup.php")
    @FormUrlEncoded
    Call<LingkupResponse> getLingkup(
            @Field("id_jenis") String id_jenis,
            @Field("id_peran") String id_peran
    );

    @POST("api_get_poin.php")
    Call<PoinResponse> getPoin();

    @POST("api_get_kegiatan.php")
    Call<KegiatanResponse> getKegiatan();

    @POST("api_add_tugas_khusus.php")
    @FormUrlEncoded
    Call<BaseResponse> postTugasKhusus(
            @Field("nrp") String nrp,
            @Field("id_poin") String id_poin,
            @Field("judul") String judul,
            @Field("pembicara") String pembicara,
            @Field("kegiatan") String kegiatan,
            @Field("tanggal_acara") String tanggal_acara,
            @Field("foto") String foto
    );

    @POST("api_update_tugas_khusus.php")
    @FormUrlEncoded
    Call<BaseResponse> updateTugasKhusus(
            @Field("id_tugas_khusus") String id_tugas_khusus,
            @Field("id_poin") String id_poin,
            @Field("judul") String judul,
            @Field("pembicara") String pembicara,
            @Field("kegiatan") String kegiatan,
            @Field("tanggal_acara") String tanggal_acara,
            @Field("foto") String foto
    );
}
