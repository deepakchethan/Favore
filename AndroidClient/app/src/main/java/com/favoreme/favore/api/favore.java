package com.favoreme.favore.api;


import android.content.Context;
import android.content.SharedPreferences;

public class favore {

    private static favore ref = new favore();
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;
    private static String key;
    private SharedPreferences loggedIn;

    public favore(){

    }
    public static favore get(Context context){
        mContext = context;
        if (isLoggedIn()){
            mSharedPreferences = mContext.getSharedPreferences("jwtkey",Context.MODE_PRIVATE);
            key = mSharedPreferences.getString("jKey","");
        }
        return ref;
    }

    private static boolean isLoggedIn(){
        mSharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
        return mSharedPreferences.getBoolean("log",false);
    }
    public static String getKey() {
        return key;
    }

    public void logIn(String jwtKey){
        mSharedPreferences=mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putBoolean("log",true).apply();

        mSharedPreferences=mContext.getSharedPreferences("jwtKey",Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putString("jKey",jwtKey).apply();
        key = jwtKey;
    }
}
