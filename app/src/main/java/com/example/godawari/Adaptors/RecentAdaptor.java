package com.example.godawari.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.RecentModel;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecentAdaptor extends RecyclerView.Adapter<RecentAdaptor.ViewHolder> {

    ArrayList<RecentModel> list=new ArrayList<>();
    Context context;

    public RecentAdaptor(ArrayList<RecentModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_recent,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecentModel model=list.get(position);
        holder.Name.setText(model.getName());
        String uri= model.getProfile();
        Picasso picasso = Picasso.get();
        // Enable memory caching
        //picasso.setIndicatorsEnabled(true); // Optional: Show indicators for debugging purposes
        picasso.setLoggingEnabled(true); // Optional: Enable logging for debugging purposes

        picasso.load(uri)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) // Disable memory caching
                .networkPolicy(NetworkPolicy.OFFLINE) // Load from cache only
                .into(holder.Image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, SearchActivity.class);
                intent.putExtra("Recent",model.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        TextView Name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.Image_Recent);
            Name=itemView.findViewById(R.id.Name_recent);
        }
    }
}
