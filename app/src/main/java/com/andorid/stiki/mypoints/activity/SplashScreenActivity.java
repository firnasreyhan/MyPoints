package com.andorid.stiki.mypoints.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.andorid.stiki.mypoints.api.response.MahasiswaResponse;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends Activity {
    private static final int RC_SIGN_IN = 123;

    private Api api;

    private FirebaseUser firebaseUser;
    private AuthMethodPickerLayout authMethodPickerLayout;
    private List<AuthUI.IdpConfig> providers;

    private Button buttonCobaLagi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        buttonCobaLagi = findViewById(R.id.buttonCobaLagi);

        api = ApiClient.getClient();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());

        authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.firbase_login)
                .setGoogleButtonId(R.id.signInButton)
                .setPhoneButtonId(R.id.signInButton1)
                .build();

        buttonCobaLagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firebaseUser == null) {
                    doSignIn();
                } else {
                    api.getDetailMahasiswa(firebaseUser.getEmail()).enqueue(new Callback<MahasiswaResponse>() {
                        @Override
                        public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                            if(t instanceof UnknownHostException){
                                Toast.makeText(getApplicationContext(), "Terjadi kesalahan pada jaringan.", Toast.LENGTH_SHORT).show();
                                buttonCobaLagi.setVisibility(View.VISIBLE);
                            }else {
                                t.printStackTrace();
                            }
                        }
                    });
                }
            }
        }, 3000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.getEmail() != null) {
                        if (user.getEmail().contains("@mhs.stiki.ac.id")) {
                            String nrp = user.getEmail().substring(0,9);
                            String nama = user.getDisplayName().substring(10);
                            String email = user.getEmail();
                            String prodi = "";
                            if (nrp.substring(2,5).equals("111")) {
                                prodi = "TI";
                            } else if (nrp.substring(2,5).equals("122")) {
                                prodi = "MI";
                            } else if (nrp.substring(2,5).equals("113")) {
                                prodi = "SI";
                            } else if (nrp.substring(2,5).equals("211")) {
                                prodi = "DKV";
                            }
                            String angkatan = "20" + nrp.substring(0,2);

                            addMahasiswa(nrp,nama,email,prodi,angkatan);
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            finish();
                        } else {
                            deleteAccount();
                        }
                    }
                }
            } else {
                if (response == null) {
                    finish();
                }
            }
        }
    }

    public void doSignIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setTheme(R.style.FirebaseLoginAppTheme)
                        .setAuthMethodPickerLayout(authMethodPickerLayout)
                        .build(),
                RC_SIGN_IN);
    }

    public void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SplashScreenActivity.this, "Mohon gunakan email mahasiswa anda.", Toast.LENGTH_SHORT).show();
                        doSignIn();
                    }
                });
    }

    public void addMahasiswa(String nrp, String nama, String email, String prodi, String angkatan) {
        api.getDetailMahasiswa(email).enqueue(new Callback<MahasiswaResponse>() {
            @Override
            public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                if (!response.body().error) {
                    api.postMahasiswa(nrp,nama,email,prodi,angkatan).enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            Log.e("message", response.body().message);
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Log.e("error", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                Log.e("error", t.getMessage());
                if(t instanceof UnknownHostException){
                    Toast.makeText(getApplicationContext(), "Terjadi kesalahan pada jaringan.", Toast.LENGTH_SHORT).show();
                    buttonCobaLagi.setVisibility(View.VISIBLE);
                }else {
                    t.printStackTrace();
                }
            }
        });
    }
}