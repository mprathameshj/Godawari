package com.example.godawari.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.SubBannerModel;
import com.example.godawari.R;

import java.util.ArrayList;

public class SubBannerAdaptor extends RecyclerView.Adapter<SubBannerAdaptor.ViewHolder> {

    ArrayList<SubBannerModel> list=new ArrayList<>();
    Context context;

    public SubBannerAdaptor(ArrayList<SubBannerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sub_banner,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
   SubBannerModel  model=list.get(position);
    holder.image.setImageResource(model.getPic());
    holder.text.setText(model.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imageview8);
            text=itemView.findViewById(R.id.textView2);
        }
    }
}
