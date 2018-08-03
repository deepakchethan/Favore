package com.favoreme.favore.Fragments;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.favoreme.favore.CustomAdapter;
import com.favoreme.favore.Location.Tracker;
import com.favoreme.favore.Models.Post;
import com.favoreme.favore.R;
import com.favoreme.favore.api.Backend;
import com.favoreme.favore.api.Favore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LocationPost extends Fragment {

    ArrayList<Post> posts;
    Backend backend;
    Favore favore;
    ListView lst;
    public LocationPost() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        backend = Backend.get(getContext());
        favore = Favore.get(getContext());

        Tracker t = new Tracker(getContext());
        final ArrayList<Post> posts = new ArrayList<>();
        Location l = t.getLocation();
        try {
            backend.FetchPosts(l.getLongitude(),l.getLatitude()).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    favore.toasty("Unable to fetch the posts!");
                    return;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONArray array = jsonObject.getJSONArray("posts");
                        for (int i = 0;i < array.length(); i++){
                            JSONObject temp = array.getJSONObject(i);
                            // Convert JSON to java objects
                            String img = null;
                            if (temp.getBoolean("isImage")) {
                                img = temp.getString("postImage");
                            }
                            Post post = new Post(
                              temp.getString("id"),
                              temp.getString("poster"),
                                    temp.getString("postText"),
                                    temp.getLong("age"),
                                    temp.getInt("favors"),
                                    temp.getString("posterProfile"),
                                    temp.getBoolean("isImage"),
                                    img
                            );
                            posts.add(post);
                            Log.d("temp", "onResponse: "+post.toString());

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        lst = (ListView) v.findViewById(R.id.post_list);
        CustomAdapter customAdapter = new CustomAdapter(getActivity(),posts,R.layout.entry);
        lst.setAdapter(customAdapter);
        return v;
    }

}
