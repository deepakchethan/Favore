package com.favoreme.favore.api;


import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Backend {
    private static Backend ref = new Backend();
    private static Context mContext;
    private String mUrl = "172.16.16.16:3000";
    private OkHttpClient client;
    private Response mResponse =  null;
    public Backend(){}

    public static Backend get(Context context){
        mContext = context;
        return ref;
    }
    public void toasty(String txt){
        Toast.makeText(mContext,txt,Toast.LENGTH_SHORT).show();
    }

    public String Signup(String email, String pass) throws IOException {
         client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username="+email+"&password="+pass);
        Request request = new Request.Builder()
                .url(mUrl+"/signup")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                   toasty("Unable to create account");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }

    public String Signin(String email,String pass) throws IOException{
         client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username="+email+"&password="+pass);
        Request request = new Request.Builder()
                .url(mUrl+"/signin")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Could not signin!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    mResponse = response;
            }
        });
        return mResponse.body().string();
    }
    public String WritePost(long lon,long lat,String postText,long uid,long date) throws IOException{
         client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\n\t\"location\":{\"type\":\"point\",\"coordinates\":["+lon+","+lat+"]},\n\t\"favors\":0,\n\t\"postText\":\""+postText+"\",\n\t\"age\":0,\n\t\"posterId\":"+uid+",\n\t\"date\":"+date+"\n}");
        Request request = new Request.Builder()
                .url(mUrl+"/user/post")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to post it");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }
    public String RemovePost(long id) throws IOException {
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/removePost/"+id)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to remove the post");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }
    public String FetchPosts(long lon,long lat) throws IOException{
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/fetchPosts?lon="+lon+"&lat="+lat)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to get the posts for now");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }
    public String GetUserDetails(long uid) throws IOException {
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/getUserDetails/"+uid)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to get user info");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }
    public String EditUser(String fname, String lname,String dname,String email,String phone,String bio,String age) throws IOException {
         client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "fname="+fname+"&lname="+lname+"&dname="+dname+"&username="+email+"&phone="+phone+"&bio="+bio+"&id=1&age="+age);
        Request request = new Request.Builder()
                .url(mUrl+"/user/edituser/")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to edit user info!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }

    public String AddFriend(long usr,long frnd) throws IOException {
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/addfriend/?usr="+usr+"&frnd="+frnd)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to add as friend");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }

    public String RemoveFriend(long usr,long frnd) throws IOException {
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/removefriend/?usr="+usr+"&frnd="+frnd)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to remove him as friend");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }

    public String GetFriendList(long usr) throws IOException{
         client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(mUrl+"/user/getFriendList/"+usr)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toasty("Unable to fetch friend list");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mResponse = response;
            }
        });
        return mResponse.body().string();
    }

}
