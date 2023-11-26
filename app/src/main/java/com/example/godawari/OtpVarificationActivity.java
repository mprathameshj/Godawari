package com.example.godawari;


import static com.google.firebase.auth.PhoneAuthProvider.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.godawari.databinding.ActivityOtpVarificationBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class OtpVarificationActivity extends AppCompatActivity {
    ActivityOtpVarificationBinding binding;
    FirebaseAuth auth;
    String otpId;
    String phoneNumber;
    ProgressDialog dialog;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpVarificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        phoneNumber=getIntent().getStringExtra("mobile");
        binding.textMobilenum.setText("+91"+phoneNumber);
        dialog=new ProgressDialog(this);
        dialog.setMessage("Varifying"+" "+"+91"+phoneNumber);
        dialog.setCanceledOnTouchOutside(false);
        initiateOtp();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OtpVarificationActivity.this,LoginActivity.class));
            }
        });

        binding.btnSubmitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                if(binding.etotp.getText().toString().isEmpty()){
                    Toast.makeText(OtpVarificationActivity.this, "Please entre valid otp", Toast.LENGTH_SHORT).show();
                }
                else if(binding.etotp.getText().toString().length()!=6){
                    Toast.makeText(OtpVarificationActivity.this, "otp must contain 6 digits", Toast.LENGTH_SHORT).show();
                }
                else{
                    dialog.show();
                    PhoneAuthCredential credential= getCredential(otpId,binding.etotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
                }catch (Exception e){
                }

            }
        });
    if(auth.getCurrentUser()!=null){
        Intent intent=new Intent(OtpVarificationActivity.this,RegisterSellerActivity.class);
        startActivity(intent);
    }
    binding.btnEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(OtpVarificationActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    });

    }

    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91"+phoneNumber)    // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otpId=s;
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);



    }

    private void  signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                dialog.dismiss();
                if (task.isSuccessful()){
                  Intent intent=new Intent(OtpVarificationActivity.this,RegisterSellerActivity.class);
                  startActivity(intent);

                }
                else{

                    Toast.makeText(OtpVarificationActivity.this,"Invalid otp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
