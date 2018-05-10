package com.favoreme.favore.Fragments;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class HomeFragment extends Fragment {

    ArrayList<Post> posts;
    Backend backend;
    LocationManager lm;
    Favore favore;
    ListView lst;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);
        backend = Backend.get(getContext());
        favore = Favore.get(getContext());
        posts=new ArrayList<Post>();
        Tracker t = new Tracker(getContext());

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
                        if (jsonObject.getBoolean("success")){
                            favore.toasty("Failed to fetch the posts!");
                        }
                        else{
                            
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        posts.add(new Post("Deepak", "I am so good at this even then I am unable to get any kind of kinky values"));
        posts.add(new Post("Chethan", "I am awesome"));
        posts.add(new Post("Deepak Chethan", "I am awesome"));
        posts.add(new Post("DC", "I am awesome"));
        posts.add(new Post("dodococo", "I am awesome too"));
        posts.add(new Post("dodococo","I am awesome too"));
        lst = (ListView) v.findViewById(R.id.post_list);

        CustomAdapter customAdapter = new CustomAdapter(getActivity(),posts,R.layout.entry);
        lst.setAdapter(customAdapter);
        return v;
    }

}
