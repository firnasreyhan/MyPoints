package com.andorid.stiki.mypoints.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.andorid.stiki.mypoints.api.response.JenisResponse;
import com.andorid.stiki.mypoints.api.response.LingkupResponse;
import com.andorid.stiki.mypoints.api.response.PeranResponse;
import com.andorid.stiki.mypoints.api.response.PoinResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventActivity extends AppCompatActivity {
    private FirebaseUser firebaseUser;
    private Calendar calendar;
    private ProgressDialog progressDialog;

    private CardView cardViewBuktiPendukung;
    private Spinner spinnerJenis, spinnerPeran, spinnerLingkup, spinnerKegiatan;
    private EditText editTextJudul, editTextPembicara, editTextTanggal;
    private ImageView imageViewBuktiPendukung;
    private Button buttonSimpan;
    private Toolbar toolbar;

    private ArrayList<JenisResponse.JenisModel> jenisModelArrayList;
    private ArrayList<PeranResponse.PeranModel> peranModelArrayList;
    private ArrayList<LingkupResponse.LingkupModel> lingkupModelArrayList;
    private ArrayList<PoinResponse.PoinModel> poinModelArrayList;

    private Api api;

    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        calendar = Calendar.getInstance();
        progressDialog = new ProgressDialog(this);

        spinnerJenis = findViewById(R.id.spinnerJenis);
        spinnerPeran = findViewById(R.id.spinnerPeran);
        spinnerLingkup = findViewById(R.id.spinnerLingkup);
        spinnerKegiatan = findViewById(R.id.spinnerKegiatan);
        editTextJudul = findViewById(R.id.editTextJudul);
        editTextPembicara = findViewById(R.id.editTextPembicara);
        editTextTanggal = findViewById(R.id.editTextTanggal);
        imageViewBuktiPendukung = findViewById(R.id.imageViewBuktiPendukung);
        buttonSimpan = findViewById(R.id.buttonSimpan);
        cardViewBuktiPendukung = findViewById(R.id.cardViewBuktiPendukung);

        api = ApiClient.getClient();

        api.getPoin().enqueue(new Callback<PoinResponse>() {
            @Override
            public void onResponse(Call<PoinResponse> call, Response<PoinResponse> response) {
                if (!response.body().error) {
                    poinModelArrayList = response.body().data;
                }
            }

            @Override
            public void onFailure(Call<PoinResponse> call, Throwable t) {

            }
        });

        setSpinnerJenis();
        setSpinnerKegiatan();
        spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpinnerPeran(jenisModelArrayList.get(position).id_jenis);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPeran.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PostActivity.this, "ABC", Toast.LENGTH_SHORT).show();
                setSpinnerLingkup(jenisModelArrayList.get(spinnerJenis.getSelectedItemPosition()).id_jenis, peranModelArrayList.get(position).id_peran);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editTextTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        AddEventActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        cardViewBuktiPendukung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AddEventActivity.this);
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddEventActivity.this)
                        .setMessage("Apakah anda yakin ingin menambahkan data ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Tunggu Sebentar...");
                                progressDialog.show();

                                String nrp = firebaseUser.getEmail().substring(0,9);
                                String id_poin = "";
                                for (int i = 0; i < poinModelArrayList.size(); i++) {
                                    if (poinModelArrayList.get(i).id_jenis.equalsIgnoreCase(jenisModelArrayList.get(spinnerJenis.getSelectedItemPosition()).id_jenis)) {
                                        if (poinModelArrayList.get(i).id_peran.equalsIgnoreCase(peranModelArrayList.get(spinnerPeran.getSelectedItemPosition()).id_peran)) {
                                            if (poinModelArrayList.get(i).id_lingkup.equalsIgnoreCase(lingkupModelArrayList.get(spinnerLingkup.getSelectedItemPosition()).id_lingkup)) {
                                                id_poin = poinModelArrayList.get(i).id_poin;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!editTextJudul.getText().toString().isEmpty() && !editTextPembicara.getText().toString().isEmpty() && !editTextTanggal.getText().toString().isEmpty() && encodedImage != null) {
                                    api.postTugasKhusus(
                                            nrp,
                                            id_poin,
                                            editTextJudul.getText().toString(),
                                            editTextPembicara.getText().toString(),
                                            spinnerKegiatan.getSelectedItem().toString(),
                                            editTextTanggal.getText().toString(),
                                            encodedImage).enqueue(new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                            if (progressDialog.isShowing()) {
                                                progressDialog.dismiss();
                                            }
                                            Toast.makeText(AddEventActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                            final Intent intent = new Intent(AddEventActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                                            Log.e("message", t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(AddEventActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                                }
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

    public void setSpinnerJenis() {
        api.getJenis().enqueue(new Callback<JenisResponse>() {
            @Override
            public void onResponse(Call<JenisResponse> call, Response<JenisResponse> response) {
                if (!response.body().error) {
                    jenisModelArrayList = response.body().data;
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).jenis);
                        //jenisModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(AddEventActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);

                    spinnerJenis.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<JenisResponse> call, Throwable t) {

            }
        });
    }

    public void setSpinnerPeran(String jenis) {
        api.getPeran(jenis).enqueue(new Callback<PeranResponse>() {
            @Override
            public void onResponse(Call<PeranResponse> call, Response<PeranResponse> response) {
                if (!response.body().error) {
                    peranModelArrayList = response.body().data;
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).peran);
                        //peranModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(AddEventActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);

                    spinnerPeran.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<PeranResponse> call, Throwable t) {

            }
        });
    }

    public void setSpinnerLingkup(String jenis, String peran) {
        api.getLingkup(jenis, peran).enqueue(new Callback<LingkupResponse>() {
            @Override
            public void onResponse(Call<LingkupResponse> call, Response<LingkupResponse> response) {
                if (!response.body().error) {
                    lingkupModelArrayList = response.body().data;
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).lingkup);
                        //lingkupModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(AddEventActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);

                    spinnerLingkup.setAdapter(spinnerArrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<LingkupResponse> call, Throwable t) {

            }
        });
    }

    public void setSpinnerKegiatan() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Internal");
        list.add("Eksternal");
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(AddEventActivity.this, R.layout.item_spinner, list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);

        spinnerKegiatan.setAdapter(spinnerArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(resultUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Bitmap resizeImage = scaleBitmap(selectedImage);
                    encodedImage = encodeImage(resizeImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageViewBuktiPendukung.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private Bitmap scaleBitmap(Bitmap bm) {
        int maxWidth = 640;
        int maxHeight = 480;
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextTanggal.setText(sdf.format(calendar.getTime()));
    }
}