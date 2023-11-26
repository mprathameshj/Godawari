package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.godawari.Adaptors.BuyAdaptor;
import com.example.godawari.Models.BuyModel;
import com.example.godawari.databinding.ActivityOrderAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;

import Fragments.AddAddressDialogueFragment;

public class OrderAddressActivity extends AppCompatActivity {
    ActivityOrderAddressBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<String> ArryDetailAddress = new ArrayList<>();
    String addressId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        NumberFormat numberFormat = NumberFormat.getInstance();
        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.CustomAlertDialogTheme);
        AlertDialog.Builder builder=new AlertDialog.Builder(themedContext);


        String Id=getIntent().getStringExtra("Id");
        String AddressId=getIntent().getStringExtra("AddressId");
        String Name = getIntent().getStringExtra("Name");
        String Discount = getIntent().getStringExtra("Discount");
        String Price1 = getIntent().getStringExtra("Price");
        String RetailPrice1 = getIntent().getStringExtra("RetailPrice");
        String Colour1 = getIntent().getStringExtra("Colour");
        String Ram1 = getIntent().getStringExtra("Ram");
        boolean cod=getIntent().getBooleanExtra("CashOnD",false);
        ArrayList<String> Image = getIntent().getStringArrayListExtra("Images");



        String Price2=Price1.replace("\u20B9","");

       String Price=Price2.replace(",","");
       String RetailPrice=RetailPrice1.replace(",","");

       int Pr= Integer.parseInt(Price);
       int RPr= Integer.parseInt(RetailPrice);
       String discount= String.valueOf(RPr-Pr);
       double discount_double= Double.parseDouble(discount);
       binding.etDiscount.setText("\u20B9"+numberFormat.format(discount_double));

       String TotalAmt= String.valueOf(Pr+89);
       double TotalAmount= Double.parseDouble(TotalAmt);
       binding.etTotalAmount.setText("\u20B9"+numberFormat.format(TotalAmount));

        String savings= String.valueOf(RPr-Pr-89);
        double Savings= Double.parseDouble(savings);
        binding.etTotalSavings.setText("\u20B9"+numberFormat.format(Savings));



        String replaceColour = "Colour:";
        String replaceRam = "RAM and Storage:";
        String Colour = Colour1.replace(replaceColour, "");
        String Ram = Ram1.replace(replaceRam, "");
        try {
            SpannableStringBuilder spannable = new SpannableStringBuilder(RetailPrice1);
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            spannable.setSpan(strikethroughSpan, 0, RetailPrice1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            binding.CutPrice.setText(spannable);
        }catch (Exception e){
            Toast.makeText(themedContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
           binding.name.setText(Name);
           binding.SellingPrice.setText(Price1);
           binding.Off.setText(Discount);
           binding.etColour1.setText(Html.fromHtml("Colour:" + " " + "<b>" + Colour + "</b>"));
           binding.etRam.setText(Html.fromHtml("Ram and Storage:" + " " + "<b>" + Ram + "</b>"));

           binding.etBuyPrice.setText(Price1);

        }catch (Exception e){
            Toast.makeText(themedContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        binding.FinalTotal.setText(Html.fromHtml("Total:"+"<b>"+binding.etTotalAmount.getText().toString()+"</b>"));


        MyPreferences preferences = new MyPreferences(this); // 'this' is the context of the current activity
        preferences.saveMyString("0");


        // Back button
        binding.imageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // End of back button



        //set the images into imageView;
        ArrayList<BuyModel> list=new ArrayList<>();
        BuyAdaptor adaptor=new BuyAdaptor(list,OrderAddressActivity.this);
        binding.recycleImage.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(OrderAddressActivity.this,LinearLayoutManager.HORIZONTAL,false);
        binding.recycleImage.setLayoutManager(layoutManager);
        for (String image:Image){
            BuyModel model=new BuyModel();
            model.setImage(image);
            list.add(model);
        }
        adaptor.notifyDataSetChanged();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                int curruntPosition= layoutManager.findFirstVisibleItemPosition();
                int nextPosition=curruntPosition+1;
                if(nextPosition>=adaptor.getItemCount()){
                    nextPosition=0;
                }
                binding.recycleImage.smoothScrollToPosition(nextPosition);
                binding.recycleImage.postDelayed(this,2000);
            }
        };
        binding.recycleImage.postDelayed(runnable,2000);
        //emd of set the images into imageView;

        getAddressData();

      if(AddressId!=null){
          DatabaseReference addressRef=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Address").child(AddressId);
          addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  String detailAdd=snapshot.child("DetailAddress").getValue(String.class);
                  String Name=snapshot.child("Name").getValue(String.class);
                  String mobileNumber=snapshot.child("MobileNumber").getValue(String.class);
                  String State=snapshot.child("State").getValue(String.class);
                  String Pincode=snapshot.child("Pincode").getValue(String.class);
                  binding.etAddress.setText(detailAdd);
                  binding.etMobile.setText(mobileNumber);
                  binding.Name.setText(Name);
                  binding.State.setText(State);
                  binding.Pincode.setText(Pincode);
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {

              }
          });
      }else {
          DatabaseReference UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
          UserRef.child("Address").addListenerForSingleValueEvent(new ValueEventListener() {
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

        binding.btnSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String option = binding.btnSelectAddress.getText().toString();
                if (option.length() > 8) {
                    AddAddressDialogueFragment fragment = new AddAddressDialogueFragment();
                    fragment.show(getSupportFragmentManager(), fragment.getTag());
                }else{
                    Intent intent=new Intent(OrderAddressActivity.this,ChangeAddressActivity.class);
                    startActivity(intent);
                }
            }
        });

      binding.btnContinue.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent=new Intent(OrderAddressActivity.this,SelectPaymentActivity.class);
              intent.putExtra("cod",cod);
              intent.putExtra("Id",Id);
              intent.putExtra("DetailAddress",binding.etAddress.getText().toString());
              intent.putExtra("State",binding.State.getText().toString());
              intent.putExtra("Pincode",binding.Pincode.getText().toString());
              intent.putExtra("Name",binding.Name.getText().toString());
              intent.putExtra("MobileNumber",binding.etMobile.getText().toString());
              intent.putExtra("ProductName",binding.name.getText().toString());
              intent.putExtra("Colour",binding.etColour1.getText().toString());
              intent.putExtra("Ram",binding.etRam.getText().toString());
              intent.putExtra("Amount",binding.FinalTotal.getText().toString());
              startActivity(intent);
          }
      });

    }

    private void getAddressData() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid());
        UserRef.child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.btnSelectAddress.setText("Change");
                    for (DataSnapshot AddressSnap : snapshot.getChildren()) {
                        String detailAddress = AddressSnap.child("DetailAddress").getValue(String.class);
                        String Name = AddressSnap.child("Name").getValue(String.class);
                        String Number = AddressSnap.child("MobileNumber").getValue(String.class);
                        String State = AddressSnap.child("State").getValue(String.class);
                        String Pincode = AddressSnap.child("Pincode").getValue(String.class);
                        binding.State.setText(State);
                        binding.Pincode.setText(Pincode);
                        binding.etAddress.setText(detailAddress);
                        binding.Name.setText(Name);
                        binding.etMobile.setText(Number);
                        ArryDetailAddress.add(detailAddress);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        String State=snapshot.child("State").getValue(String.class);
                        String Pincode=snapshot.child("Pincode").getValue(String.class);
                        String Name=snapshot.child("Name").getValue(String.class);
                        String Number=snapshot.child("MobileNumber").getValue(String.class);
                        binding.etAddress.setText(detailAdd);
                        binding.State.setText(State);
                        binding.Pincode.setText(Pincode);
                        binding.Name.setText(Name);
                        binding.etMobile.setText(Number);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

}