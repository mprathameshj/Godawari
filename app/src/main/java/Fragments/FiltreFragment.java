package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.godawari.ChangeAddressActivity;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.example.godawari.databinding.FragmentFiltreBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class FiltreFragment extends BottomSheetDialogFragment {
  FragmentFiltreBinding binding;
  String Price,Ram;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFiltreBinding.inflate(inflater,container,false);


        //Dismiss the dialogue
        binding.btnCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //End of dismiss the dialogue




        //Get the selected value
        binding.RadioPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 Price=getResources().getResourceEntryName(i);

            }
        });

        binding.RadioRam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Ram=getResources().getResourceEntryName(i);
            }
        });
        //End of get the selected value




        //Get the filtre data and start the new intent
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Price!=null||Ram!=null){
                    Intent intent=new Intent(getActivity(), SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putExtra("Price",Price);
                    intent.putExtra("Ram",Ram);
                    startActivity(intent);
                    dismiss();
                }
            }
        });
        //End of get the filtre data and start the new intent



        binding.btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.RadioRam.setVisibility(View.GONE);
                binding.RadioBattery.setVisibility(View.GONE);
                binding.RadioPrice.setVisibility(View.VISIBLE);
            }
        });

        binding.btnRam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.RadioRam.setVisibility(View.VISIBLE);
                binding.RadioPrice.setVisibility(View.GONE);
                binding.RadioBattery.setVisibility(View.GONE);
            }
        });

        binding.btnBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.RadioRam.setVisibility(View.GONE);
                binding.RadioPrice.setVisibility(View.GONE);
                binding.RadioBattery.setVisibility(View.VISIBLE);
            }
        });



        return binding.getRoot();
    }
}