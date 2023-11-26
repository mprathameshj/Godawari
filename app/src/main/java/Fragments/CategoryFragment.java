package Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godawari.Adaptors.CategoryAdaptor;
import com.example.godawari.Models.CategoryModel;
import com.example.godawari.R;
import com.example.godawari.databinding.FragmentCategoryBinding;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    FragmentCategoryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCategoryBinding.inflate(inflater,container,false);


        ArrayList<CategoryModel> list=new ArrayList<>();
        list.add(new CategoryModel(R.drawable.apple,"apple"));
        list.add(new CategoryModel(R.drawable.samsung,"samsung"));
        list.add(new CategoryModel(R.drawable.vivo,"vivo"));
        list.add(new CategoryModel(R.drawable.oneplus,"oneplus"));
        list.add(new CategoryModel(R.drawable.oppo,"oppo"));
        list.add(new CategoryModel(R.drawable.mi,"mi"));
        list.add(new CategoryModel(R.drawable.nothing,"nothing"));
        list.add(new CategoryModel(R.drawable.realme,"realme"));
        list.add(new CategoryModel(R.drawable.poco1,"poco"));
        list.add(new CategoryModel(R.drawable.iqoo,"iqoo"));
        list.add(new CategoryModel(R.drawable.google,"google"));
        list.add(new CategoryModel(R.drawable.motorola,"motorola"));

        CategoryAdaptor adaptor=new CategoryAdaptor(list,getContext());
        binding.recycle.setAdapter(adaptor);

        GridLayoutManager layoutManager=new GridLayoutManager(getContext(),3);
        binding.recycle.setLayoutManager(layoutManager);



        return binding.getRoot();
    }
}