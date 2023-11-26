package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.databinding.ActivityUserMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.AccountFragment;
import Fragments.CategoryFragment;
import Fragments.FavoriteFragment;
import Fragments.HomeFragment;
import Fragments.WalletFragment;

public class userMainActivity extends AppCompatActivity {

    ActivityUserMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities=connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                boolean isConnected =capabilities!=null&& capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
                if(isConnected){
                    FragmentTransaction homeTran=getSupportFragmentManager().beginTransaction();
                    homeTran.replace(R.id.frameLayout,new HomeFragment());
                    homeTran.commit();

                    // update this rules
                    //".read": "auth!=null",
                    //    ".write":"auth!=null"

                    if(ContextCompat.checkSelfPermission(userMainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    }else {
                        ActivityCompat.requestPermissions(userMainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},49);
                    }


                    binding.bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.home:
                                    FragmentTransaction homeTran=getSupportFragmentManager().beginTransaction();
                                    homeTran.replace(R.id.frameLayout,new HomeFragment());
                                    homeTran.commit();
                                    break;

                                case R.id.category:
                                    FragmentTransaction categoryTran=getSupportFragmentManager().beginTransaction();
                                    categoryTran.replace(R.id.frameLayout,new CategoryFragment());
                                    categoryTran.commit();
                                    break;

                                case R.id.account:
                                    FragmentTransaction accountTran=getSupportFragmentManager().beginTransaction();
                                    accountTran.replace(R.id.frameLayout,new AccountFragment());
                                    accountTran.commit();
                                    break;

                                case R.id.wallet:
                                    FragmentTransaction walletTran=getSupportFragmentManager().beginTransaction();
                                    walletTran.replace(R.id.frameLayout,new WalletFragment());
                                    walletTran.commit();
                                    break;

                                case R.id.favorite:
                                    FragmentTransaction favoriteTran=getSupportFragmentManager().beginTransaction();
                                    favoriteTran.replace(R.id.frameLayout,new FavoriteFragment());
                                    favoriteTran.commit();
                                    break;
                            }
                            return true;
                        }
                    });

                }else {
                    binding.relativeNoInternet.setVisibility(View.VISIBLE);
                    binding.btnTry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recreate();
                        }
                    });
                }
            }
        }






    }
}