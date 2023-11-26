package com.example.godawari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.godawari.databinding.ActivityConfirmOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmOrderActivity extends AppCompatActivity {
    ActivityConfirmOrderBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityConfirmOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();



        String OrderId=getIntent().getStringExtra("OrderId");
        binding.textView1.setText("Order id:"+" "+OrderId);


        try {
            LottieAnimationView animationView = findViewById(R.id.animation_view_1);
            animationView.setAnimation(R.raw.confiremed_ok);
            animationView.playAnimation();
        }catch (Exception e){
            Toast.makeText(ConfirmOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            LottieAnimationView animationView = findViewById(R.id.animation_view_2);
            animationView.setAnimation(R.raw.confetti_confirm);
            animationView.playAnimation();
        }catch (Exception e){
            Toast.makeText(ConfirmOrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ConfirmOrderActivity.this,MyOrdersActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(ConfirmOrderActivity.this,userMainActivity.class);
        startActivity(intent);
    }

}