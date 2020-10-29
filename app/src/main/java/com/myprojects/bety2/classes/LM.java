package com.myprojects.bety2.classes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/** Locale Manager class **/
public class LM {

    public static String mSharedPreferenceLang;

    public LM(){}

    public static String translate(String name, Context c) {
        String packageName = c.getPackageName() == null ? "com.myprojects.bety2" : c.getPackageName();
        int resId = c.getResources().getIdentifier(name, "string", packageName);
        return c.getString(resId);
    }

    public static void loadLocale(Context c) {
        SharedPreferences preferences = c.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        mSharedPreferenceLang = preferences.getString("MyLang", "ar");

        setLocale(mSharedPreferenceLang, c);
    }

    public static void setLocale(String lang, Context c) {
        Locale myLocale = new Locale(lang);
        Resources resources = c.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(myLocale);
        resources.updateConfiguration(configuration, displayMetrics);

        // Save data to shared preference
        SharedPreferences.Editor editor = c.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("MyLang", lang);
//        Log.d(String.valueOf(c), "setLocale: SharedPreference: " + lang);
        editor.apply();
    }

}
