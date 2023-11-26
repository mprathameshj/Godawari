package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

import com.example.godawari.Adaptors.ChangeAddressAdaptor;
import com.example.godawari.Models.ChangeAddressModel;
import com.example.godawari.databinding.ActivityChangeAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragments.AddAddressDialogueFragment;

public class ChangeAddressActivity extends AppCompatActivity {
    ActivityChangeAddressBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChangeAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ArrayList<ChangeAddressModel> list=new ArrayList<>();
        List<ChangeAddressModel> data;


        binding.textView17.setVisibility(View.GONE);
        binding.BtnAddAddress.setVisibility(View.GONE);
        binding.recyckler.setVisibility(View.GONE);

        getSupportActionBar().hide();

        ChangeAddressAdaptor adaptor=new ChangeAddressAdaptor(list,ChangeAddressActivity.this);
        binding.recyckler.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(ChangeAddressActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.recyckler.setLayoutManager(layoutManager);





        ProgressBar progressBar = findViewById(R.id.progressBar2);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(this, R.color.blue2));
        progressBar.setIndeterminateDrawable(circularProgressDrawable);
        progressBar.setVisibility(View.VISIBLE);







        //Back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //End of back button

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Address");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot addressSnap: snapshot.getChildren()){
                    ChangeAddressModel model=addressSnap.getValue(ChangeAddressModel.class);
                    model.setId(addressSnap.getKey());
                    list.add(model);
                }
                adaptor.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                binding.textView17.setVisibility(View.VISIBLE);
                binding.BtnAddAddress.setVisibility(View.VISIBLE);
                binding.recyckler.setVisibility(View.VISIBLE);
                binding.textView0.setVisibility(View.GONE);

                if(adaptor.getItemCount()==0||list.size()==0){
                    binding.BtnAddAddress.setVisibility(View.VISIBLE);
                    binding.textView17.setVisibility(View.GONE);
                    binding.textView0.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.BtnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAddressDialogueFragment fragment = new AddAddressDialogueFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });








    }
}