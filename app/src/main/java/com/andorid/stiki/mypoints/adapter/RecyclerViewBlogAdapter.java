package com.andorid.stiki.mypoints.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andorid.stiki.mypoints.R;
import com.andorid.stiki.mypoints.activity.DetailBlogActivity;
import com.andorid.stiki.mypoints.activity.DetailEventStudentActivity;
import com.andorid.stiki.mypoints.activity.ListDetailActivity;
import com.andorid.stiki.mypoints.api.response.BlogResponse;
import com.andorid.stiki.mypoints.api.response.PoinMahasiswaResponse;

import java.util.ArrayList;

public class RecyclerViewBlogAdapter extends RecyclerView.Adapter<RecyclerViewBlogAdapter.ViewHolder> {
    private ArrayList<BlogResponse.BlogModel> list;

    public RecyclerViewBlogAdapter(ArrayList<BlogResponse.BlogModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBlogAdapter.ViewHolder holder, int position) {
        BlogResponse.BlogModel model = list.get(position);

        holder.textViewJudul.setText(model.judul);
        holder.textViewUrl.setText(model.url);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailBlogActivity.class);
                intent.putExtra("blog", model);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewJudul, textViewUrl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJudul = itemView.findViewById(R.id.textViewJudul);
            textViewUrl = itemView.findViewById(R.id.textViewUrl);
        }
    }
}
