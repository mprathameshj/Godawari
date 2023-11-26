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

import com.example.godawari.Models.SellerShowProductModel;
import com.example.godawari.R;
import com.example.godawari.SellerEditProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SellerShowProductAdaptor extends RecyclerView.Adapter<SellerShowProductAdaptor.ViewHolder> {

    public ArrayList<SellerShowProductModel> list;
    public ArrayList<SellerShowProductModel> filteredList;
    private Context context;

    public SellerShowProductAdaptor(ArrayList<SellerShowProductModel> list, Context context) {
        this.list = list;
        this.filteredList = new ArrayList<>(list);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.seller_show_product,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    SellerShowProductModel product=filteredList.get(position);
    holder.name.setText(product.getName());
    holder.price.setText("\u20B9"+product.getPrice());
    String imageuri= product.getProfile();
        Picasso.get().load(imageuri).into(holder.image);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Id= product.getId();
                Intent intent = new Intent(context, SellerEditProductActivity.class);
                intent.putExtra("id", Id); // pass the product ID as an extra to the target activity
                context.startActivity(intent); // start the target activityss);
            }
        });


    }

    @Override
    public int getItemCount() {
       return filteredList.size();
    }

    //new code
    public void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(list);
        } else {
            for (SellerShowProductModel product : list) {
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
    //end of new code


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView image,btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            image=itemView.findViewById(R.id.image);
            btn=itemView.findViewById(R.id.btn_editProduct);

        }
    }
}
