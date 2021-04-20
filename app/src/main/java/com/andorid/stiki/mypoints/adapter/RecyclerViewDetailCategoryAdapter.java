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
import com.andorid.stiki.mypoints.activity.DetailEventStudentActivity;
import com.andorid.stiki.mypoints.api.response.KegiatanMahasiswaResponse;
import com.andorid.stiki.mypoints.api.response.PoinMahasiswaResponse;

import java.util.ArrayList;

public class RecyclerViewDetailCategoryAdapter extends RecyclerView.Adapter<RecyclerViewDetailCategoryAdapter.ViewHolder> {
    private ArrayList<KegiatanMahasiswaResponse.KegiatanMahasiswaModel> list;

    public RecyclerViewDetailCategoryAdapter(ArrayList<KegiatanMahasiswaResponse.KegiatanMahasiswaModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDetailCategoryAdapter.ViewHolder holder, int position) {
        KegiatanMahasiswaResponse.KegiatanMahasiswaModel model = list.get(position);

        byte[] decodedString = Base64.decode(model.foto, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.imageViewBukti.setImageBitmap(decodedByte);
        holder.textViewJudul.setText(model.judul);
        holder.textViewPembicara.setText(model.pembicara);
        holder.textViewPoin.setText(model.poin + " Poin");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailEventStudentActivity.class);
                intent.putExtra("detail_kegiatan", model);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewBukti;
        private TextView textViewJudul, textViewPembicara, textViewPoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewBukti = itemView.findViewById(R.id.imageViewBukti);
            textViewJudul = itemView.findViewById(R.id.textViewJudul);
            textViewPembicara = itemView.findViewById(R.id.textViewPembicara);
            textViewPoin = itemView.findViewById(R.id.textViewPoin);
        }
    }
}
