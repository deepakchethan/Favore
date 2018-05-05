package com.favoreme.favore.Login;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import com.favoreme.favore.MainActivity;
import com.favoreme.favore.Models.User;
import com.favoreme.favore.R;
import com.favoreme.favore.api.Backend;
import com.favoreme.favore.api.Favore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Extra_details_activity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private Button sub;
    private EditText fname,lname,dname,pnum,about;
    private ProgressBar progressBar2;
    private CircleImageView profile_pic;
    private String gender = "male";
    private Uri filePath;
    private EditText age;
    private Favore favore;
    private Backend backend;
    public String TAG = "dope";
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_details_activity);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        dname = (EditText) findViewById(R.id.dname);
        pnum = (EditText) findViewById(R.id.pnum);
        sub = (Button) findViewById(R.id.signUp);
        about =(EditText) findViewById(R.id.about);
        age = (EditText) findViewById(R.id.age);
        profile_pic = (CircleImageView)findViewById(R.id.profile_image);
        progressBar2 = (ProgressBar) findViewById(R.id.signupProgress);
        favore=Favore.get(Extra_details_activity.this);
        backend= Backend.get(Extra_details_activity.this);
        User owner = null;
        if (favore.isSyncedIn()){
            owner = favore.getOwner();
            setDetails(owner);
        }

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent();
                cameraIntent.setType("image/png");
                cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(cameraIntent,"Select a jpeg image"), CAMERA_REQUEST);
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = fname.getText().toString();
                String lastName = lname.getText().toString();
                String displayName = dname.getText().toString();
                String phoneNum = pnum.getText().toString();
                String abouts = about.getText().toString();
                String ages = age.getText().toString();
                int id = favore.getOwner().getUid();
                if (TextUtils.isEmpty(firstName) ||TextUtils.isEmpty(lastName) || TextUtils.isEmpty(abouts)|| TextUtils.isEmpty(displayName) ||
                        TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(ages) || TextUtils.isEmpty(gender)){
                        Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                        return;
                }
                // Upload the extra details
                try {
                    backend.EditUser(id,firstName,lastName,displayName,favore.getOwner().getuName(),phoneNum,abouts,ages,path).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            favore.toasty("Could not update your details mate!"+e.getStackTrace().toString());
                            e.printStackTrace();
                            Log.d(TAG, "onFailure: "+e.getStackTrace().toString());
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                if(jsonObject.getBoolean("success")){
                                    favore.toasty("Successfully updated everything");
                                    Intent intent = new Intent(Extra_details_activity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    favore.toasty("Failure could not update your details");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }


                progressBar2.setVisibility(View.VISIBLE);
                startActivity(new Intent(Extra_details_activity.this,MainActivity.class));

            }
        });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null){
                path = getPathFromURI(selectedImageUri);
                profile_pic.setImageURI(selectedImageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setDetails(User owner) {
        fname.setText(owner.getfName());
        lname.setText(owner.getlName());
        dname.setText(owner.getdName());
        pnum.setText(owner.getPhone());
        about.setText(owner.getBio());
        age.setText(owner.getAge()+"");
        RadioButton ml = (RadioButton) findViewById(R.id.male);
        RadioButton fl = (RadioButton) findViewById(R.id.female);
        if (owner.getGender().equals("male")) ml.setChecked(true);
        else fl.setChecked(true);

    }
    public void onRadioButtonClicked(View v){
        switch (v.getId()){
            case R.id.male: gender="male" ;break;
            case R.id.female: gender="female";break;
        }
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


}


