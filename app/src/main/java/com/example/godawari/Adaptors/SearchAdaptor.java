package com.example.godawari.Adaptors;

import com.example.godawari.BuyProductActivity;
import com.example.godawari.Models.SearchModel;
import com.example.godawari.R;
import com.example.godawari.SellerEditProductActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class SearchAdaptor extends RecyclerView.Adapter<SearchAdaptor.ViewHolder> {

    public ArrayList<SearchModel> list;
    public Context context;
    NumberFormat numberFormat = NumberFormat.getInstance();



    public SearchAdaptor(ArrayList<SearchModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_search_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchModel model=list.get(position);
        holder.Name.setText(model.getName());
        holder.RamDefault.setText(model.getRamDefault());
        holder.Battery.setText(model.getBattery());
        holder.Display.setText(model.getDisplay());


        // Apply strike through formatting to the RetailPrice field
        double retailPrice=Double.parseDouble(model.getRetailPrice());
        String retailPriceText =numberFormat.format(retailPrice);
        SpannableStringBuilder spannable = new SpannableStringBuilder(retailPriceText);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannable.setSpan(strikethroughSpan, 0, retailPriceText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.RetailPrice.setText(spannable);

        // Format the Price field with commas
        double pr = Double.parseDouble(model.getPrice());
        holder.Price.setText("\u20B9" + numberFormat.format(pr));
        String price= String.valueOf(pr);

        //find discount
        int p = Integer.parseInt(model.getPrice());
        int rp = Integer.parseInt(model.getRetailPrice());
        double discount_percentage = ((rp - p) / (double) rp) * 100;
        String discount = String.format("%.0f", discount_percentage);
        holder.Discount.setText(discount + "% off");


        holder.Discount.setText(discount+"% off");
        holder.Camera.setText(model.getCamera());
        holder.Rating.setRating(4.5F);
        holder.SelfieCamera.setText(model.getSelfieCamera());
        holder.Note.setText(model.getNote());
        String imageUri=model.getProfile();
        Picasso.get().load(imageUri).into(holder.Profile);

        //Go to Buy product activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String Id = model.getId();
                    Intent intent = new Intent(context, BuyProductActivity.class);
                    intent.putExtra("id", Id);
                    intent.putExtra("Reference","ok");
                    intent.putExtra("price", price);
                    intent.putExtra("retailPriceText", model.getRetailPrice());
                    intent.putExtra("Discount", discount);
                    intent.putExtra("Name", model.getName());
                    context.startActivity(intent); // start the target activityss);
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name,RamDefault,Battery,Display,RetailPrice,Price,Camera,Discount,SelfieCamera,Note;
        ImageView Profile;
        RatingBar Rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.name);
            RamDefault=itemView.findViewById(R.id.Ram);
            Battery=itemView.findViewById(R.id.Battery);
            RetailPrice=itemView.findViewById(R.id.CutPrice);
            Display=itemView.findViewById(R.id.Display);
            Price=itemView.findViewById(R.id.SellingPrice);
            Camera=itemView.findViewById(R.id.Camera);
            Discount=itemView.findViewById(R.id.Off);
            Profile=itemView.findViewById(R.id.Image);
            Rating=itemView.findViewById(R.id.rate);
            SelfieCamera=itemView.findViewById(R.id.FrontCamera);
            Note=itemView.findViewById(R.id.Note);

        }
    }
}
