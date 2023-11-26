package Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godawari.Adaptors.MainBannerAdaptor;
import com.example.godawari.Adaptors.RecentAdaptor;
import com.example.godawari.Adaptors.RecentAdaptor1;
import com.example.godawari.Adaptors.SubBannerAdaptor;
import com.example.godawari.CustomDialog;
import com.example.godawari.LoginActivity;
import com.example.godawari.Models.BannerModel;
import com.example.godawari.Models.RecentModel;
import com.example.godawari.Models.SubBannerModel;
import com.example.godawari.MyCartActivity;
import com.example.godawari.PreSearchActivity;
import com.example.godawari.R;
import com.example.godawari.RegisterSellerActivity;
import com.example.godawari.SearchActivity;
import com.example.godawari.SellerMainActivity;
import com.example.godawari.databinding.ActivityUpdateMainBannerBinding;
import com.example.godawari.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator2;


public class HomeFragment extends Fragment {
    String aByte;
    public HomeFragment() {
    }
    FragmentHomeBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        NumberFormat numberFormat=NumberFormat.getNumberInstance();
        aByte= "1";



        binding.textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), RegisterSellerActivity.class);
                startActivity(intent);
            }
        });




        binding.searchBarHome.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), PreSearchActivity.class);
            intent.putExtra("value",aByte);
            startActivity(intent);
            binding.searchBarHome.clearFocus();
        }
    });
    binding.imageView2.setOnClickListener(new View.OnClickListener() {
         @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(), MyCartActivity.class);
            startActivity(intent);
        }
    });
    binding.textView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                Intent intent=new Intent(getActivity(), SellerMainActivity.class);
                startActivity(intent);
        }
    });

        // Set up adapter with list of BannerModel objects
        List<BannerModel> bannerModels = new ArrayList<>();
        MainBannerAdaptor adapter = new MainBannerAdaptor((ArrayList<BannerModel>) bannerModels, getContext());
        binding.recyclerview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(layoutManager);


        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(binding.recyclerview);
        CircleIndicator2 indicator =binding.indicator;
        indicator.attachToRecyclerView(binding.recyclerview, pagerSnapHelper);
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


        // start of recyclerview
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("MainBanner");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot imageSnap: snapshot.getChildren()){
                    BannerModel model=imageSnap.getValue(BannerModel.class);
                    bannerModels.add(model);
                }
                adapter.notifyDataSetChanged();

                Runnable scrollRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Get the current position of the RecyclerView
                        int currentPosition = layoutManager.findFirstCompletelyVisibleItemPosition();

                        // Calculate the position of the next item
                        int nextPosition = currentPosition + 1;
                        if (nextPosition >= adapter.getItemCount()) {
                            // Wrap around to the beginning of the list
                            nextPosition = 0;
                        }

                        // Scroll to the next item
                        binding.recyclerview.smoothScrollToPosition(nextPosition);

                        // Schedule the next scroll after a delay
                        binding.recyclerview.postDelayed(this, 5000);
                    }

                };
                binding.recyclerview.postDelayed(scrollRunnable, 5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
        //end of recyklerview



    //// start of sub recyklerview
       ArrayList<SubBannerModel> list=new ArrayList<>();
       list.add(new SubBannerModel(R.drawable.cash_svgrepo_com,"Cash on delivery"));
       list.add(new SubBannerModel(R.drawable.return__2_,"Easy returns"));
       list.add(new SubBannerModel(R.drawable.cargo,"Fast delivery"));
       list.add(new SubBannerModel(R.drawable.rupee,"Law prices"));
       list.add(new SubBannerModel(R.drawable.quality_premium_certificate_svgrepo_com,"Quality products"));

        SubBannerAdaptor adaptor=new SubBannerAdaptor(list,getContext());
        binding.recyclerview2.setAdapter(adaptor);

        LinearLayoutManager layoutManager2=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyclerview2.setLayoutManager(layoutManager2);
    ////end of sub recyklerview



        ArrayList<RecentModel> list1=new ArrayList<>();
        RecentAdaptor1 adaptor1=new RecentAdaptor1(list1,getContext());
        binding.recyckler.setAdapter(adaptor1);

        LinearLayoutManager layoutManager1=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        binding.recyckler.setLayoutManager(layoutManager1);

        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("Product");
        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("Recent");


        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    String id=data.getValue(String.class);
                    reference1.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            RecentModel model=snapshot.getValue(RecentModel.class);
                            model.setId(snapshot.getKey());
                            list1.add(model);
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


        //Update the new product
        DatabaseReference reference3=FirebaseDatabase.getInstance().getReference().child("New").child("Product");
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id=snapshot.getValue(String.class);
                DatabaseReference reference4=FirebaseDatabase.getInstance().getReference().child("Product").child(id);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.progress.setVisibility(View.GONE);
                        String name=snapshot.child("Name").getValue(String.class);
                        String price=snapshot.child("Price").getValue(String.class);
                        String rp=snapshot.child("RetailPrice").getValue(String.class);
                        String note=snapshot.child("Note").getValue(String.class);
                        String image=snapshot.child("Profile").getValue(String.class);
                        Picasso.get().load(image).into(binding.Image);

                        double price1= Double.parseDouble(price);
                        double rp1= Double.parseDouble(rp);

                        double discount_percentage = ((rp1 - price1) /rp1) * 100;
                        int discount= (int) discount_percentage;

                        SpannableStringBuilder spannable=new SpannableStringBuilder(rp);
                        StrikethroughSpan strikethroughSpan=new StrikethroughSpan();
                        spannable.setSpan(strikethroughSpan,0,rp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        binding.name.setText(name);
                        binding.SellingPrice.setText("\u20B9"+numberFormat.format(price1));
                        binding.CutPrice.setText(spannable);
                        binding.Note.setText(note);
                        binding.Off.setText(discount+"%"+" "+"Off");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // end of Update the new product


        return binding.getRoot();
    }

}