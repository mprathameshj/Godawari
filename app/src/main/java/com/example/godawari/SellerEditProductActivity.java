package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.databinding.ActivitySellerEditProductBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class SellerEditProductActivity extends AppCompatActivity {
    ActivitySellerEditProductBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<String> getRam=new ArrayList();
    ArrayList<String> colour=new ArrayList();
    HashMap<String, Object> RamPrice =new HashMap<>();
    HashMap<String,HashMap<String,Object>> Ram=new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ProgressDialog dialog;

        dialog=new ProgressDialog(SellerEditProductActivity.this,R.style.myDialogue);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(4f);
        circularProgressDrawable.setCenterRadius(24f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(this, R.color.pumpkin_orange));
        circularProgressDrawable.start();
        dialog.setIndeterminateDrawable(circularProgressDrawable);


        CustomDialog dialog1=new CustomDialog(SellerEditProductActivity.this);

        String Id = getIntent().getStringExtra("id");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product").child(Id);

        //set as new arrival
        binding.btnSetNewArriwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.showDialogue();
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("New");
                reference1.child("Product").setValue(Id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog1.dismisDialogue();
                        Toast.makeText(SellerEditProductActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //end of set as new arrival


        //Get Ram And Name
        DatabaseReference ramRef=reference.child("Ram");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.etProductName.setText((CharSequence) snapshot.child("Name").getValue());
                binding.etDefaultPrice.setText((CharSequence) snapshot.child("Price").getValue());
                binding.availSwitchProduct.setChecked(snapshot.child("Available").getValue(Boolean.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // end of Get Ram And Name


        // Set price according to the ram
        binding.btnSelectRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRam.clear();
                ramRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ramSnapshot : snapshot.getChildren()) {
                            String ramName = ramSnapshot.getKey();
                            getRam.add(ramName);
                        }

                        CharSequence[] ramArray = getRam.toArray(new CharSequence[getRam.size()]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(SellerEditProductActivity.this);
                        builder.setItems(ramArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String clicked = getRam.get(i);
                                binding.etRam1.setText(clicked);
                                DatabaseReference ramRef=reference.child("Ram").child(clicked);
                                ramRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        binding.etSellingPrise.setText(snapshot.child("SellingPrice").getValue(String.class));
                                        binding.etRetailprice.setText(snapshot.child("RetailPrice").getValue(String.class));
                                        binding.availSwitch.setChecked(snapshot.child("Available").getValue(Boolean.class));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        //End Set price according to the ram


        //Get colour
       binding.btnSelectColour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               colour.clear();
               DatabaseReference colourRef= reference.child("Colour");
               colourRef.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for (DataSnapshot colourName:snapshot.getChildren()){
                           String colourS=colourName.getKey();
                           colour.add(colourS);
                       }
                       CharSequence[] colourArry = colour.toArray(new CharSequence[colour.size()]);
                       AlertDialog.Builder builder=new AlertDialog.Builder(SellerEditProductActivity.this);
                       builder.setItems(colourArry, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               String clicked=   colour.get(i);
                               binding.etcolor.setText(clicked);

                           }
                       }).show();
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


           }
       });
        //End of Get colour

        //Update the Ram
        binding.btnUpdateRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sellingPrice = binding.etSellingPrise.getText().toString();
                String retailPrice = binding.etRetailprice.getText().toString();
                String ram = binding.etRam1.getText().toString().trim();
                Boolean availRam=binding.availSwitch.isChecked();
                if (sellingPrice.isEmpty() || retailPrice.isEmpty() || ram.isEmpty()) {
                    Toast.makeText(SellerEditProductActivity.this, "All fields are compulsory", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    RamPrice.put("SellingPrice", sellingPrice);
                    RamPrice.put("RetailPrice", retailPrice);
                    RamPrice.put("Available",availRam);
                    Ram.put(ram,RamPrice);
                   ramRef.child(ram).setValue(RamPrice).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           dialog.dismiss();
                           Toast.makeText(SellerEditProductActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                           getRam.clear();
                       }
                   });
                }
            }
        });
        //End of Update the Ram

        //Update the colour
        binding.btnUpdateColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean avlColour=binding.availSwitchColour.isChecked();
                String colour=binding.etcolor.getText().toString();
                reference.child("Colour").child(colour).child("Available").setValue(avlColour).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SellerEditProductActivity.this, "Succesfully updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //End of Update the colour

        //Delete Product
        binding.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SellerEditProductActivity.this);
                builder.setMessage("Delete this product");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        dialog.show();
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Seller").child(auth.getUid()).child("Product").child(Id);
                                reference2.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(SellerEditProductActivity.this, "Product deleted", Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(SellerEditProductActivity.this,SellerShowProductActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                }).show();

            }
        });
        //End of Delete Product

        //Update Main Price
        binding.btnUpdateDefaultPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                String dfPrice=binding.etDefaultPrice.getText().toString();
                reference.child("Price").setValue(dfPrice).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Toast.makeText(SellerEditProductActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // end of Update Main Price
        
        //Update availibility
        binding.btnProductAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean availprod=binding.availSwitchProduct.isChecked();
                reference.child("Available").setValue(availprod).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SellerEditProductActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //End of Update availibility

    }


}