package com.andorid.stiki.mypoints.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.activity.DetailEventActivity;
import com.andorid.stiki.mypoints.activity.ListDetailActivity;
import com.andorid.stiki.mypoints.api.response.PoinMahasiswaResponse;

import java.util.ArrayList;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.ViewHolder> {
    private ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list;

    public RecyclerViewCategoryAdapter(ArrayList<PoinMahasiswaResponse.PoinMahasiswaModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoryAdapter.ViewHolder holder, int position) {
        PoinMahasiswaResponse.PoinMahasiswaModel model = list.get(position);

        holder.textViewJenis.setText(model.jenis);
        holder.textViewJumlah.setText(model.jumlah + " Kali");
        holder.textViewPoin.setText(model.poin + " Poin");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListDetailActivity.class);
                intent.putExtra("id_jenis", model.id_jenis);
                intent.putExtra("jenis", model.jenis);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewJenis, textViewJumlah, textViewPoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJenis = itemView.findViewById(R.id.textViewJenis);
            textViewJumlah = itemView.findViewById(R.id.textViewJumlah);
            textViewPoin = itemView.findViewById(R.id.textViewPoin);
        }
    }
}
