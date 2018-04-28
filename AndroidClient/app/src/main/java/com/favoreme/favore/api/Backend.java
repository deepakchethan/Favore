package com.favoreme.favore.api;


import android.content.Context;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Backend {
    private static Backend ref = new Backend();
    private static Context mContext;
    private String mUrl = "172.16.16.16:3000";

    public Backend(){}

    public static Backend get(Context context){
        mContext = context;
        return ref;
    }

    public String Signup(String email, String pass) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username="+email+"&password="+pass);
        Request request = new Request.Builder()
                .url(mUrl+"/signup")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String Signin(String email,String pass) throws IOException{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username="+email+"&password="+pass);
        Request request = new Request.Builder()
                .url(mUrl+"/signin")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String WritePost(long lon,long lat,String postText,long uid,long date) throws IOException{
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\n\t\"location\":{\"type\":\"point\",\"coordinates\":["+lon+","+lat+"]},\n\t\"favors\":0,\n\t\"postText\":\""+postText+"\",\n\t\"age\":0,\n\t\"posterId\":"+uid+",\n\t\"date\":"+date+"\n}");
        Request request = new Request.Builder()
                .url(mUrl+"/user/post")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String RemovePost(long id) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/removePost/"+id)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String FetchPosts(long lon,long lat) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/fetchPosts?lon="+lon+"&lat="+lat)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String GetUserDetails(long uid) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/getUserDetails/"+uid)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public String EditUser(String fname, String lname,String dname,String email,String phone,String bio,String age) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "fname="+fname+"&lname="+lname+"&dname="+dname+"&username="+email+"&phone="+phone+"&bio="+bio+"&id=1&age="+age);
        Request request = new Request.Builder()
                .url(mUrl+"/user/edituser/")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String AddFriend(long usr,long frnd) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/addfriend/?usr="+usr+"&frnd="+frnd)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String RemoveFriend(long usr,long frnd) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/removefriend/?usr="+usr+"&frnd="+frnd)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String GetFriendList(long usr) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/getFriendList/"+usr)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
