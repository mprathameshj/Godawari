package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godawari.Adaptors.RecentAdaptor;
import com.example.godawari.ChangeAddressActivity;
import com.example.godawari.Models.RecentModel;
import com.example.godawari.MyOrdersActivity;
import com.example.godawari.MyPreferences;
import com.example.godawari.R;
import com.example.godawari.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AccountFragment extends Fragment {
 FragmentAccountBinding binding;
 FirebaseAuth auth;
 FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAccountBinding.inflate(inflater,container,false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        ArrayList<RecentModel> list=new ArrayList<>();
        RecentAdaptor adaptor=new RecentAdaptor(list,getContext());
        binding.recyckler.setAdapter(adaptor);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyckler.setLayoutManager(layoutManager);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Product");
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Recent");


        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String id=data.getValue(String.class);
                    reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            binding.relativeloading.setVisibility(View.GONE);
                            binding.relative2.setVisibility(View.VISIBLE);
                            RecentModel model=snapshot.getValue(RecentModel.class);
                            model.setId(snapshot.getKey());
                            list.add(model);
                            adaptor.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreferences preferences = new MyPreferences(getContext()); // 'this' is the context of the current activity
                preferences.saveMyString("2");
                Intent intent=new Intent(getActivity(), ChangeAddressActivity.class);
                startActivity(intent);
            }
        });

        binding.btnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MyOrdersActivity.class);
                startActivity(intent);
            }
        });

        binding.btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return binding.getRoot();
    }
}