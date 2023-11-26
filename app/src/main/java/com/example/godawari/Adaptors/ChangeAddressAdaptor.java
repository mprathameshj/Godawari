package com.example.godawari.Adaptors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godawari.BuyProductActivity;
import com.example.godawari.ChangeAddressActivity;
import com.example.godawari.CustomDialog;
import com.example.godawari.Models.ChangeAddressModel;
import com.example.godawari.MyPreferences;
import com.example.godawari.OrderAddressActivity;
import com.example.godawari.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangeAddressAdaptor extends RecyclerView.Adapter<ChangeAddressAdaptor.ViewHolder> {

    ArrayList<ChangeAddressModel> list;
    Context context;


    public ChangeAddressAdaptor(ArrayList<ChangeAddressModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

     FirebaseDatabase database;
     FirebaseAuth auth;

    public ChangeAddressAdaptor(FirebaseDatabase database, FirebaseAuth auth) {
        this.database =database;
        this.auth = auth;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_change_address,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChangeAddressModel model=list.get(position);
        holder.Name.setText(model.getName());
        holder.DetailAddress.setText(model.getDetailAddress());
        holder.Pincode.setText(model.getPincode());
        holder.State.setText(model.getState()+" "+"-");
        holder.MobileNumber.setText(model.getMobileNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    MyPreferences preferences = new MyPreferences(context); // 'this' is the context of the current activity
                    String myString = preferences.getMyString();
                    if (myString.equals("1")){
                        Intent intent=new Intent(context, BuyProductActivity.class);
                        intent.putExtra("AddressId",model.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent);
                    }else if (myString.equals("0")){
                        Intent intent=new Intent(context, OrderAddressActivity.class);
                        intent.putExtra("AddressId",model.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        context.startActivity(intent);
                    }else if(myString.equals("2")){}else{}
            }
        });



        holder.Remove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               CustomDialog dialog=new CustomDialog(context);
               dialog.showDeleteDialogue();

               try {
                   TextView cancelButton = dialog.getCancelButton();
                   TextView deleteButton = dialog.getDeleteButton();

                   cancelButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           dialog.dismisDialogue();
                       }
                   });

                   deleteButton.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           dialog.dismisDialogue();
                           CustomDialog dialogue=new CustomDialog(context);
                           dialogue.showDialogue();
                           MyPreferences preferences = new MyPreferences(context); // 'this' is the context of the current activity
                           String myId = preferences.getMyId();
                           DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(myId).child("Address").child(model.getId());
                           reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   dialogue.dismisDialogue();
                                   Snackbar.make(((Activity) context).findViewById(android.R.id.content), "Successfully deleted", Snackbar.LENGTH_SHORT).show();

                               }
                           });
                       }
                   });

               }catch (Exception e){
                   Toast.makeText(context,e.getMessage(), Toast.LENGTH_SHORT).show();
               }

           }
       });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
    TextView Name,DetailAddress,Pincode,State,MobileNumber,Remove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name11);
            DetailAddress=itemView.findViewById(R.id.DetailAddress11);
            Pincode=itemView.findViewById(R.id.Pincode11);
            State=itemView.findViewById(R.id.State11);
            MobileNumber=itemView.findViewById(R.id.MobileNumber11);
            Remove=itemView.findViewById(R.id.Remove);
        }
    }
}
