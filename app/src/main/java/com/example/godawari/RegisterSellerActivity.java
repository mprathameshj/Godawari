package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.databinding.ActivityRegisterSellerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class RegisterSellerActivity extends AppCompatActivity {
    ActivityRegisterSellerBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterSellerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();







        CustomDialog dialog=new CustomDialog(RegisterSellerActivity.this);

      binding.btnRegister.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dialog.showDialogue1();
              DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product");

              reference.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                          if (!productSnapshot.hasChild("Price")) {
                              productSnapshot.getRef().removeValue();
                          }
                      }
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                      // Handle any errors that occur
                  }
              });



          }
      });




    }
}