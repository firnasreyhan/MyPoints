package com.andorid.stiki.mypoints.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.adapter.RecyclerViewCategoryAdapter;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.MahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.PoinMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.TotalEksternalResponse;
import com.andorid.stiki.mypoints.api.response.TotalPoinResponse;
import com.andorid.stiki.mypoints.api.response.TotalSeminarResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.UnknownHostException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Api api;
    private FirebaseUser firebaseUser;

    private CircleImageView circleImageView;
    private LinearLayout linearLayoutSignOut, linearLayoutKosong;
    private Toolbar toolbar;
    private CardView cardViewAddPoint, cardViewEventList, cardViewInformation, cardViewSummary, cardViewBlog;
    private TextView textViewName, textViewEmail, textViewTotalPoin, textViewNilai, textViewStatus, textViewTanggalValidasi, textViewJumlahBlog;
    private RecyclerView recyclerViewJenis;
    private ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayoutPoin;
    private Button buttonAjukanValidasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        circleImageView = findViewById(R.id.circleImageView);
        linearLayoutSignOut = findViewById(R.id.linearLayoutSignOut);
        linearLayoutKosong = findViewById(R.id.linearLayoutKosong);
        cardViewAddPoint = findViewById(R.id.cardViewAddPoint);
        cardViewEventList = findViewById(R.id.cardViewEventList);
        cardViewInformation = findViewById(R.id.cardViewInformation);
        cardViewSummary = findViewById(R.id.cardViewSummary);
        cardViewBlog = findViewById(R.id.cardViewBlog);
        textViewName = findViewById(R.id.textViewName);
        textViewJumlahBlog = findViewById(R.id.textViewJumlahBlog);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewTotalPoin = findViewById(R.id.textViewTotalPoin);
        textViewNilai = findViewById(R.id.textViewNilai);
        textViewStatus = findViewById(R.id.textViewStatus);
        textViewTanggalValidasi = findViewById(R.id.textViewTanggalValidasi);
        buttonAjukanValidasi = findViewById(R.id.buttonAjukanValidasi);

        recyclerViewJenis = findViewById(R.id.recyclerViewJenis);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        shimmerFrameLayoutPoin = findViewById(R.id.shimmerFrameLayoutPoin);

        setSupportActionBar(toolbar);

        Glide.with(this)
                .load(firebaseUser.getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(circleImageView);

        api = ApiClient.getClient();

        getDetailMahasiswa();

        linearLayoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Apakah anda yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doSignOut();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        textViewName.setText(firebaseUser.getDisplayName().substring(10));
        textViewEmail.setText(firebaseUser.getEmail());

        cardViewAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
        cardViewEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EventActivity.class));
            }
        });
        cardViewInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InformationActivity.class));
            }
        });

        cardViewBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListBlogActivity.class);
                intent.putExtra("nrp", firebaseUser.getEmail().substring(0,9));
                v.getContext().startActivity(intent);
            }
        });
    }

    public void doSignOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                        finish();
                    }
                });
    }

    public void setRecyclerViewJenis(ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list) {
        recyclerViewJenis.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewJenis.setAdapter(new RecyclerViewCategoryAdapter(list));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerViewJenis.setVisibility(View.VISIBLE);
    }

    public void getTotalSeminar(int poin, ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list, boolean eksternal, boolean blog) {
        api.getTotalSeminar(firebaseUser.getEmail().substring(0,9)).enqueue(new Callback<TotalSeminarResponse>() {
            @Override
            public void onResponse(Call<TotalSeminarResponse> call, Response<TotalSeminarResponse> response) {
                if (list != null) {
                    int cekWorkshop = 0;
                    int cekStudiEkskursi = 0;
                    int cekPameranProduk = 0;
                    int cekKuliahTamu = 0;
                    int cekKompetisiIlmiah = 0;

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).id_jenis.equalsIgnoreCase("1")) {
                            cekKuliahTamu = list.get(i).jumlah;
                        }
                        if (list.get(i).id_jenis.equalsIgnoreCase("2")) {
                            cekStudiEkskursi = list.get(i).jumlah;
                        }
                        if (list.get(i).id_jenis.equalsIgnoreCase("3")) {
                            cekPameranProduk = list.get(i).jumlah;
                        }
                        if (list.get(i).id_jenis.equalsIgnoreCase("6")) {
                            cekWorkshop = list.get(i).jumlah;
                        }
                        if (list.get(i).id_jenis.equalsIgnoreCase("13")) {
                            cekKompetisiIlmiah = list.get(i).jumlah;
                        }
                    }
                    if (cekStudiEkskursi >= 1 && cekPameranProduk >= 1 && cekKuliahTamu >= 2 && blog) {
                        int cekSeminarNasional = 0;
                        int cekSeminarInternasional = 0;

                        if (!response.body().error) {
                            for (int i = 0; i < response.body().data.size(); i++) {
                                if (response.body().data.get(i).lingkup.equalsIgnoreCase("Nasional")) {
                                    cekSeminarNasional = response.body().data.get(i).total;
                                }
                                if (response.body().data.get(i).lingkup.equalsIgnoreCase("Internasional")) {
                                    cekSeminarInternasional = response.body().data.get(i).total;
                                }
                            }
                        }

                        if (cekWorkshop >= 4 && cekKompetisiIlmiah >= 1) {
                            if (poin >= 70) {
                                textViewNilai.setText("B");
                            }
                        }

                        if (cekWorkshop >= 2 && cekSeminarNasional >= 1 && cekKompetisiIlmiah >= 1) {
                            if (poin >= 75) {
                                textViewNilai.setText("B+");
                            }
                        }

                        if (cekWorkshop >= 2 && (cekSeminarInternasional >= 1 || cekSeminarNasional >= 2) && cekKompetisiIlmiah >= 2 && eksternal) {
                            if (poin >= 80) {
                                textViewNilai.setText("A");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TotalSeminarResponse> call, Throwable t) {

            }
        });
    }

    public void getTotalEksternal(int poin, ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list) {
        api.getTotalEksternal(firebaseUser.getEmail().substring(0,9)).enqueue(new Callback<TotalEksternalResponse>() {
            @Override
            public void onResponse(Call<TotalEksternalResponse> call, Response<TotalEksternalResponse> response) {
                if (!response.body().error) {
                    getBlog(poin, list, true);
                    //getTotalSeminar(poin, list, true);
                }
            }

            @Override
            public void onFailure(Call<TotalEksternalResponse> call, Throwable t) {

            }
        });
    }

    public void getTotalPoin() {
        api.getTotalPoin(firebaseUser.getEmail().substring(0,9)).enqueue(new Callback<TotalPoinResponse>() {
            @Override
            public void onResponse(Call<TotalPoinResponse> call, Response<TotalPoinResponse> response) {
                if (!response.body().error) {
                    textViewTotalPoin.setText(response.body().data.get(0).poin + " Poin");
                    getPoinMahasiswa(response.body().data.get(0).poin);
                }
            }

            @Override
            public void onFailure(Call<TotalPoinResponse> call, Throwable t) {

            }
        });
    }

    public void getPoinMahasiswa(int poin) {
        api.getPoinMahasiswa(firebaseUser.getEmail().substring(0,9)).enqueue(new Callback<PoinMahasiswaResponse>() {
            @Override
            public void onResponse(Call<PoinMahasiswaResponse> call, Response<PoinMahasiswaResponse> response) {
                if (!response.body().error) {
                    setRecyclerViewJenis(response.body().data);
                    //linearLayoutKosong.setVisibility(View.GONE);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerViewJenis.setVisibility(View.GONE);
                }
                getTotalEksternal(poin, response.body().data);
            }

            @Override
            public void onFailure(Call<PoinMahasiswaResponse> call, Throwable t) {
                if(t instanceof UnknownHostException){
                    //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    //btnRetry.setVisibility(View.VISIBLE);
                }else {
                    t.printStackTrace();
                }
            }
        });
    }

    public void getBlog(int poin, ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list, boolean eksternal) {
        api.getBlog(firebaseUser.getEmail().substring(0,9)).enqueue(new Callback<BlogResponse>() {
            @Override
            public void onResponse(Call<BlogResponse> call, Response<BlogResponse> response) {
                if (!response.body().error) {
                    cardViewBlog.setVisibility(View.VISIBLE);
                    //linearLayoutKosong.setVisibility(View.GONE);
                    textViewJumlahBlog.setText(response.body().data.size() + " Kali");
                    getTotalSeminar(poin, list, eksternal, true);
                }
            }

            @Override
            public void onFailure(Call<BlogResponse> call, Throwable t) {

            }
        });
    }

    public void getDetailMahasiswa() {
        api.getDetailMahasiswa(firebaseUser.getEmail()).enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                if (response.body().error) {
                    textViewNilai.setText(response.body().data.get(0).nilai);
                    textViewStatus.setText(response.body().data.get(0).status);
                    if (response.body().data.get(0).tanggal_validasi != null) {
                        textViewTanggalValidasi.setText(response.body().data.get(0).tanggal_validasi);
                    } else {
                        textViewTanggalValidasi.setText("-");
                    }
                    shimmerFrameLayoutPoin.stopShimmer();
                    shimmerFrameLayoutPoin.setVisibility(View.GONE);
                    cardViewSummary.setVisibility(View.VISIBLE);
                    //buttonAjukanValidasi.setVisibility(View.VISIBLE);

                    getTotalPoin();
                } else {
                    shimmerFrameLayoutPoin.stopShimmer();
                    shimmerFrameLayoutPoin.setVisibility(View.GONE);
                    cardViewSummary.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetailMahasiswa();
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayoutPoin.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayoutPoin.setVisibility(View.VISIBLE);
        //linearLayoutKosong.setVisibility(View.VISIBLE);
        cardViewBlog.setVisibility(View.GONE);
        recyclerViewJenis.setVisibility(View.GONE);
        cardViewSummary.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayoutPoin.stopShimmer();
        super.onPause();
    }
}