package com.example.local_data;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void putStringValue(String key, String value)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }

    public void putIntegerValue(String key, Integer value)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public Integer getIntegerValue(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }
}
