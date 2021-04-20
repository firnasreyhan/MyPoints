package com.andorid.stiki.mypoints.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.activity.DetailEventActivity;
import com.andorid.stiki.mypoints.api.response.KegiatanResponse;

import java.util.ArrayList;

public class RecyclerViewEventAdapter extends RecyclerView.Adapter<RecyclerViewEventAdapter.ViewHolder> {
    private ArrayList<KegiatanResponse.KegiatanModel> list;

    public RecyclerViewEventAdapter(ArrayList<KegiatanResponse.KegiatanModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewEventAdapter.ViewHolder holder, int position) {
        KegiatanResponse.KegiatanModel model = list.get(position);

        byte[] decodedString = Base64.decode(model.poster, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.imageViewPoster.setImageBitmap(decodedByte);
        holder.textViewJudul.setText(model.judul);
        holder.textViewDeskripsi.setText(model.deskripsi);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailEventActivity.class);
                intent.putExtra("kegiatan", model);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewPoster;
        private TextView textViewJudul, textViewDeskripsi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewJudul = itemView.findViewById(R.id.textViewJudul);
            textViewDeskripsi = itemView.findViewById(R.id.textViewDeskripsi);
        }
    }
}
