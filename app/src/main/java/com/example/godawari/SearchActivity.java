package com.example.godawari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.godawari.Adaptors.SearchAdaptor;
import com.example.godawari.Models.SearchModel;
import com.example.godawari.databinding.ActivitySearchBinding;
import com.example.godawari.databinding.ActivityUserMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import Fragments.FiltreFragment;

public class SearchActivity extends AppCompatActivity {
  ActivitySearchBinding binding;
  FirebaseAuth auth;
  FirebaseDatabase database;
  String Link,a,Search,Query,Id,Recent,Price;
  int Filtre,Filtre1;
    ArrayList<SearchModel> list;
    SearchAdaptor adaptor;


    CustomDialog dialog=new CustomDialog(SearchActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        a="1";




        MyPreferences preferences = new MyPreferences(this); // 'this' is the context of the current activity
        preferences.saveMyId(auth.getUid());


        Recent=getIntent().getStringExtra("Recent");
        try {
            if (Recent.length() != 0) {
                binding.searchBar.setQuery(Recent, true);
                getData(Recent.toLowerCase());
            }
        }catch (Exception e){}


        Id=getIntent().getStringExtra("Id");
        try {
            if (Id.length() != 0) {
                binding.searchBar.setQuery(Id, true);
                getData(Id);
            }
        }catch (Exception e){}

         Query=getIntent().getStringExtra("Query");
        try {
            if (Query.length() != 0) {
                binding.searchBar.setQuery(Query, true);
                getData(Query);
            }
        }catch (Exception e){}

        // chek the intent
         Link = getIntent().getStringExtra("Link");
         try {
             if (Link.length() != 0) {
                 binding.searchBar.setQuery(Link, true);
                 getData(Link.toLowerCase());
             }
         }catch (Exception e){}

        binding.searchBar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Intent intent = new Intent(SearchActivity.this, PreSearchActivity.class);
                    intent.putExtra("value",a);
                    startActivity(intent);
                    binding.searchBar.clearFocus();
                }
            }
        });


         binding.tryAgain.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(SearchActivity.this, PreSearchActivity.class);
                 intent.putExtra("value",a);
                 startActivity(intent);
                 
             }
         });

       try {
           Search=getIntent().getStringExtra("Name").toLowerCase();
           if (Search.length() != 0) {
               binding.searchBar.setQuery(Search, true);
               getData(Search);
           }
       }catch (Exception e){}

        //check intent

         binding.imageView4.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent=new Intent(SearchActivity.this,MyCartActivity.class);
                 startActivity(intent);
             }
         });





        //Filtre button

        binding.btnFiltre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltreFragment fragment=new FiltreFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });

        binding.btnFiltrePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltreFragment fragment=new FiltreFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });

        binding.btnFiltreRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltreFragment fragment=new FiltreFragment();
                fragment.show(getSupportFragmentManager(), fragment.getTag());
            }
        });


        // end of Filtre button





    }
    public void getData(String s){
        CustomDialog dialog=new CustomDialog(SearchActivity.this);
        dialog.showDialogue1();
         list=new ArrayList<>();
         adaptor=new SearchAdaptor(list,this);
        binding.recyckleSearch.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(SearchActivity.this, RecyclerView.VERTICAL,false);
        binding.recyckleSearch.setLayoutManager(layoutManager);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Product");
        Query query=reference.orderByChild("Discription");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismisDialogue();
                setVisibility();
                for(DataSnapshot data:snapshot.getChildren()){
                    SearchModel model=data.getValue(SearchModel.class);
                    model.setId(data.getKey());
                    String discription= model.getDiscription();
                    if(discription!=null&&discription.toLowerCase().trim().contains(s.trim())){
                        list.add(model);
                    }

                }
                adaptor.notifyDataSetChanged();
                if(list.size()==0||adaptor.getItemCount()==0){
                    showError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    protected void onNewIntent(Intent intent) {
        try {

            super.onNewIntent(intent);
            if (intent != null) {
                Price =intent.getStringExtra("Price");
                if (Price!=null){ dialog.showDialogue1();}
                if (Price.equals("Radio1")) {
                    Filtre = 0;
                    Filtre1 = 10000;
                    getFiltreData();
                } else if (Price.equals("Radio2")) {
                    Filtre = 10000;
                    Filtre1 = 15000;
                    getFiltreData();
                } else if (Price.equals("Radio3")) {
                    Filtre = 15000;
                    Filtre1 = 20000;
                    getFiltreData();
                } else if (Price.equals("Radio4")) {
                    Filtre = 20000;
                    Filtre1 = 30000;
                    getFiltreData();
                } else {
                    Filtre = 30000;
                    getFiltreData1();
                }

            }
        }catch (Exception e){
        }
    }

    private void getFiltreData()    {
        String a= String.valueOf(Filtre);
        String b= String.valueOf(Filtre1);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Product");
        Query query=reference.orderByChild("Price").
                startAfter(a)
                .endBefore(b);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot  data:snapshot.getChildren()){
                    SearchModel model=data.getValue(SearchModel.class);
                    model.setId(data.getKey());
                    list.add(model);
                }
                adaptor.notifyDataSetChanged();
                dialog.dismisDialogue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getFiltreData1() {
        String a = String.valueOf(Filtre);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = reference.orderByChild("Price").
                startAfter(a);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SearchModel model = data.getValue(SearchModel.class);
                    model.setId(data.getKey());
                    list.add(model);
                }
                adaptor.notifyDataSetChanged();
                dialog.dismisDialogue();
                if (list.size()==0||adaptor.getItemCount()==0){
                    showError();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setVisibility(){
        binding.btnFiltreRam.setVisibility(View.VISIBLE);
        binding.btnFiltre.setVisibility(View.VISIBLE);
        binding.btnFiltrePrice.setVisibility(View.VISIBLE);
    }

    private void  showError(){
        binding.relativeNotFound.setVisibility(View.VISIBLE);
        binding.recyckleSearch.setVisibility(View.GONE);
        binding.btnFiltrePrice.setVisibility(View.GONE);
        binding.btnFiltreRam.setVisibility(View.GONE);
        binding.btnFiltre.setVisibility(View.GONE);
    }

}