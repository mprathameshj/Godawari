package com.example.godawari.Adaptors;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.AddImageModel;
import com.example.godawari.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddImageAdaptor extends RecyclerView.Adapter<AddImageAdaptor.ViewHolder> {

    ArrayList<AddImageModel> list;
    Context context;

    public AddImageAdaptor(ArrayList<AddImageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_add_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddImageModel model=list.get(position);
        String imageUri=model.getImageId();
        Picasso.get().load(imageUri).into(holder.imageView);
        holder.button.setOnClickListener(v -> {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
        });

    }

    public void clear() {
        int size = list.size();
        list.clear();
        notifyItemRangeRemoved(0, size);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView9);
             button=itemView.findViewById(R.id.btn_remove_image);
        }
    }
}
