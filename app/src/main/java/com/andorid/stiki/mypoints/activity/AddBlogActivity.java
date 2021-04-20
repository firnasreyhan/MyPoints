package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;

    private Toolbar toolbar;
    private EditText editTextJudul, editTextURL;
    private Button buttonSimpan;

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);

        progressDialog = new ProgressDialog(this);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        api = ApiClient.getClient();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextJudul = findViewById(R.id.editTextJudul);
        editTextURL = findViewById(R.id.editTextURL);
        buttonSimpan = findViewById(R.id.buttonSimpan);

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddBlogActivity.this)
                        .setMessage("Apakah anda yakin ingin menyimpan blog ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setTitle("Tunggu Sebentar...");
                                progressDialog.show();

                                api.postBlog(
                                        firebaseUser.getEmail().substring(0,9),
                                        editTextJudul.getText().toString(),
                                        editTextURL.getText().toString()).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (!response.body().error) {
                                            final Intent intent = new Intent(AddBlogActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                        Toast.makeText(AddBlogActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                                        Log.e("error", t.getMessage());
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
    }
}