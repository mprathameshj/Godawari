package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.godawari.Adaptors.SellerShowOrderAdaptor;
import com.example.godawari.Adaptors.SellerShowProductAdaptor;
import com.example.godawari.Models.SellerShowOrderModel;
import com.example.godawari.databinding.ActivitySellerShowOrdersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class SellerShowOrdersActivity extends AppCompatActivity {
    ActivitySellerShowOrdersBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerShowOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        ArrayList<SellerShowOrderModel> list=new ArrayList<>();
        SellerShowOrderAdaptor adaptor=new SellerShowOrderAdaptor(list,SellerShowOrdersActivity.this);
        binding.recyckler.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(SellerShowOrdersActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.recyckler.setLayoutManager(layoutManager);



        String[] options={"New","Completed","Delivered","Dispatched","Return","Rejected"};
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SellerShowOrdersActivity.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String clicked=options[i].toLowerCase();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Orders");
                        Query query = reference.orderByChild("Status");
                        ValueComparator valueComparator = new ValueComparator();
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot order: snapshot.getChildren()) {
                                    SellerShowOrderModel model = order.getValue(SellerShowOrderModel.class);
                                    String status = model.getStatus();
                                    if (status != null && status.toLowerCase().contains(clicked)) {
                                        model.setOrderId(order.getKey());
                                        list.add(model);
                                    }
                                }
                                adaptor.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle error
                            }
                        });
                    }
                }).show();
            }
        });







    }
    class ValueComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
    }
}