package com.favoreme.favore.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.favoreme.favore.CustomAdapter;
import com.favoreme.favore.Models.Post;
import com.favoreme.favore.R;

import java.util.ArrayList;

public class UserPostFragment extends Fragment {


    private ListView lst;
    public UserPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_user_post, container, false);
        lst = (ListView) v.findViewById(R.id.post_list);
        ArrayList<Post> posts = new ArrayList<Post>();
       // CustomAdapter customAdapter = new CustomAdapter(getActivity(),posts,R.layout.entry);
        //lst.setAdapter(customAdapter);
        return v;
    }

}
