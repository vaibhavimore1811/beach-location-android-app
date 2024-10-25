package com.sanika.beachapplication.constance;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    public static String PrefName = "SharedPref";
    public static String PrefNameLocale = "SharedPrefLocale";
    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static String locale = "locale";
    public static String locale_db = "locale_db";
    public static final String IS_INTRO_OPENED = "IS_INTRO_OPENED";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String Product_category = "Product_category";
    public static final String Wallet_balance = "Wallet_balance";
    public static final String user_id = "user_id";
    public static final String full_name = "full_name";
    public static final String sponser_id = "sponser_id";
    public static final String email_address = "email_address";
    public static final String mobile_number = "mobile_number";
    public static final String gender = "gender";
    public static final String registration_date = "registration_date";
    public static final String wallet_balance = "wallet_balance";
    public static final String userPoints = "userPoints";
    public static final String pincode = "pincode";
    public static final String pincodeverify = "pincodeverify";



    public static SharedPreferences getSharedPref(Context context) {
        SharedPreferences mPreferences;

        mPreferences = context.getSharedPreferences(PrefName, MODE_PRIVATE);

        return mPreferences;
    }

    public static SharedPreferences getLocaleSharedPref(Context context) {
        SharedPreferences mPreferences;

        mPreferences = context.getSharedPreferences(PrefNameLocale, MODE_PRIVATE);

        return mPreferences;
    }

    public static String getString(SharedPreferences mPreferences, String name) {
        return mPreferences.getString(name, "");
    }

    public static int getInt(SharedPreferences mPreferences, String name) {
        return mPreferences.getInt(name, 0);
    }

    public static Boolean getBoolean(SharedPreferences mPreferences, String name) {
        return mPreferences.getBoolean(name, false);
    }

    public static void setInt(SharedPreferences mPreferences, String name, int value) {
        SharedPreferences.Editor mEditor;
        mEditor = mPreferences.edit();
        mEditor.putInt(name, value);
        mEditor.apply();
    }

    public static void setString(SharedPreferences mPreferences, String name, String value) {
        SharedPreferences.Editor mEditor;
        mEditor = mPreferences.edit();
        mEditor.putString(name, value);
        mEditor.apply();
    }

    public static void setBoolean(SharedPreferences mPreferences, String name, Boolean value) {
        SharedPreferences.Editor mEditor;
        mEditor = mPreferences.edit();
        mEditor.putBoolean(name, value);
        mEditor.commit();
    }

    public static void clearSharedPref(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences(PrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


}
