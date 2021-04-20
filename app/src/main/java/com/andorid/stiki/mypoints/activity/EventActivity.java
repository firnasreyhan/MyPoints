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

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.adapter.RecyclerViewEventAdapter;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerViewKegiatan;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout liniearLayoutKosong;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        toolbar = findViewById(R.id.toolbar);
        recyclerViewKegiatan = findViewById(R.id.recyclerViewKegiatan);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        liniearLayoutKosong = findViewById(R.id.liniearLayoutKosong);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = ApiClient.getClient();

        getKegiatan();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        shimmerFrameLayout.startShimmer();
                        shimmerFrameLayout.setVisibility(View.VISIBLE);
                        recyclerViewKegiatan.setVisibility(View.GONE);
                        liniearLayoutKosong.setVisibility(View.GONE);
                        getKegiatan();
                    }
                }, 3000);
            }
        });
    }

    public void getKegiatan() {
        api.getKegiatan().enqueue(new Callback<KegiatanResponse>() {
            @Override
            public void onResponse(Call<KegiatanResponse> call, Response<KegiatanResponse> response) {
                if (!response.body().error) {
                    setRecyclerViewKegiatan(response.body().data);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    liniearLayoutKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<KegiatanResponse> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void setRecyclerViewKegiatan(ArrayList<KegiatanResponse.KegiatanModel> list) {
        recyclerViewKegiatan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewKegiatan.setAdapter(new RecyclerViewEventAdapter(list));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerViewKegiatan.setVisibility(View.VISIBLE);
        liniearLayoutKosong.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerViewKegiatan.setVisibility(View.GONE);
        liniearLayoutKosong.setVisibility(View.GONE);
        getKegiatan();
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}