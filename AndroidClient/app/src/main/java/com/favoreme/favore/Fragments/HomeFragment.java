package com.favoreme.favore.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.favoreme.favore.CustomAdapter;
import com.favoreme.favore.Models.Post;
import com.favoreme.favore.R;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<Post> posts;
    ListView lst;
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        posts=new ArrayList<Post>();
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
