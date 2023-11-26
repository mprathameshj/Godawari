package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.godawari.Adaptors.BuyAdaptor;
import com.example.godawari.Models.BuyModel;
import com.example.godawari.databinding.ActivityBuyProductBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import Fragments.AddAddressDialogueFragment;
import Fragments.RamFragment;
import me.relex.circleindicator.CircleIndicator2;



public class BuyProductActivity extends AppCompatActivity {
    ActivityBuyProductBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
   ArrayList<String> ArryColour=new ArrayList<>();
   ArrayList<String> ArryRam=new ArrayList<>();
   ArrayList<String> ArryDetailAddress=new ArrayList<>();
    ArrayList<String> imageUris2= new ArrayList<>();
    boolean cod;
    String Reference,ClickedColour,ClickedRam,Id;

    CustomDialog customDialogue;
    String addressId;

    ArrayList<BuyModel> list;
    BuyAdaptor adaptor;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBuyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.CustomAlertDialogTheme);
        AlertDialog.Builder builder=new AlertDialog.Builder(themedContext);
        AlertDialog.Builder dialog= new AlertDialog.Builder(themedContext);
        NumberFormat numberFormat = NumberFormat.getInstance();

        customDialogue=new CustomDialog(this);



         Id= getIntent().getStringExtra("id");
       String sp=getIntent().getStringExtra("price");
       String RetailPrice= getIntent().getStringExtra("retailPriceText");
       String Discount= getIntent().getStringExtra("Discount");
       String Name= getIntent().getStringExtra("Name");
       double Price= Double.parseDouble(sp);
       double Rp= Double.parseDouble(RetailPrice);
       try {
           double save = Rp - Price;
           binding.Save.setText(Html.fromHtml("You will save" + " " +"<b>\u20B9" + numberFormat.format(save) + "</b> " + "on this order"));
       }catch (Exception e){}
        SpannableStringBuilder spannable = new SpannableStringBuilder(RetailPrice);
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannable.setSpan(strikethroughSpan, 0, RetailPrice.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

       binding.name.setText(Name);
       binding.SellingPrice.setText("\u20B9" + numberFormat.format(Price));
       binding.CutPrice.setText(spannable);
       binding.Off.setText(Discount+"%"+" "+"off");


         Reference=getIntent().getStringExtra("Reference");
        try {
            if (Reference.equals("ok")){
                DatabaseReference referenceUser=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Recent").child(Id);
                referenceUser.setValue(Id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                       Reference="NotOk";
                    }
                });

            }
        }catch (Exception e){}



        MyPreferences preferences = new MyPreferences(this); // 'this' is the context of the current activity
        preferences.saveMyString("1");
        preferences.saveMyId(auth.getUid());

        preferences.saveMyProduct(Id);

        //Back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //End of back button

        //confeti vieew
        try {
            LottieAnimationView animationView = findViewById(R.id.animation_view);
            animationView.setAnimation(R.raw.explode_confetti);
            animationView.playAnimation();
        }catch (Exception e){
            Toast.makeText(themedContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //end of confeti view


         list=new ArrayList<>();
         adaptor=new BuyAdaptor(list,BuyProductActivity.this);
        binding.recyckler.setAdapter(adaptor);

        LinearLayoutManager manager=new LinearLayoutManager(BuyProductActivity.this, RecyclerView.HORIZONTAL,false);
        binding.recyckler.setLayoutManager(manager);



        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.recyckler);
        CircleIndicator2 indicator = findViewById(R.id.indicator);
        indicator.attachToRecyclerView(binding.recyckler, pagerSnapHelper);
        adaptor.registerAdapterDataObserver(indicator.getAdapterDataObserver());



        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Product").child(Id);



       reference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String SpecifSnap=snapshot.child("Specification").getValue(String.class);
                cod=snapshot.child("CashOnDelivery").getValue(boolean.class);
               boolean avb=snapshot.child("Available").getValue(boolean.class);
               binding.etSpecification.setText(Html.fromHtml(SpecifSnap));
               if(cod!=true){binding.cashOnDelivery.setText("No cash on delivery");}
               if(avb!=true){binding.ProductUavailable.setText("This product is curruntly not available");
                   binding.btnBuy.setBackgroundColor(getResources().getColor(R.color.silver));
                   binding.btnBuy.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           RelativeLayout layout=findViewById(R.id.buy_relative);
                           Snackbar snackbar= Snackbar.make(layout, "This product is curruntly not available", Snackbar.LENGTH_SHORT);
                           snackbar.show();
                       }
                   });
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        DatabaseReference imgRef=reference.child("Image");
        imgRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> imageUris = new ArrayList<>();
               for (DataSnapshot image:snapshot.getChildren()){
                   String Uri= (String) image.getValue();
                   imageUris.add(Uri);
                   imageUris2.add(Uri);
               }
               for(String imgUri:imageUris){
                   BuyModel model=new BuyModel();
                   model.setImage(imgUri);
                   list.add(model);
               }
               adaptor.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        //new new code
        try {
            reference.child("Colour").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot colourSnap : snapshot.getChildren()) {
                        String colour = colourSnap.getKey();
                        ArryColour.add(colour);
                    }
                    binding.etColour.setText(Html.fromHtml("Colour:" + " " + "<b> " + ArryColour.get(0) + "</b>"));
                    DatabaseReference ColorRef1 = reference.child("Colour").child(ArryColour.get(0));
                    ColorRef1.child("Ram").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArryRam.clear();
                            for (DataSnapshot RamSnap2:snapshot.getChildren()){
                                String Rams= (String) RamSnap2.getValue();
                                ArryRam.add(Rams);
                            }
                                binding.etRam.setText(Html.fromHtml("RAM and Storage:" + "<b> " + ArryRam.get(0) + "</b>"));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(themedContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // End of new new code







        //Set the ram and the colour field
        binding.etColour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RamFragment fragment=new RamFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
                binding.warnigRam.setText(null);
                }
        });
        //End of Set the ram and the colour field


        //Set Ram fields
     /*   reference.child("Ram").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ramSnap:snapshot.getChildren()){
                    String rams=ramSnap.getKey();
                    ArryRam.add(rams);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });  */
        //End of Set Ram fields

        binding.etRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.warnigRam.setText(null);
               RamFragment fragment=new RamFragment();
               fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });

        getAddressData();






        binding.btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String option = binding.btnSelectAddress.getText().toString();
                if (option.length() > 8) {
                    AddAddressDialogueFragment fragment = new AddAddressDialogueFragment();
                    fragment.show(getSupportFragmentManager(), fragment.getTag());
                }else{
                    Intent intent=new Intent(BuyProductActivity.this,ChangeAddressActivity.class);
                    startActivity(intent);


                }
            }
        });


        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Ram = binding.etRam.getText().toString();
                String Colour = binding.etColour.getText().toString();
                String check = binding.RamNotAvailable.getText().toString();
                try {
                    if(check.length()>2){
                        int y = (int) binding.recyckler.getY();
                        binding.scroll.smoothScrollTo(0, y);
                        Toast.makeText(themedContext, "Out of stock", Toast.LENGTH_SHORT).show();
                    }else {
                        if (Ram.length() < 17 || Colour.length() < 9) {
                            int y = (int) binding.recyckler.getY();
                            binding.scroll.smoothScrollTo(0, y);
                            //binding.etColour.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.warning_bg));
                            binding.warnigRam.setText("Select Colour,RAM and Storage");
                            RelativeLayout layout=findViewById(R.id.buy_relative);
                            Snackbar snackbar = Snackbar.make(layout, "Select Colour,RAM and Storage", Snackbar.LENGTH_SHORT);
                            //snackbar.getView().setBackgroundColor(ContextCompat.getColor(BuyProductActivity.this, R.color.electric_red));
                            snackbar.show();

                        } else {
                            Intent intent = new Intent(BuyProductActivity.this, OrderAddressActivity.class);
                            intent.putExtra("Name", binding.name.getText().toString());
                            intent.putExtra("Price", binding.SellingPrice.getText().toString());
                            intent.putExtra("RetailPrice", binding.CutPrice.getText().toString());
                            intent.putExtra("Discount", binding.Off.getText().toString());
                            intent.putExtra("Colour", Colour);
                            intent.putExtra("Ram", Ram);
                            intent.putExtra("Id", Id);
                            intent.putExtra("AddressId", addressId);
                            intent.putExtra("CashOnD",cod);
                            intent.putStringArrayListExtra("Images",imageUris2);
                            startActivity(intent);
                        }
                    }
                }catch (Exception e){}
            }
        });

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            addressId = intent.getStringExtra("AddressId");
            if (addressId != null) {
                DatabaseReference addressRef=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Address").child(addressId);
               addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String detailAdd=snapshot.child("DetailAddress").getValue(String.class);
                       binding.etAddress.setText(detailAdd);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }

            ClickedColour=intent.getStringExtra("Colour");
            ClickedRam=intent.getStringExtra("Ram");
            if (ClickedColour!=null&&ClickedRam!=null){
                binding.etColour.setText(Html.fromHtml("Colour:" + " " + "<b> " +ClickedColour+ "</b>"));
                binding.etRam.setText(Html.fromHtml("RAM and Storage:" + "<b> " + ClickedRam+"</b>"));

                //new code
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product").child(Id);
                DatabaseReference RamPrice= reference.child("Ram");
                RamPrice.child(ClickedRam).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            NumberFormat numberFormat = NumberFormat.getInstance(); // Initialize NumberFormat
                            String SellingPrice1=snapshot.child("SellingPrice").getValue(String.class);
                            String RetailPrice1=snapshot.child("RetailPrice").getValue(String.class);
                            SellingPrice1=SellingPrice1.trim();
                            RetailPrice1=RetailPrice1.trim();

                            double Sell= Double.parseDouble(SellingPrice1);
                            double RetSell= Double.parseDouble(RetailPrice1);

                            double savep=RetSell-Sell;
                            double discount_percentage = ((RetSell - Sell) / (double) RetSell) * 100;
                            int discount= (int) discount_percentage;
                            binding.Off.setText(discount+"%"+" "+"off");
                            binding.Save.setText(Html.fromHtml("You will save" + " " +"<b>\u20B9" + numberFormat.format(savep) + "</b> " + "on this order"));
                            String RetSellFormat=numberFormat.format(RetSell);
                            Boolean Available=snapshot.child("Available").getValue(Boolean.class);
                            binding.SellingPrice.setText("\u20B9" + numberFormat.format(Sell));
                            if(!Available==true){binding.RamNotAvailable.setText("Out of stock");}else{
                                binding.RamNotAvailable.setText(null);
                            }

                            SpannableStringBuilder spannable = new SpannableStringBuilder(RetSellFormat);
                            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
                            spannable.setSpan(strikethroughSpan, 0, RetSellFormat.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            binding.CutPrice.setText(spannable);

                        }catch (Exception e){
                            Toast.makeText(BuyProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






                try {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Product").child(Id);
                    DatabaseReference ColorRef = reference1.child("Colour").child(ClickedColour);
                    ColorRef.child("Images").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                list.clear();
                                imageUris2.clear();
                                List<String> imageUris1 = new ArrayList<>();
                                for (DataSnapshot Clr : snapshot.getChildren()) {
                                    String uri = (String) Clr.getValue();
                                    imageUris1.add(uri);
                                    imageUris2.add(uri);

                                }
                                for (String imgUri : imageUris1) {
                                    BuyModel model = new BuyModel();
                                    model.setImage(imgUri);
                                    list.add(model);
                                }
                                adaptor.notifyDataSetChanged();


                            }catch (Exception e){
                                Toast.makeText(BuyProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }catch (Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //End of new code



            }

        }
    }


    private void getAddressData() {
        DatabaseReference UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        UserRef.child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.btnSelectAddress.setText("Change");
                    for (DataSnapshot AddressSnap:snapshot.getChildren()){
                        String detailAddress=AddressSnap.child("DetailAddress").getValue(String.class);
                        binding.etAddress.setText(detailAddress);
                        ArryDetailAddress.add(detailAddress);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}