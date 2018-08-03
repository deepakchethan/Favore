package com.favoreme.favore;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.favoreme.favore.Fragments.FollowerPost;
import com.favoreme.favore.Fragments.LocationPost;
import com.favoreme.favore.Fragments.UserPostFragment;
import com.favoreme.favore.Location.Tracker;
import com.favoreme.favore.Login.Extra_details_activity;
import com.favoreme.favore.Login.LoginActivity;
import com.favoreme.favore.Models.Post;
import com.favoreme.favore.api.Backend;
import com.favoreme.favore.api.Favore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PrimaryActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    private static final int CAMERA_REQUEST = 999;
    private Button btn;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    String post_text;
    private ListView lst;
    private ArrayList<Post> posts;
    private Date dt;
    public Favore favore;
    public Backend backend;
    public FollowerPost followerPost;
    public LocationPost locationPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);
        followerPost = new FollowerPost();
        locationPost = new LocationPost();
        dt = new Date();
        favore = Favore.get(this);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        backend = Backend.get(PrimaryActivity.this);
        if (!favore.isLoggedIn()){
            //if not signed in, launch the Sign In Activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }else{
            //Initialize few things
            if (!favore.isSyncedIn()){
                //Sync here
            }
        }
        Fragment home = new LocationPost();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,home).commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            //TODO update this mate
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.post:
                        final AlertDialog.Builder alert = new AlertDialog.Builder(PrimaryActivity.this);
                        alert.setTitle("New Post!");
                        alert.setMessage("What do you want to post!");
                        alert.setPositiveButton("Image", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent cameraIntent = new Intent();
                                cameraIntent.setType("image/png");
                                cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(cameraIntent,"What do you want to share?"),CAMERA_REQUEST);


                            }
                        });
                        alert.setNegativeButton("Text", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder talert = new AlertDialog.Builder(PrimaryActivity.this);
                                talert.setTitle("Text Post!");
                                talert.setMessage("What's on your mind?");
                                final EditText input = new EditText(PrimaryActivity.this);
                                talert.setView(input);
                                talert.setPositiveButton("Post", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        post_text = input.getText().toString();
                                        Tracker t = new Tracker(getApplicationContext());
                                        Location l = t.getLocation();
                                        Toast.makeText(getApplicationContext(),t.getLocation()+" ",Toast.LENGTH_SHORT).show();
                                        if (l == null)  return;
                                        try {
                                            backend.WritePost(l.getLongitude(),l.getLatitude(),post_text,favore.getOwner().getUid(),(new Date()).getTime()).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    favore.toasty("Unable to post it");
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                                        if (jsonObject.getBoolean("success")){
                                                            favore.toasty("Uploaded the post mate");
                                                        }else{
                                                            favore.toasty(jsonObject.getString("msg"));
                                                        }
                                                    } catch (JSONException e) {
                                                        favore.toasty("Someone");
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        } catch (IOException e) {
                                            favore.toasty("IOEx");
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                talert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                talert.show();
                            }
                        });
                        alert.show();
                        break;
                    case R.id.home:
                        tabLayout.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle("Home");
                        Fragment home = locationPost;
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,home).commit();
                        break;
                    case R.id.profile:
                        tabLayout.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("My Posts");
                        Fragment frag = new UserPostFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                        break;
                }
                return false;
            }
        });
        }





    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        LocationPost locationPost;
        FollowerPost followerPost;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            locationPost = new LocationPost();
            followerPost = new FollowerPost();

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) return locationPost;
            else return followerPost;

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null){
                String path = getPathFromURI(selectedImageUri);
                Tracker t = new Tracker(getApplicationContext());
                Location l = t.getLocation();
                try {
                    backend.WriteImagePost(l.getLongitude(),l.getLatitude(),favore.getOwner().getUid(),(new Date()).getTime(),path).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            favore.toasty("Unable to post this!");
                            e.printStackTrace();
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                favore.toasty(jsonObject.toString());
                                if (jsonObject.getBoolean("success")){
                                    favore.toasty("Successfully uploaded the post mate!");
                                }else{
                                    favore.toasty(jsonObject.getString("msg"));
                                }
                            } catch (JSONException e) {
                                favore.toasty("JSON exception man!");
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getPathFromURI(Uri contentUri){
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri,proj,null,null,null);
        if (cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(PrimaryActivity.this, Extra_details_activity.class));
                break;
            case R.id.app_bar_search:
                Toast.makeText(getApplicationContext(),"This will be implemented in future",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
