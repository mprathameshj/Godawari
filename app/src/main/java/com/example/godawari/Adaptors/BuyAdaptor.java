package com.example.godawari.Adaptors;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.BuyModel;
import com.example.godawari.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyAdaptor extends RecyclerView.Adapter<BuyAdaptor.ViewHolder> {

    ArrayList<BuyModel> list=new ArrayList<>();
    Context context;

    public BuyAdaptor(ArrayList<BuyModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_buy_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyModel model=list.get(position);
        String imageUri=model.getImage();
        Picasso.get().load(imageUri).into(holder.Image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.image_buy);
        }
    }
}
