package com.favoreme.favore;


import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.favoreme.favore.Models.Post;
import java.util.ArrayList;

/**
 * This class contans the adapter for the custom list view
 */

public class CustomAdapter  extends ArrayAdapter<Post>{


    private Activity context;
    ArrayList<Post> posts;
    public CustomAdapter(Activity context, ArrayList<Post> posts, int resource) {
        super(context, resource,posts);
        this.context = context;
        this.posts = posts;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder = null;
        if (v == null){
            v = context.getLayoutInflater().inflate(R.layout.entry,parent,false);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) v.getTag();
        }
        Post post = posts.get(position);
        viewHolder.poster.setText(post.getPoster().toString());
        viewHolder.post_text.setText(post.getPost().toString());


        return v;
    }

    class ViewHolder{
        TextView poster;
        TextView post_text;
        ImageView profile;

        ViewHolder(View v){
            poster=(TextView)v.findViewById(R.id.poster);
            post_text=(TextView)v.findViewById(R.id.post_text);

        }
    }
}
