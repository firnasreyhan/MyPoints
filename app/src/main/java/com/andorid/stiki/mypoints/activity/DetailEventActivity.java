package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;

public class DetailEventActivity extends AppCompatActivity {
    private KegiatanResponse.KegiatanModel kegiatanModel;

    private Toolbar toolbar;
    private ImageView imageViewPoster;
    private TextView textViewJudul, textViewPembicara, textViewTanggal, textViewJam, textViewJenis, textViewLingkup, textViewKuota, textViewDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        kegiatanModel = (KegiatanResponse.KegiatanModel) getIntent().getSerializableExtra("kegiatan");

        toolbar = findViewById(R.id.toolbar);
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewJudul = findViewById(R.id.textViewJudul);
        textViewPembicara = findViewById(R.id.textViewPembicara);
        textViewTanggal = findViewById(R.id.textViewTanggal);
        textViewJam = findViewById(R.id.textViewJam);
        textViewJenis = findViewById(R.id.textViewJenis);
        textViewLingkup = findViewById(R.id.textViewLingkup);
        textViewKuota = findViewById(R.id.textViewKuota);
        textViewDeskripsi = findViewById(R.id.textViewDeskripsi);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        byte[] decodedString = Base64.decode(kegiatanModel.poster, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        imageViewPoster.setImageBitmap(decodedByte);
        textViewJudul.setText(kegiatanModel.judul);
        textViewPembicara.setText(kegiatanModel.pembicara);
        textViewTanggal.setText(kegiatanModel.tanggalAcara);
        textViewJam.setText(kegiatanModel.jamMulai);
        textViewJenis.setText(kegiatanModel.jenis);
        textViewLingkup.setText(kegiatanModel.lingkup);
        textViewKuota.setText(kegiatanModel.kuota + " Peserta");
        textViewDeskripsi.setText(kegiatanModel.deskripsi);
    }
}