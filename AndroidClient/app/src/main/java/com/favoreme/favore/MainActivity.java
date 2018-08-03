package com.favoreme.favore;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.favoreme.favore.Fragments.LocationPost;
import com.favoreme.favore.Fragments.UserPostFragment;
import com.favoreme.favore.Location.Tracker;
import com.favoreme.favore.Login.Extra_details_activity;
import com.favoreme.favore.Login.LoginActivity;
import com.favoreme.favore.Models.Loci;
import com.favoreme.favore.Models.Post;
import com.favoreme.favore.Settings.SettingsActivity;
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

public class MainActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dt = new Date();
        favore = Favore.get(this);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom);
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        backend = Backend.get(MainActivity.this);
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
                startActivity(new Intent(MainActivity.this, Extra_details_activity.class));
                break;
            case R.id.app_bar_search:
                Toast.makeText(getApplicationContext(),"This will be implemented in future",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
