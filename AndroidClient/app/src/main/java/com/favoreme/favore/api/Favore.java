package com.favoreme.favore.api;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.favoreme.favore.Login.LoginActivity;
import com.favoreme.favore.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

public class Favore {

    private static Favore ref = new Favore();
    private static Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private String key;
    private User Owner;
    private SharedPreferences loggedIn;

    //Shared pref INFO
    // login: log stores if the user is logged in
    // login: jkey stores the jwt key of the user
    // login: jkey stores the uid of the user

    // user: fname stores the first name of the user
    // user: lname stores the last name of the user
    // user: dname stores the display name of the user
    // user: uname stores the email of the user
    // user: age stores the age of the user
    // user: uid stores the user id
    // user: bio stores the use bio

    // User details start here

    private int uid,age;
    private String fname,lname,dname,uname,bio,phone,gender;

    private void fetchDeets(){
        mSharedPreferences = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        fname = mSharedPreferences.getString("fname",null);
        lname = mSharedPreferences.getString("lname",null);
        dname = mSharedPreferences.getString("dname",null);
        uname = mSharedPreferences.getString("uname",null);
        phone = mSharedPreferences.getString("phone",null);
        gender = mSharedPreferences.getString("gender",null);
        uid = mSharedPreferences.getInt("uid",-1);
        age = mSharedPreferences.getInt("age",-1);
        bio = mSharedPreferences.getString("bio",null);
        Owner = new User(uid,fname,lname,uname,dname,gender,phone,bio,age);

    }

    public User getOwner(){
        return Owner;
    }

    public void setDeets(JSONObject user){
        mSharedPreferences = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        try {
            editor.putString("fname",user.getString("fname")).apply();
            editor.putString("lname",user.getString("lname")).apply();
            editor.putString("dname",user.getString("dname")).apply();
            editor.putString("uname",user.getString("username")).apply();
            editor.putString("phone",user.getString("phone")).apply();
            editor.putString("gender",user.getString("gender")).apply();
            editor.putString("bio",user.getString("bio")).apply();
            editor.putBoolean("sync",true).apply();
            editor.putInt("age",user.getInt("age")).apply();
            editor.putInt("uid",user.getInt("id")).apply();
        } catch (JSONException e) {
            toasty("Unable to setup user details!");
            e.printStackTrace();
        }

    }

    Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mContext, (String) msg.obj,Toast.LENGTH_SHORT).show();
        }
    };

    public void toasty(String mess){
        Message msg = Message.obtain();
        msg.obj = mess;
        msg.setTarget(h);
        msg.sendToTarget();
    }
    public Favore(){

    }
    public static Favore get(Context context){
        mContext = context;
        return ref;
    }

    public boolean isSyncedIn(){
        mSharedPreferences = mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        boolean ans = mSharedPreferences.getBoolean("sync",false);
        if (ans){
            fetchDeets();
        }
        return ans;
    }
    public boolean isLoggedIn(){
        mSharedPreferences = mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
        boolean ans = mSharedPreferences.getBoolean("log",false);
        if (ans){
            fetchKey();
        }
        return ans;
    }

    public String getKey() {
        return this.key;
    }

    public void logIn(String jwtKey){
        mSharedPreferences=mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putBoolean("log",true).apply();
        editor.putString("jKey",jwtKey).apply();
        this.key = jwtKey;
    }

    private void fetchKey(){
        mSharedPreferences = mContext.getSharedPreferences("jwtkey",Context.MODE_PRIVATE);
        key = mSharedPreferences.getString("jKey","");
    }
}
