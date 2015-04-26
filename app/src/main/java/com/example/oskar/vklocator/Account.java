package com.example.oskar.vklocator;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by romanismagilov on 25.04.15.
 */
public class Account {
    private static Context ctx;
    public String access_token;
    public long user_id;

    public void save(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=prefs.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }

    public void restore(Context context){
        if (ctx == null)
            ctx = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        access_token=prefs.getString("access_token", null);
        user_id=prefs.getLong("user_id", 0);
    }

    public void clear()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor=prefs.edit();
        editor.clear();
        editor.commit();
    }
}
