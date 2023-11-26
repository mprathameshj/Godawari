package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.Adaptors.MyOrdersAdaptor;
import com.example.godawari.Models.MyOrdersModel;
import com.example.godawari.databinding.ActivityMyOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    ActivityMyOrdersBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        CustomDialog dialog=new CustomDialog(MyOrdersActivity.this);
        ArrayList<String> orderId=new ArrayList<>();

        dialog.showDialogue2();

        ArrayList<MyOrdersModel> list=new ArrayList<>();
        MyOrdersAdaptor adaptor=new MyOrdersAdaptor(list,MyOrdersActivity.this);
        binding.recycle.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(MyOrdersActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.recycle.setLayoutManager(layoutManager);


        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Orders");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders");
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Product");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    dialog.dismisDialogue();
                    dialog.showDialogue1();
                    if (snapshot.exists()) {
                        String orderId1 = data.getValue(String.class);
                        orderId.add(orderId1);

                        reference.child(orderId1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                dialog.dismisDialogue();
                                String product = snapshot.child("Id").getValue(String.class);
                                String status = snapshot.child("Status").getValue(String.class);
                                String  orderId=snapshot.child("OrderId").getValue(String.class);
                                reference1.child(product).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            MyOrdersModel model = snapshot.getValue(MyOrdersModel.class);
                                            model.setStatus(status);
                                            model.setOrderId(orderId);
                                            if (model != null) {
                                                list.add(model);
                                                adaptor.notifyDataSetChanged();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(MyOrdersActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle onCancelled if needed
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle onCancelled if needed
                            }
                        });
                    }else{

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });


        //Back button
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //End of back button


    }
}