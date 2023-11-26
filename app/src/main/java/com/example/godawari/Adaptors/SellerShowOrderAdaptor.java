package com.example.godawari.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.SellerShowOrderModel;
import com.example.godawari.R;

import java.util.ArrayList;

public class SellerShowOrderAdaptor extends RecyclerView.Adapter<SellerShowOrderAdaptor.ViewHolder> {

    ArrayList<SellerShowOrderModel> list=new ArrayList<>();
    Context context;

    public SellerShowOrderAdaptor(ArrayList<SellerShowOrderModel> list,  Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_seller_show_orders,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SellerShowOrderModel model=list.get(position);
        holder.Product.setText(model.getProduct());
        holder.Ram.setText(model.getRam());
        holder.Colour.setText(model.getColour());
        holder.Date.setText(model.getDate());
        holder.Status.setText(model.getStatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public  class  ViewHolder extends RecyclerView.ViewHolder {
        TextView Product,Ram,Colour,Date,Status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Product=itemView.findViewById(R.id.Name);
            Ram=itemView.findViewById(R.id.Ram);
            Colour=itemView.findViewById(R.id.Colour);
            Date=itemView.findViewById(R.id.Date);
            Status=itemView.findViewById(R.id.Status);

        }
    }
}
