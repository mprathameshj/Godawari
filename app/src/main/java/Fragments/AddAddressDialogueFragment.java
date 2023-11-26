package Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godawari.CustomDialog;
import com.example.godawari.databinding.FragmentAddAddressDialogueBinding;
import com.example.godawari.databinding.FragmentHomeBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddAddressDialogueFragment extends BottomSheetDialogFragment {
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE=100;
    FirebaseDatabase database;
    FirebaseAuth auth;
    CustomDialog dialog;

    FragmentAddAddressDialogueBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding= FragmentAddAddressDialogueBinding.inflate(inflater, container, false);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        dialog=new CustomDialog(getContext());


        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();

        binding.btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }

        });

        setCancelable(false);

        binding.textHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        binding.BtnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name= binding.etName.getText().toString();
                String Number= binding.etMobileNumber.getText().toString();
                String Pincode= binding.etPincode.getText().toString();
                String State= binding.etState.getText().toString();
                String City= binding.etCity.getText().toString();
                String DetailAdd= binding.etDetailAddress.getText().toString();
                boolean hasError = false;
                if(Name.isEmpty()){binding.etName.setError("Required"); hasError = true;}
                if(Number.isEmpty()){binding.etMobileNumber.setError("Required"); hasError = true;}
                if(Pincode.isEmpty()){binding.etPincode.setError("Required"); hasError = true;}
                if(State.isEmpty()){binding.etState.setError("Required"); hasError = true;}
                if(City.isEmpty()){binding.etCity.setError("Required"); hasError = true;}
                if(DetailAdd.isEmpty()){binding.etDetailAddress.setError("Required"); hasError = true;}
                if(hasError) {
                    return;
                } else {
                    dialog.showDialogue();
                    HashMap<String,Object> address=new HashMap<>();
                    address.put("Name",Name);
                    address.put("MobileNumber",Number);
                    address.put("Pincode",Pincode);
                    address.put("State",State);
                    address.put("City",City);
                    address.put("DetailAddress",DetailAdd);
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Address").child(String.valueOf(System.currentTimeMillis()));
                    reference.setValue(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismisDialogue();
                            onOkButtonClick();

                        }
                    });
                }
                
            }
        });





    return binding.getRoot();
    }
    private void getLastLocation() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                   if(location!=null){
                       Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                       List<Address> addresses= null;
                       try {
                           addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                           binding.etDetailAddress.setText(addresses.get(0).getAddressLine(0));
                           binding.etCity.setText(addresses.get(0).getLocality());
                           binding.etState.setText(addresses.get(0).getAdminArea());
                           binding.etPincode.setText(addresses.get(0).getPostalCode());
                       } catch (IOException e) {
                           e.printStackTrace();
                       }


                   }
                }
            });
        }else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onOkButtonClick() {
        try {
            Intent resultIntent = new Intent();
            dismiss();
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}