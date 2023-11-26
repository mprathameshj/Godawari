package com.example.godawari.Adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.ProgressModel;
import com.example.godawari.R;

import java.util.ArrayList;

public class ProgressAdaptor extends RecyclerView.Adapter<ProgressAdaptor.ViewHolder> {

    ArrayList<ProgressModel>  list =new ArrayList<>();
    Context context;

    public ProgressAdaptor(ArrayList<ProgressModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_progress,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProgressModel model=list.get(position);
        holder.textView.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text09);
        }
    }
}
