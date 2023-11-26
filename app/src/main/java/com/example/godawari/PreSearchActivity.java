package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.godawari.Adaptors.PreSearchAdaptor;
import com.example.godawari.Adaptors.RecentAdaptor1;
import com.example.godawari.Models.PreSearchModel;
import com.example.godawari.Models.RecentModel;
import com.example.godawari.Models.SearchModel;
import com.example.godawari.databinding.ActivityPreSearchBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreSearchActivity extends AppCompatActivity {
    ActivityPreSearchBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String a;
    ArrayList<PreSearchModel> list=new ArrayList<>();
    PreSearchModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityPreSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        PreSearchAdaptor adaptor=new PreSearchAdaptor(list,PreSearchActivity.this);
        binding.recyckleSearch.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(PreSearchActivity.this,LinearLayoutManager.VERTICAL,false);
        binding.recyckleSearch.setLayoutManager(layoutManager);


        a=getIntent().getStringExtra("value");
        try {
            if (a.length() != 0) {
                binding.searchBar.requestFocus();


            }
        }catch (Exception e){}


        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                binding.textView1.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
                binding.recycklerRecom.setVisibility(View.GONE);
                binding.linear.setVisibility(View.GONE);
                binding.mic.setVisibility(View.GONE);

                RecyclerView recyclerView = binding.recyckleSearch;
                ViewGroup.LayoutParams layoutParams1 = recyclerView.getLayoutParams();
                layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
                recyclerView.setLayoutParams(layoutParams1);



                binding.searchText.setVisibility(View.VISIBLE);
                binding.linear8.setVisibility(View.VISIBLE);
                binding.searchText.setText(newText);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product");
                    Query query = reference.orderByChild("Name");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                 model = data.getValue(PreSearchModel.class);
                                String Name = model.getName();
                                if (Name != null && Name.toLowerCase().contains(newText.toLowerCase())) {
                                    list.add(model);
                                }
                            }
                            adaptor.notifyDataSetChanged();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                if (newText.isEmpty()) {
                    binding.textView1.setVisibility(View.VISIBLE);
                    binding.textView.setVisibility(View.VISIBLE);
                    binding.recycklerRecom.setVisibility(View.VISIBLE);
                    binding.linear.setVisibility(View.VISIBLE);
                    binding.mic.setVisibility(View.VISIBLE);


                    RecyclerView recyclerView1 = binding.recyckleSearch;

                    int desiredHeightInDp = 200; // Specify the desired height in dp here
                    float scale = recyclerView1.getContext().getResources().getDisplayMetrics().density;
                    int desiredHeightInPixels = (int) (desiredHeightInDp * scale + 0.5f);
                    ViewGroup.LayoutParams layoutParams = recyclerView1.getLayoutParams();
                    layoutParams.height = desiredHeightInPixels;

// Apply the updated layout parameters to the RecyclerView
                    recyclerView.setLayoutParams(layoutParams);



                    binding.searchText.setVisibility(View.GONE);
                    binding.linear8.setVisibility(View.GONE);
                } else {
                }
                return true;
            }
        });

        binding.searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String b=binding.searchText.getText().toString().toLowerCase();
                saveSearch(b);
                Intent intent=new Intent(PreSearchActivity.this,SearchActivity.class);
                intent.putExtra("Query",b);
                startActivity(intent);
            }
        });



        // get search history from sq database
        getsearch();
        // end of get search history from sq database



        binding.mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PreSearchActivity.this, "Voice search not available", Toast.LENGTH_SHORT).show();
            }
        });
        
        

        // Recommonded recyckle
        ArrayList<RecentModel> list1 = new ArrayList<>();
        RecentAdaptor1 adaptor1 = new RecentAdaptor1(list1, PreSearchActivity.this);

        GridLayoutManager layoutManager1 = new GridLayoutManager(PreSearchActivity.this, 4, RecyclerView.VERTICAL, false);
        binding.recycklerRecom.setAdapter(adaptor1);
        binding.recycklerRecom.setLayoutManager(layoutManager1);

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = reference1.orderByChild("Price").limitToFirst(28);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    RecentModel model = data.getValue(RecentModel.class);
                    model.setId(data.getKey());
                    list1.add(model);
                }
                adaptor1.notifyDataSetChanged(); // Notify the adapter of the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });



        // End of Recommonded recyckle



    }


    private void saveSearch(String b){
        //code save search
        // Create or open the database
        SQLiteDatabase db = openOrCreateDatabase("MyCache.db", Context.MODE_PRIVATE, null);

// Create the table if it doesn't exist
        db.execSQL("CREATE TABLE IF NOT EXISTS cache (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)");

// Save the text to the cache
        String textToCache = b;

// Delete the oldest text if there are already 5 texts in the cache
        Cursor cursor = db.rawQuery("SELECT id FROM cache ORDER BY id DESC LIMIT 5", null);
        if (cursor.moveToLast()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            db.execSQL("DELETE FROM cache WHERE id < " + id);
        }

        db.execSQL("INSERT INTO cache (text) VALUES ('" + textToCache + "')");

// Close the database when finished
        db.close();



        //end of  code save search
    }

    private void getsearch(){
        try {
            // Open the database
            SQLiteDatabase db = openOrCreateDatabase("MyCache.db", Context.MODE_PRIVATE, null);

// Retrieve the cached texts
            Cursor cursor = db.rawQuery("SELECT text FROM cache", null);
            List<String> cachedTexts = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String cachedText = cursor.getString(cursor.getColumnIndex("text"));
                    cachedTexts.add(cachedText);
                } while (cursor.moveToNext());
            }

// Close the cursor and the database when finished
            cursor.close();
            db.close();

// Process the retrieved texts
            for (String text : cachedTexts) {
                PreSearchModel model1 = new PreSearchModel(text, "");
                list.add(model1);
            }

        }catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(PreSearchActivity.this,userMainActivity.class);
        startActivity(intent);
    }
}