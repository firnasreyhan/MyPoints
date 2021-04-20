package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.adapter.RecyclerViewDetailCategoryAdapter;
import com.andorid.stiki.mypoints.adapter.RecyclerViewEventAdapter;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDetailActivity extends AppCompatActivity {
    private String id_jenis, jenis;

    private Toolbar toolbar;
    private RecyclerView recyclerViewKegiatan;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout liniearLayoutKosong;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textViewJenis;

    private FirebaseUser firebaseUser;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        id_jenis = getIntent().getStringExtra("id_jenis");
        jenis = getIntent().getStringExtra("jenis");

        toolbar = findViewById(R.id.toolbar);
        recyclerViewKegiatan = findViewById(R.id.recyclerViewKegiatan);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        liniearLayoutKosong = findViewById(R.id.liniearLayoutKosong);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        textViewJenis = findViewById(R.id.textViewJenis);
        textViewJenis.setText(jenis);

        setSupportActionBar(toolbar);

        api = ApiClient.getClient();
        getKegiatan();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getKegiatan();
                    }
                }, 3000);
            }
        });
    }

    public void getKegiatan() {
        api.getKegiatanMahasiswa(firebaseUser.getEmail().substring(0,9), id_jenis).enqueue(new Callback<KegiatanMahasiswaResponse>() {
            @Override
            public void onResponse(Call<KegiatanMahasiswaResponse> call, Response<KegiatanMahasiswaResponse> response) {
                if (!response.body().error) {
                    setRecyclerViewKegiatan(response.body().data);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerViewKegiatan.setVisibility(View.GONE);
                    liniearLayoutKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<KegiatanMahasiswaResponse> call, Throwable t) {

            }
        });
    }

    public void setRecyclerViewKegiatan(ArrayList<KegiatanMahasiswaResponse.KegiatanMahasiswaModel> list) {
        recyclerViewKegiatan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKegiatan.setAdapter(new RecyclerViewDetailCategoryAdapter(list));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerViewKegiatan.setVisibility(View.VISIBLE);
        liniearLayoutKosong.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getKegiatan();
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerViewKegiatan.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}