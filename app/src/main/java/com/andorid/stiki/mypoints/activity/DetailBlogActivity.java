package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBlogActivity extends AppCompatActivity {
    private BlogResponse.BlogModel blogModel;
    private FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;
    private EditText editTextJudul, editTextURL;
    private Button buttonSimpan, buttonUbah, buttonHapus, buttonBatal;
    private LinearLayout linearLayoutUbahHapus, linearLayoutBatalSimpan;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_blog);

        blogModel = (BlogResponse.BlogModel) getIntent().getSerializableExtra("blog");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        api = ApiClient.getClient();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        editTextJudul = findViewById(R.id.editTextJudul);
        editTextURL = findViewById(R.id.editTextURL);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        buttonHapus = findViewById(R.id.buttonHapus);
        buttonBatal = findViewById(R.id.buttonBatal);
        buttonUbah = findViewById(R.id.buttonUbah);
        linearLayoutBatalSimpan = findViewById(R.id.linearLayoutBatalSimpan);
        linearLayoutUbahHapus = findViewById(R.id.linearLayoutUbahHapus);

        setData();

        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailBlogActivity.this)
                        .setMessage("Apakah anda yakin ingin mengahpus data ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Tunggu Sebentar...");
                                progressDialog.show();

                                api.deleteBlog(blogModel.id_blog).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        if (!response.body().error) {
                                            finish();
                                        }
                                        Toast.makeText(DetailBlogActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                                    }
                                });
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

        buttonUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextJudul.setEnabled(true);
                editTextURL.setEnabled(true);
                editTextJudul.setText(blogModel.judul);
                editTextURL.setText(blogModel.url);
                linearLayoutBatalSimpan.setVisibility(View.VISIBLE);
                linearLayoutUbahHapus.setVisibility(View.GONE);
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                linearLayoutBatalSimpan.setVisibility(View.GONE);
                linearLayoutUbahHapus.setVisibility(View.VISIBLE);
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailBlogActivity.this)
                        .setMessage("Apakah anda yakin ingin menyimpan data ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Tunggu Sebentar...");
                                progressDialog.show();

                                api.updateBlog(
                                        blogModel.id_blog,
                                        editTextJudul.getText().toString(),
                                        editTextURL.getText().toString()
                                ).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        Toast.makeText(DetailBlogActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                                    }
                                });
                                editTextJudul.setEnabled(false);
                                editTextURL.setEnabled(false);

                                linearLayoutUbahHapus.setVisibility(View.VISIBLE);
                                linearLayoutBatalSimpan.setVisibility(View.GONE);
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
    }

    public void setData() {
        editTextJudul.setText(blogModel.judul);
        editTextURL.setText(blogModel.url);
        editTextJudul.setEnabled(false);
        editTextURL.setEnabled(false);
    }
}