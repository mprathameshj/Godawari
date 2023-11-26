package com.example.godawari.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.CategoryModel;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.ViewHolder> {

    ArrayList<CategoryModel> list=new ArrayList<>();
    Context context;

    public CategoryAdaptor(ArrayList<CategoryModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryModel model=list.get(position);
       holder.Image.setImageResource(model.getImage());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(context, SearchActivity.class);
               intent.putExtra("Id",model.getId());
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.image_6);
        }
    }
}
