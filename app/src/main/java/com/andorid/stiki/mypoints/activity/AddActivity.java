package com.andorid.stiki.mypoints.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andorid.stiki.mypoints.R;

public class AddActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CardView cardViewCatatBlog, cardViewCatatKegiatan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardViewCatatBlog = findViewById(R.id.cardViewCatatBlog);
        cardViewCatatKegiatan = findViewById(R.id.cardViewCatatKegiatan);

        cardViewCatatBlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, AddBlogActivity.class));
            }
        });
        cardViewCatatKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, AddEventActivity.class));
            }
        });
    }
}