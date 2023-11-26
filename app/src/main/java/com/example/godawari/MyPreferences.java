package com.example.godawari;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String KEY_MY_STRING = "my_string_key";
    private static final String KEY_MY_ID = "my_id_key";
    private static final String KEY_MY_Product = "my_product_key";


    public MyPreferences(Context context) {
        preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void saveMyString(String value) {
        editor.putString(KEY_MY_STRING, value);
        editor.apply();
    }

    public String getMyString() {
        return preferences.getString(KEY_MY_STRING, "");
    }
    public void saveMyId(String value) {
        editor.putString(KEY_MY_ID, value);
        editor.apply();
    }

    public String getMyId() {
        return preferences.getString(KEY_MY_ID, "");
    }


    public void saveMyProduct(String value) {
        editor.putString(KEY_MY_Product, value);
        editor.apply();
    }
    public String getMyProduct() {
        return preferences.getString(KEY_MY_Product, "");
    }


}
