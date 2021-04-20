package com.andorid.stiki.mypoints.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.api.Api;
import com.andorid.stiki.mypoints.api.ApiClient;
import com.andorid.stiki.mypoints.api.response.BaseResponse;
import com.andorid.stiki.mypoints.api.response.JenisResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;
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

public class DetailEventStudentActivity extends AppCompatActivity {
    private KegiatanMahasiswaResponse.KegiatanMahasiswaModel kegiatanMahasiswaModel;

    private FirebaseUser firebaseUser;
    private Calendar calendar;
    private ProgressDialog progressDialog;

    private CardView cardViewBuktiPendukung;
    private Spinner spinnerJenis, spinnerPeran, spinnerLingkup, spinnerKegiatan;
    private EditText editTextJudul, editTextPembicara, editTextTanggal;
    private ImageView imageViewBuktiPendukung;
    private Button buttonSimpan, buttonBatal, buttonUbah, buttonHapus;
    private Toolbar toolbar;
    private LinearLayout linearLayoutUbahHapus, linearLayoutBatalSimpan;
    private NestedScrollView nestedScrollView;

    private ArrayList<JenisResponse.JenisModel> jenisModelArrayList;
    private ArrayList<PeranResponse.PeranModel> peranModelArrayList;
    private ArrayList<LingkupResponse.LingkupModel> lingkupModelArrayList;
    private ArrayList<PoinResponse.PoinModel> poinModelArrayList;

    private Api api;

    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event_student);

        kegiatanMahasiswaModel = (KegiatanMahasiswaResponse.KegiatanMahasiswaModel) getIntent().getSerializableExtra("detail_kegiatan");

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
        buttonHapus = findViewById(R.id.buttonHapus);
        buttonBatal = findViewById(R.id.buttonBatal);
        buttonUbah = findViewById(R.id.buttonUbah);
        cardViewBuktiPendukung = findViewById(R.id.cardViewBuktiPendukung);
        linearLayoutUbahHapus = findViewById(R.id.linearLayoutUbahHapus);
        linearLayoutBatalSimpan = findViewById(R.id.linearLayoutBatalSimpan);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        api = ApiClient.getClient();

        setData();
        getPoin();

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
                        DetailEventStudentActivity.this, date,
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
                        .start(DetailEventStudentActivity.this);
            }
        });

        buttonUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerJenis.setEnabled(true);
                spinnerPeran.setEnabled(true);
                spinnerLingkup.setEnabled(true);
                spinnerKegiatan.setEnabled(true);
                editTextJudul.setEnabled(true);
                editTextPembicara.setEnabled(true);
                editTextTanggal.setEnabled(true);
                cardViewBuktiPendukung.setEnabled(true);
                linearLayoutBatalSimpan.setVisibility(View.VISIBLE);
                linearLayoutUbahHapus.setVisibility(View.GONE);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                nestedScrollView.smoothScrollTo(0,0);
            }
        });

        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailEventStudentActivity.this)
                        .setMessage("Apakah anda yakin ingin mengahpus data ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Tunggu Sebentar...");
                                progressDialog.show();

                                api.deleteTugasKhusus(kegiatanMahasiswaModel.id_tugas_khusus).enqueue(new Callback<BaseResponse>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        if (!response.body().error) {
                                            finish();
                                        }
                                        Toast.makeText(DetailEventStudentActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
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

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setData();
                linearLayoutBatalSimpan.setVisibility(View.GONE);
                linearLayoutUbahHapus.setVisibility(View.VISIBLE);
                nestedScrollView.fullScroll(View.FOCUS_UP);
                nestedScrollView.smoothScrollTo(0,0);
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailEventStudentActivity.this)
                        .setMessage("Apakah anda yakin ingin menyimpan data ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.setMessage("Tunggu Sebentar...");
                                progressDialog.show();

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
                                api.updateTugasKhusus(
                                        kegiatanMahasiswaModel.id_tugas_khusus,
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
                                        Toast.makeText(DetailEventStudentActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<BaseResponse> call, Throwable t) {

                                    }
                                });
                                spinnerJenis.setEnabled(false);
                                spinnerPeran.setEnabled(false);
                                spinnerLingkup.setEnabled(false);
                                spinnerKegiatan.setEnabled(false);
                                editTextJudul.setEnabled(false);
                                editTextPembicara.setEnabled(false);
                                editTextTanggal.setEnabled(false);
                                cardViewBuktiPendukung.setEnabled(false);

                                linearLayoutUbahHapus.setVisibility(View.VISIBLE);
                                linearLayoutBatalSimpan.setVisibility(View.GONE);
                                nestedScrollView.fullScroll(View.FOCUS_UP);
                                nestedScrollView.smoothScrollTo(0,0);
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
        setSpinnerJenis();
        setSpinnerKegiatan();
        spinnerJenis.setEnabled(false);
        spinnerPeran.setEnabled(false);
        spinnerLingkup.setEnabled(false);
        spinnerKegiatan.setEnabled(false);
        editTextJudul.setText(kegiatanMahasiswaModel.judul);
        editTextJudul.setEnabled(false);
        editTextPembicara.setText(kegiatanMahasiswaModel.pembicara);
        editTextPembicara.setEnabled(false);
        editTextTanggal.setText(kegiatanMahasiswaModel.tanggal_acara);
        editTextTanggal.setEnabled(false);
        cardViewBuktiPendukung.setEnabled(false);

        encodedImage = kegiatanMahasiswaModel.foto;
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageViewBuktiPendukung.setImageBitmap(decodedByte);
    }

    public void setSpinnerJenis() {
        api.getJenis().enqueue(new Callback<JenisResponse>() {
            @Override
            public void onResponse(Call<JenisResponse> call, Response<JenisResponse> response) {
                if (!response.body().error) {
                    jenisModelArrayList = response.body().data;
                    ArrayList<String> list = new ArrayList<>();
                    int index = 0;
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).jenis);
                        if (response.body().data.get(i).jenis.equalsIgnoreCase(kegiatanMahasiswaModel.jenis)) {
                            index = i;
                        }
                        //jenisModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(DetailEventStudentActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
                    spinnerJenis.setAdapter(spinnerArrayAdapter);
                    spinnerJenis.setSelection(index);
                }
            }

            @Override
            public void onFailure(Call<JenisResponse> call, Throwable t) {

            }
        });
    }

    public void getPoin() {
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
    }

    public void setSpinnerPeran(String jenis) {
        api.getPeran(jenis).enqueue(new Callback<PeranResponse>() {
            @Override
            public void onResponse(Call<PeranResponse> call, Response<PeranResponse> response) {
                if (!response.body().error) {
                    peranModelArrayList = response.body().data;
                    int index = 0;
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).peran);
                        if (response.body().data.get(i).peran.equalsIgnoreCase(kegiatanMahasiswaModel.peran)) {
                            index = i;
                        }
                        //peranModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(DetailEventStudentActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
                    spinnerPeran.setAdapter(spinnerArrayAdapter);
                    spinnerPeran.setSelection(index);
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
                    int index = 0;
                    for (int i = 0; i < response.body().data.size(); i++) {
                        list.add(response.body().data.get(i).lingkup);
                        if (response.body().data.get(i).lingkup.equalsIgnoreCase(kegiatanMahasiswaModel.lingkup)) {
                            index = i;
                        }
                        //lingkupModelArrayList.add(response.body().data.get(i));
                    }
                    ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(DetailEventStudentActivity.this, R.layout.item_spinner, list);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
                    spinnerLingkup.setAdapter(spinnerArrayAdapter);
                    spinnerLingkup.setSelection(index);
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
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter<>(DetailEventStudentActivity.this, R.layout.item_spinner, list);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerKegiatan.setAdapter(spinnerArrayAdapter);
        if (kegiatanMahasiswaModel.kegiatan.equalsIgnoreCase("Internal")) {
            spinnerKegiatan.setSelection(0);
        } else {
            spinnerKegiatan.setSelection(1);
        }
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