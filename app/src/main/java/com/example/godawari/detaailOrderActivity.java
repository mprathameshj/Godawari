package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.Adaptors.ProgressAdaptor;
import com.example.godawari.Models.ProgressModel;
import com.example.godawari.databinding.ActivityDetaailOrderBinding;
import com.example.godawari.databinding.ActivitySearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class detaailOrderActivity extends AppCompatActivity {

    ActivityDetaailOrderBinding binding;
    FirebaseAuth  auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String orderId,Profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityDetaailOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.linear.setVisibility(View.GONE);

        CustomDialog dialog=new CustomDialog(detaailOrderActivity.this);
        dialog.showDialogue1();


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        try {
             orderId = getIntent().getStringExtra("orderId");
             Profile = getIntent().getStringExtra("Profile");
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = String.valueOf(snapshot.child("Product").getValue(String.class));
                    String ram = String.valueOf(snapshot.child("Ram").getValue(String.class));
                    String colour = String.valueOf(snapshot.child("Colour").getValue(String.class));

                    binding.name.setText(name);
                    binding.ram.setText(ram);
                    binding.Colour.setText(colour);
                     binding.orderId.setText("Order id : " + orderId);
                    Picasso.get().load(Profile).into(binding.image);
                    binding.linear.setVisibility(View.VISIBLE);
                    dialog.dismisDialogue();
                }catch (Exception e){
                    Toast.makeText(detaailOrderActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ArrayList<ProgressModel> list=new ArrayList<>();
        list.add(new ProgressModel("Order placed"));
        list.add(new ProgressModel("Order confirmed"));
        list.add(new ProgressModel("Shiped"));
        list.add(new ProgressModel("Delivered"));


        ProgressAdaptor adaptor=new ProgressAdaptor(list,detaailOrderActivity.this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(detaailOrderActivity.this, RecyclerView.VERTICAL,false);
        binding.recyckler.setAdapter(adaptor);
        binding.recyckler.setLayoutManager(layoutManager);


    }
}