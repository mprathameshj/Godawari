package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.databinding.ActivityUpdateMainBannerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

import kotlin.jvm.internal.CharSpreadBuilder;

public class UpdateMainBannerActivity extends AppCompatActivity {
    ActivityUpdateMainBannerBinding binding;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUpdateMainBannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        dialog=new ProgressDialog(UpdateMainBannerActivity.this);
        dialog.setMessage("Please wait..");


       //delete previous banners
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateMainBannerActivity.this);
                builder.setMessage("Delete all existing banners");
                builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    StorageReference reference=FirebaseStorage.getInstance().getReference().child("MainBanner");
                    reference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for(StorageReference img:listResult.getItems()){
                                img.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(UpdateMainBannerActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();     
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateMainBannerActivity.this, "Failed to load images", Toast.LENGTH_SHORT).show();
                        }
                    });
                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("MainBanner");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot child:snapshot.getChildren()){
                                child.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
                    }
                }).show();

            }
        });


        //Get image from gallary
        binding.imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,80);

            }
        });
        binding.imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateMainBannerActivity.this,SellerMainActivity.class);
                startActivity(intent);
            }
        });



    }
    //Get image from gallary and upload to database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getData()!=null){
            Uri file=data.getData();
            binding.imageView6.setImageURI(file);
            binding.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String link = binding.link.getText().toString();
                    if (file == null || link.isEmpty()) {
                        Toast.makeText(UpdateMainBannerActivity.this, "Image or link is empty", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.show();
                        final String imageId = UUID.randomUUID().toString();
                        final StorageReference reference = storage.getReference().child("MainBanner").child(imageId);
                        reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                Toast.makeText(UpdateMainBannerActivity.this, "Banner uploaded", Toast.LENGTH_SHORT).show();
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String curruntTime = String.valueOf(System.currentTimeMillis());
                                        HashMap<String, Object> hash = new HashMap<>();
                                        hash.put("Image", uri.toString());
                                        hash.put("Link", link);
                                        DatabaseReference databaseReference = database.getReference().child("MainBanner").child(curruntTime);
                                        databaseReference.setValue(hash);
                                    }
                                });
                            }
                        });

                    }
                }
            });
        }
        
    }



}