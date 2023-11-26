package com.example.godawari;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.godawari.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
 ActivityLoginBinding binding;
 FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.etMobile.requestFocus();
        auth=FirebaseAuth.getInstance();


        if(auth.getCurrentUser()!=null) {
            Intent intent = new Intent(LoginActivity.this, userMainActivity.class);
            startActivity(intent);
        }


     binding.textView90.setText(Html.fromHtml("By continuing you are agree to Godawari's "+"<font color='#013ADF'>Terms , Conditions</font>"+" and "+
             "<font color='#013ADF'>Privacy Policy</font>"));

        binding.btnGetOtp.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if (binding.etMobile.getText().toString().isEmpty()) {
                 Toast.makeText(LoginActivity.this, "Please entre a mobile number to continue", Toast.LENGTH_SHORT).show();
             } else if (binding.etMobile.getText().toString().length() != 10) {
                 Toast.makeText(LoginActivity.this, "Please entre a valid mobile number", Toast.LENGTH_SHORT).show();
             } else {
                 Intent intent = new Intent(LoginActivity.this, OtpVarificationActivity.class);
                 intent.putExtra("mobile", binding.etMobile.getText().toString().trim());
                 startActivity(intent);
             }
         }
     });


    }
}