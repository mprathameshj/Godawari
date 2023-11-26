package com.example.godawari.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.BuyProductActivity;
import com.example.godawari.Models.MyOrdersModel;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.example.godawari.detaailOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyOrdersAdaptor extends RecyclerView.Adapter<MyOrdersAdaptor.ViewHolder> {
    ArrayList<MyOrdersModel> list=new ArrayList<>();
    Context context;

    public MyOrdersAdaptor(ArrayList<MyOrdersModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_my_orders,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyOrdersModel model=list.get(position);
        holder.Name.setText(model.getName());
        holder.Status.setText(model.getStatus());
        String id=model.getProfile();
        Picasso.get().load(id).into(holder.Image);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, detaailOrderActivity.class);
                intent.putExtra("orderId",model.getOrderId());
                intent.putExtra("Profile",model.getProfile());
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
        TextView Name,Status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.imageOrder);
            Name=itemView.findViewById(R.id.orderName);
            Status=itemView.findViewById(R.id.orderStatus);
        }
    }
}
