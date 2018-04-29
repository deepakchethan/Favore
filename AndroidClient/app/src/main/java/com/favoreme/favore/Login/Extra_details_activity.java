package com.favoreme.favore.Login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.favoreme.favore.MainActivity;
import com.favoreme.favore.Models.User;
import com.favoreme.favore.R;


import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Extra_details_activity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private Button sub;
    private EditText fname,lname,dname,pnum,about;
    private ProgressBar progressBar2;
    private CircleImageView profile_pic;
    private String gender;
    private Uri filePath;

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
        profile_pic = (CircleImageView)findViewById(R.id.profile_image);
        progressBar2 = (ProgressBar) findViewById(R.id.signupProgress);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent();
                cameraIntent.setType("image/*");
                cameraIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(cameraIntent,"Select an image"), CAMERA_REQUEST);
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
                if (TextUtils.isEmpty(firstName) ||TextUtils.isEmpty(lastName) || TextUtils.isEmpty(displayName) ||
                        TextUtils.isEmpty(phoneNum)){
                        Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                        return;
                }
                progressBar2.setVisibility(View.VISIBLE);
                startActivity(new Intent(Extra_details_activity.this,MainActivity.class));

            }
        });
        }

    public void onRadioButtonClicked(View v){
        switch (v.getId()){
            case R.id.male: gender="Male" ;break;
            case R.id.female: gender="Female";break;
        }
    }


}


