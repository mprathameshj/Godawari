package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.godawari.Adaptors.SellerShowProductAdaptor;
import com.example.godawari.Models.SellerShowProductModel;
import com.example.godawari.databinding.ActivitySellerShowProductBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class SellerShowProductActivity extends AppCompatActivity {
    ActivitySellerShowProductBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerShowProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        //Show product
        ArrayList<SellerShowProductModel> arrayList=new ArrayList<>();
        SellerShowProductAdaptor adaptor=new SellerShowProductAdaptor(arrayList,this);
        binding.recycle.setAdapter(adaptor);
        adaptor.filterList("");

        LinearLayoutManager layoutManager=new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        binding.recycle.setLayoutManager(layoutManager);


            database.getReference().child("Product").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adaptor.list.clear();
                    adaptor.filteredList.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        try {
                            SellerShowProductModel model = data.getValue(SellerShowProductModel.class);
                            model.setId(data.getKey());
                            arrayList.add(model);
                        }catch (Exception e){
                            Toast.makeText(SellerShowProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    adaptor.list = arrayList;
                    adaptor.filteredList = new ArrayList<>(arrayList);
                    adaptor.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        //end of Show product

        //new code
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptor.filterList(newText.trim()); // call filterList method with newText
                return true;
            }


        });

        //end of new code







    }
}