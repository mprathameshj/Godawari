package com.example.godawari.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.BuyProductActivity;
import com.example.godawari.Models.BannerModel;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.example.godawari.SellerMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainBannerAdaptor extends RecyclerView.Adapter<MainBannerAdaptor.ViewHolder>{

    ArrayList<BannerModel> list;
    Context context;

    public MainBannerAdaptor(ArrayList<BannerModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_banner,parent,false);
        return new ViewHolder(view);
    }

    //on bind view holder

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BannerModel bannerModel = list.get(position);
        String imageUri = bannerModel.getImage();
        Picasso.get().load(imageUri).into(holder.Image);

        holder.Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Link= bannerModel.getLink();
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("Link",Link);
                context.startActivity(intent); // start the target activityss);
            }

        });
    }

    // /on bind view holder


    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
    ImageView Image;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        Image=itemView.findViewById(R.id.imageView7);
    }
}
}
