package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.Adaptors.AddImageAdaptor;
import com.example.godawari.Models.AddImageModel;
import com.example.godawari.databinding.ActivitySellerAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SellerAddProductActivity extends AppCompatActivity {
   private HashMap<String,HashMap<String,Object>> ram=new HashMap<>();
    ActivitySellerAddProductBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    private static final int REQUEST_CODE_SELECT_IMAGES = 1;
    ArrayList<AddImageModel> selectedImages; // declare selectedImages
    AddImageAdaptor adapter; // declare adapter
    ProgressDialog dialog;
    final String ProductId= UUID.randomUUID().toString();
    String curruntTime= String.valueOf(System.currentTimeMillis());
    HashMap<String, HashMap<String, ArrayList>> ColourUri=new HashMap<String, HashMap<String, ArrayList>>();
    ArrayList<String> ImageUri1 = new ArrayList<String>();
    String profileUri;
    String[] options={"2GB,32GB","3GB,32GB","4GB,64GB","4GB,128GB","6GB,64GB","6GB,128GB","6GB,256GB","8GB,64GB","8GB,128GB","8GB,256GB","12GB,128GB","12GB,256GB","12GB,512GB"};
    ArrayList<String> RamImage=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();


        dialog=new ProgressDialog(SellerAddProductActivity.this,R.style.myDialogue);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(4f);
        circularProgressDrawable.setCenterRadius(24f);
        circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(this, R.color.pumpkin_orange));
        circularProgressDrawable.start();
        dialog.setIndeterminateDrawable(circularProgressDrawable);


//new code
        //Add ram to images
        binding.AddRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SellerAddProductActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String clicked=options[i];
                        RamImage.add(clicked);
                        String result = "";
                        for (String item : RamImage) {
                            result += item + "\n"; // Append each item and a newline character to the result string
                        }

                         binding.AddRamShow.setText(result); // Set the result string as the text of the TextView
                    }
                }).show();
            }
        });

        binding.DeleteColourRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RamImage.size() > 0) {
                    RamImage.remove(RamImage.size() - 1);
                    String result = "";
                    for (String item : RamImage) {
                        result += item + "\n"; // Append each item and a newline character to the result string
                    }

                    binding.AddRamShow.setText(result);
                }
            }
        });
        //End of Add ram to images
// end of new code


        //new code
        // upload images to firebase for specific colour
        binding.btnUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(SellerAddProductActivity.this);
                String colour = binding.etColour.getText().toString().trim();
                if (colour.isEmpty() || adapter.getItemCount() == 0||RamImage.isEmpty()) {
                    builder.setMessage("Entre the colour name and at least one image");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                } else {
                    dialog.show();
                    HashMap<String,ArrayList> ColourInfo=new HashMap<>();
                    ArrayList<String> RamImage1=new ArrayList<>(RamImage);
                    ColourInfo.put("Ram",RamImage1);
                    StorageReference reference = FirebaseStorage.getInstance().getReference().child("Product").child(ProductId).child(colour);
                    final ArrayList<String> ImageUri = new ArrayList<String>();

                    for (AddImageModel image : selectedImages) {
                        // Create a unique file name for each selected image
                        String fileName = System.currentTimeMillis() + ".jpg";
                        // Get a reference to the Firebase Storage location where you want to store this image
                        StorageReference imageRef = reference.child(fileName);

                        imageRef.putFile(Uri.parse(image.getImageUrl())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                adapter.clear();
                                adapter.notifyDataSetChanged();
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Toast.makeText(SellerAddProductActivity.this, "Images uploaded for"+" "+colour, Toast.LENGTH_SHORT).show();
                                        ImageUri.add(uri.toString());

                                    }
                                });
                            }
                        });
                    }
                    ColourInfo.put("Images",ImageUri);
                ColourUri.put(colour, ColourInfo);
                binding.etColour.setText(null);
                }

            }
        });
        //end of new code


        //add image recycle view
        selectedImages = new ArrayList<>(); // initialize selectedImages
        binding.btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_CODE_SELECT_IMAGES);
            }
        });
        adapter = new AddImageAdaptor(selectedImages, this); // initialize adapter
        binding.recyckler.setAdapter(adapter);
        LinearLayoutManager manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        binding.recyckler.setLayoutManager(manager);
        //end of add image recycle view


        //ram price
        binding.etRam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SellerAddProductActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       String clicked=options[i];
                       binding.etRam1.setText(clicked);
                    }
                }).show();

            }
        });
        //end of ram price





        //Ram hashmap
        binding.buttonAddRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ram=binding.etRam1.getText().toString().trim();
                String sellingPrice=binding.etSellingPrise.getText().toString();
                String retailPrice=binding.etRetailprice.getText().toString();

                if(Ram.isEmpty()||sellingPrice.isEmpty()||retailPrice.isEmpty()){

                }else {
                    HashMap<String, Object> price = new HashMap<>();
                    price.put("SellingPrice", sellingPrice);
                    price.put("RetailPrice", retailPrice);
                    price.put("Available",true);

                    ram.put(Ram, price);
                    UpdateTextView();

                    binding.etRam1.setText(null);
                    binding.etSellingPrise.setText(null);
                    binding.etRetailprice.setText(null);
                }

            }
        });

        binding.buttonDeleteRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ram.isEmpty()) {
                    // Find the highest key value in the HashMap
                    String highestKey = Collections.max(ram.keySet());

                    // Remove the entry with the highest key value
                    ram.remove(highestKey);

                    // Update the TextView to display all RAM values
                    UpdateTextView();
                }
            }
        });
        // end of ram hashmap


      //get profle image
        binding.btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,80);

            }
        });

      //end of get profle image




        //Add data to firebase
        binding.btnAddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SellerAddProductActivity.this);
                String name = binding.etProductName.getText().toString();
                String defaultPrice = binding.etDefaultPrice.getText().toString();
                String discription = binding.etDiscription.getText().toString();
                String specification = binding.etSpecification.getText().toString();
                String RetailPrice=binding.etRetailpriceMain.getText().toString();
                String defaultRam=binding.etDefaultRam.getText().toString();
                String camera=binding.etCamera.getText().toString();
                String battery=binding.etBattery.getText().toString();
                String display=binding.etDisplay.getText().toString();
                String SelfieCamera=binding.etSelfieCamera.getText().toString();
                String Note=binding.etNote.getText().toString();
                boolean cod=binding.chipCashOnDelivery.isChecked();
                if (Note.isEmpty()||SelfieCamera.isEmpty()||camera.isEmpty()||display.isEmpty()||battery.isEmpty()||defaultRam.isEmpty()||RetailPrice.isEmpty()
                        ||name.isEmpty() || discription.isEmpty() || specification.isEmpty() || defaultPrice.isEmpty()) {
                    builder.setMessage("All fields are compulsory");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
                } else {
                    dialog.show();
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Product").child(ProductId).child("Images");
                    for (AddImageModel image : selectedImages) {
                        // Create a unique file name for each selected image
                        String fileName = System.currentTimeMillis() + ".jpg";
                        // Get a reference to the Firebase Storage location where you want to store this image
                        StorageReference imageRef = storageRef.child(fileName);
                        imageRef.putFile(Uri.parse(image.getImageUrl())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the download URL of the uploaded image
                                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ImageUri1.add(uri.toString());
                                        if (ImageUri1.size() == selectedImages.size()) {
                                            Toast.makeText(SellerAddProductActivity.this, "Images Succesfully uploaded", Toast.LENGTH_SHORT).show();
                                            HashMap<String, Object> product = new HashMap<>();
                                            String sellerId = auth.getUid();
                                            product.put("Id", ProductId);
                                            product.put("RetailPrice",RetailPrice);
                                            product.put("Name", name);
                                            product.put("RamDefault",defaultRam);
                                            product.put("Camera",camera);
                                            product.put("SelfieCamera",SelfieCamera);
                                            product.put("Battery",battery);
                                            product.put("Note",Note);
                                            product.put("Display",display);
                                            product.put("Profile",profileUri);
                                            product.put("CashOnDelivery",cod);
                                            product.put("Image", ImageUri1);
                                            product.put("Ram", ram);
                                            product.put("Price", defaultPrice);
                                            product.put("Colour", ColourUri);
                                            product.put("Discription", discription);
                                            product.put("Specification", specification);
                                            product.put("SellerId", sellerId);
                                            product.put("Available", true);

                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Product").child(ProductId);
                                            databaseReference.setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(SellerAddProductActivity.this, "Product data added successfully", Toast.LENGTH_SHORT).show();
                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Seller").child(auth.getUid()).child("Product").child(ProductId);
                                                    reference.setValue(ProductId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            dialog.dismiss();
                                                            finish();
                                                            Toast.makeText(SellerAddProductActivity.this, "All done", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(SellerAddProductActivity.this, SellerAddProductActivity.class);
                                                            startActivity(intent);


                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            dialog.dismiss();
                                                            Toast.makeText(SellerAddProductActivity.this, "Failed to save the product", Toast.LENGTH_SHORT).show();
                                                            DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference().child("Product").child(ProductId);
                                                            reference3.removeValue();
                                                            deleteData();
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    deleteData();
                                                    dialog.dismiss();
                                                }
                                            });
                                        }else{deleteData(); }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });



        // End of Add data to firebase



    }


    private  void UpdateTextView(){
        // Create a StringBuilder to hold the final string
        StringBuilder stringBuilder = new StringBuilder();

        // Loop through each RAM value in the HashMap and append it to the StringBuilder
        for (Map.Entry<String, HashMap<String, Object>> ramEntry : ram.entrySet()) {
            String ramKey = ramEntry.getKey();
            HashMap<String, Object> priceMap = ramEntry.getValue();
            String sellingPriceValue = String.valueOf(priceMap.get("SellingPrice"));
            String retailPriceValue = String.valueOf(priceMap.get("RetailPrice"));

            stringBuilder.append("RAM: ");
            stringBuilder.append(ramKey);
            stringBuilder.append("\n");
            stringBuilder.append("Selling Price: ");
            stringBuilder.append(sellingPriceValue);
            stringBuilder.append("\n");
            stringBuilder.append("Retail Price: ");
            stringBuilder.append(retailPriceValue);
            stringBuilder.append("\n\n");
        }

        // Set the text of the TextView to the final string
        binding.textViewRam.setText(stringBuilder.toString());
    }

    private  void deleteData(){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Product").child(ProductId);
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference imageRef : listResult.getItems()) {
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Image deleted successfully
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occurred while deleting the image
                        }
                    });
                }
                storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Parent reference deleted successfully
                        AlertDialog.Builder adialog=new AlertDialog.Builder(SellerAddProductActivity.this);
                        adialog.setMessage("Please reupload the data");
                        adialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while deleting the parent reference
                    }
                });
            }
        });

    }




    //add images to recycklerview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                // If multiple images are selected
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri uri = clipData.getItemAt(i).getUri();
                    selectedImages.add(new AddImageModel(uri.toString()));
                }
            } else {
                // If only one image is selected
                Uri uri = data.getData();
                selectedImages.add(new AddImageModel(uri.toString()));
            }
            adapter.notifyDataSetChanged();
        }
        //add profile photo
        if(requestCode==80){
        if(data.getData()!=null){
            Uri file=data.getData();
            binding.imageBanner.setImageURI(file);
            //Upload profilePhoto
            binding.btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();
                    StorageReference reference=FirebaseStorage.getInstance().getReference().child("Product").child(ProductId).child("Profile").child(curruntTime+"jpg");
                    reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    dialog.dismiss();
                                    Toast.makeText(SellerAddProductActivity.this, "Photo uploaded", Toast.LENGTH_SHORT).show();
                                    profileUri= String.valueOf(uri);
                                }
                            });
                        }
                    });
                }
            });
            //end of Upload profilePhoto
        }
        }
        //end of add profile photo

    }
    //end of add images to recycklerview




}
