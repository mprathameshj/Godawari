package com.example.godawari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.godawari.databinding.ActivitySellerMainBinding;

public class SellerMainActivity extends AppCompatActivity {
    ActivitySellerMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnProduct.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(SellerMainActivity.this, SellerShowProductActivity.class);
                 startActivity(intent);
             }
         });
        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerMainActivity.this, SellerAddProductActivity.class);
                startActivity(intent);
            }
        });
        binding.Banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerMainActivity.this, UpdateMainBannerActivity.class);
                startActivity(intent);
            }
        });

        binding.btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SellerMainActivity.this, SellerShowOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}