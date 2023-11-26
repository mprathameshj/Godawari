package Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.godawari.BuyProductActivity;
import com.example.godawari.MyPreferences;
import com.example.godawari.R;
import com.example.godawari.databinding.FragmentRamBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;

public class RamFragment extends BottomSheetDialogFragment {

    FragmentRamBinding binding;
    FirebaseDatabase database;
    String clicked,clickedRam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRamBinding.inflate(getLayoutInflater(),container,false);
        database=FirebaseDatabase.getInstance();

        MyPreferences preferences=new MyPreferences(getContext());
        String Id=preferences.getMyProduct();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Product").child(Id);


        // Add Colour to chips
        reference.child("Colour").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot colourSnap : snapshot.getChildren()) {
                    String colour = colourSnap.getKey();

                    Chip chip = new Chip(getContext());
                    chip.setText(colour);
                    chip.setTag(colour); // Set the value as a tag on the chip

                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v1) {
                            apply();
                            binding.warning.setText("");
                            // Reset background color of all chips
                            for (int i = 0; i < binding.chipGroup.getChildCount(); i++) {
                                Chip chip = (Chip) binding.chipGroup.getChildAt(i);
                                chip.setChipBackgroundColorResource(R.color.silver);
                            }
                            chip.setChipBackgroundColorResource(R.color.BuyNow); // Reset background color of all chips
                            Chip clickedChip = (Chip) v1;
                             clicked = clickedChip.getTag().toString();
                            DatabaseReference ColorRef=reference.child("Colour").child(clicked);
                            ColorRef.child("Ram").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    binding.chipGroup1.removeAllViews();
                                    for (DataSnapshot RamSnap2:snapshot.getChildren()){
                                        String Rams= (String) RamSnap2.getValue();
                                        Chip chip = new Chip(getContext());
                                        chip.setText(Rams);
                                        chip.setTag(Rams);
                                        chip.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                for (int i = 0; i < binding.chipGroup1.getChildCount(); i++) {
                                                    Chip chip = (Chip) binding.chipGroup1.getChildAt(i);
                                                    chip.setChipBackgroundColorResource(R.color.silver);
                                                }
                                                chip.setChipBackgroundColorResource(R.color.BuyNow); // Reset background color of all chips
                                                Chip clickedChip1 = (Chip) view;
                                                clickedRam= clickedChip1.getTag().toString();

                                                //new code
                                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Product").child(Id);
                                                DatabaseReference RamPrice= reference.child("Ram");
                                                RamPrice.child(clickedRam).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @SuppressLint("ResourceAsColor")
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(!snapshot.exists()){
                                                            chip.setChipBackgroundColorResource(R.color.redChip);
                                                            remove();
                                                        }
                                                        try {
                                                            Boolean Available=snapshot.child("Available").getValue(Boolean.class);
                                                            if(!Available==true){
                                                                chip.setChipBackgroundColorResource(R.color.redChip);
                                                                remove();
                                                            }else{
                                                               apply();
                                                            }



                                                        }catch (Exception e){
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                                //new code



                                            }
                                        });
                                        binding.chipGroup1.addView(chip);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    binding.chipGroup.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

        // End of add Colour to chips



        //Add Ram to Chips
        reference.child("Ram").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ramSnap:snapshot.getChildren()){
                    String rams=ramSnap.getKey();
                    Chip chip=new Chip(getContext());
                    chip.setText(rams);
                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            binding.warning.setText("Please select the color");
                        }
                    });
                    binding.chipGroup1.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //End of add Ram to Chips




        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked!=null&&clickedRam!=null){
                    Intent intent=new Intent(getActivity(), BuyProductActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("Colour",clicked);
                    intent.putExtra("Ram",clickedRam);
                    startActivity(intent);
                    dismiss();
                }
            }
        });


        binding.btnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });





        return binding.getRoot();
    }

    protected  void apply(){
        binding.btnApply.setEnabled(true);
        binding.btnApply.setText("Select");
        binding.btnApply.setTextColor(getResources().getColor(R.color.white));
        binding.btnApply.setBackgroundColor(getResources().getColor(R.color.BuyNow));
    }

    protected  void  remove(){
        binding.btnApply.setText("Out of stock");
        binding.btnApply.setTextColor(getResources().getColor(R.color.electric_red));
        binding.btnApply.setEnabled(false);
        binding.btnApply.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.not_available_bg));
    }
}