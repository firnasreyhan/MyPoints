package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.adapter.RecyclerViewBlogAdapter;
import com.andorid.stiki.mypoints.adapter.RecyclerViewDetailCategoryAdapter;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBlogActivity extends AppCompatActivity {
    private String nrp;

    private Toolbar toolbar;
    private RecyclerView recyclerViewBlog;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout linearLayoutKosong;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_blog);

        nrp = getIntent().getStringExtra("nrp");

        toolbar = findViewById(R.id.toolbar);
        recyclerViewBlog = findViewById(R.id.recyclerViewBlog);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        linearLayoutKosong = findViewById(R.id.linearLayoutKosong);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        setSupportActionBar(toolbar);

        api = ApiClient.getClient();
        getBlog();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        getBlog();
                    }
                }, 3000);
            }
        });
    }

    public void getBlog() {
        api.getBlog(nrp).enqueue(new Callback<BlogResponse>() {
            @Override
            public void onResponse(Call<BlogResponse> call, Response<BlogResponse> response) {
                if (!response.body().error) {
                    setRecyclerViewBlog(response.body().data);
                } else {
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerViewBlog.setVisibility(View.GONE);
                    linearLayoutKosong.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BlogResponse> call, Throwable t) {

            }
        });
    }

    public void setRecyclerViewBlog(ArrayList<BlogResponse.BlogModel> list) {
        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBlog.setAdapter(new RecyclerViewBlogAdapter(list));

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerViewBlog.setVisibility(View.VISIBLE);
        linearLayoutKosong.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBlog();
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerViewBlog.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}