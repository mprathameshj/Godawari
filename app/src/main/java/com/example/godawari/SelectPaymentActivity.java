package com.example.godawari;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.godawari.databinding.ActivitySelectPaymentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

public class SelectPaymentActivity extends AppCompatActivity  implements PaymentResultListener {
    ActivitySelectPaymentBinding binding;
    String SelectedButton;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String Amount,DetailAddress,State,Pincode,Name,MobileNumber,ProductName,Colour,Ram,Date,Id;
    private Checkout checkout;
    String OrderId,amt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CustomDialog dialog=new CustomDialog(SelectPaymentActivity.this);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Checkout.preload(getApplicationContext());



        Id=getIntent().getStringExtra("Id");
         DetailAddress=getIntent().getStringExtra("DetailAddress");
         State=getIntent().getStringExtra("State");
         Pincode=getIntent().getStringExtra("Pincode");
         Name=getIntent().getStringExtra("Name");
         MobileNumber=getIntent().getStringExtra("MobileNumber");
         ProductName=getIntent().getStringExtra("ProductName");
         Colour=getIntent().getStringExtra("Colour");
         Ram=getIntent().getStringExtra("Ram");
       Amount=getIntent().getStringExtra("Amount");
       String amt1=Amount.replaceAll("Total:\u20B9","");
        String amt01=amt1.replaceAll(",","");
        int amt03= Integer.parseInt(amt01);
        int amt04=amt03*100;
        amt2= String.valueOf(amt04);



        Random random = new Random();
        long min = 100000000000000000L; // minimum 20-digit number
        long max = 999999999999999999L; // maximum 20-digit number
        long randomNumber = min + (long) (random.nextDouble() * (max - min));
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String date = dateFormat.format(new Date());

       Date Today= Calendar.getInstance().getTime();
       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
       Date=simpleDateFormat.format(Today);


        String randomString = String.valueOf(randomNumber);
      OrderId=date+randomString;


        boolean cod=getIntent().getBooleanExtra("cod",false);
        if(cod!=true) {
            RadioButton myRadioButton = binding.RadioCashOnD;
            myRadioButton.setAlpha(0.8f);
            myRadioButton.setEnabled(false);
            binding.iButton.setVisibility(View.VISIBLE);
        }

        binding.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton selected=findViewById(i);
                SelectedButton=selected.getText().toString();
                if (SelectedButton.equals("Pay now")){
                    binding.button.setText("Pay"+"  "+Amount.replaceAll("Total:",""));
                    binding.button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background));
                }else if(SelectedButton.equals("Cash on delivery")){
                    binding.button.setText("Confirm Order");
                    binding.button.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_background));
                }
            }
        });

       binding.button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               try {
                   if (SelectedButton.equals("Pay now")) {
                       startPayment();
                   } else if (SelectedButton.equals("Cash on delivery")) {
                       addOrder();
                   } else {
                   }
               }catch (Exception e){}
           }
       });



    }

    public void startPayment() {
        checkout = new Checkout();
        checkout.setKeyID("rzp_test_DNO09pX3SHTUve");
        
        // checkout.setImage(R.drawable.logo);
        final Activity activity = this;


        try {
            JSONObject options = new JSONObject();

            options.put("name", "Jaybhaye Prathamesh");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",amt2);//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", "8550952160");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            checkout.open(SelectPaymentActivity.this, options);

        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Successfull", Toast.LENGTH_SHORT).show();
        addOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public  void addOrder(){
        CustomDialog customDialog=new CustomDialog(SelectPaymentActivity.this);
        customDialog.showDialogue();
        HashMap<String, Object> AddressHash = new HashMap<>();
        AddressHash.put("DetailAddress", DetailAddress);
        AddressHash.put("State", State);
        AddressHash.put("Pincode", Pincode);
        AddressHash.put("Name", Name);
        AddressHash.put("MobileNumber", MobileNumber);

        HashMap<String, Object> order = new HashMap<>();
        order.put("OrderId", OrderId);
        order.put("Product", ProductName);
        order.put("Colour", Colour);
        order.put("Date",Date);
        order.put("Status","New");
        order.put("Ram", Ram);
        order.put("Id", Id);
        order.put("UserId",auth.getUid());
        order.put("Amount", Amount);
        order.put("Address", AddressHash);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders").child(OrderId);
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Orders").child(String.valueOf(System.currentTimeMillis()));
        reference.setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                reference1.setValue(OrderId).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        customDialog.dismisDialogue();
                        Intent intent = new Intent(SelectPaymentActivity.this, ConfirmOrderActivity.class);
                        intent.putExtra("OrderId", OrderId);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                addOrder();
                            }
                        });
                    }
                });
            }
        });
    }
}