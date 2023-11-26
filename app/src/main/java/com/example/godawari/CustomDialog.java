package com.example.godawari;




import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class CustomDialog{
    Context context;
    Dialog dialog;

    public CustomDialog(Context context) {
        this.context = context;
    }
    public void showDialogue(){
       dialog=new Dialog(context);
       dialog.setContentView(R.layout.dialogue_style);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       dialog.setCanceledOnTouchOutside(false);
       dialog.show();
    }

    public void showDialogue1(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialogue_style1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // Disable background blur
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public void showDialogue2(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialogue_style_2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // Disable background blur
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    public void showDeleteDialogue(){
        dialog=new Dialog(context);
        dialog.setContentView(R.layout.delete_dialogue);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.dialogue_background));



        // Set dialog window attributes for positioning
       // WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        //layoutParams.gravity = Gravity.BOTTOM;  // Set the gravity to the bottom
        //layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

      //  dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // Disable background blur
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public void dismisDialogue(){
        dialog.dismiss();
    }


    public TextView getCancelButton() {
        return dialog.findViewById(R.id.btn_cancel);
    }

    public TextView getDeleteButton() {
        return dialog.findViewById(R.id.btn_delete);
    }



}


