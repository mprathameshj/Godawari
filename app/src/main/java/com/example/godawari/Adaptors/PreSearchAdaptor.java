package com.example.godawari.Adaptors;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.Models.PreSearchModel;
import com.example.godawari.R;
import com.example.godawari.SearchActivity;
import com.squareup.picasso.Cache;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class PreSearchAdaptor extends RecyclerView.Adapter<PreSearchAdaptor.ViewHolder>{

    ArrayList<PreSearchModel> list=new ArrayList<>();
    Context context;

    public PreSearchAdaptor(ArrayList<PreSearchModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_pre_search,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PreSearchModel model = list.get(position);
        holder.Name.setText(model.getName());
        String id = model.getProfile();


        if(id.length()>1) {
            Picasso picasso = Picasso.get();
            // Enable memory caching
            //picasso.setIndicatorsEnabled(true); // Optional: Show indicators for debugging purposes
            picasso.setLoggingEnabled(true); // Optional: Enable logging for debugging purposes

            picasso.load(id)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE) // Disable memory caching
                    .networkPolicy(NetworkPolicy.OFFLINE) // Load from cache only
                    .into(holder.image);

        }else {
            holder.Name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.history1, 0, 0, 0);
        }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String c=model.getName();

                 // Assuming you have the appropriate context available in the adapter class

// Open or create the database
                 SQLiteDatabase db = context.openOrCreateDatabase("MyCache.db", Context.MODE_PRIVATE, null);

// Create the table if it doesn't exist
                 db.execSQL("CREATE TABLE IF NOT EXISTS cache (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT)");

// Save the text to the cache
                 String textToCache = c;

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

                 Intent intent=new Intent(context, SearchActivity.class);
                 intent.putExtra("Name",c);
                 context.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.PreName);
            image=itemView.findViewById(R.id.PreImage);
        }
    }
}
