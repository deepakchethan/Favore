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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Extra_details_activity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private Button sub;
    private EditText fname,lname,dname,pnum,about;
    private ProgressBar progressBar2;
    private DatabaseReference fdb;
    private CircleImageView profile_pic;
    private Bitmap photo;
    private String gender;
    private FirebaseAuth auth;
    private StorageReference ref;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_details_activity);
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        fdb = FirebaseDatabase.getInstance().getReference();
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        dname = (EditText) findViewById(R.id.dname);
        pnum = (EditText) findViewById(R.id.pnum);
        sub = (Button) findViewById(R.id.signUp);
        about =(EditText) findViewById(R.id.about);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser usr = auth.getCurrentUser();
        ref = FirebaseStorage.getInstance().getReference();
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
                StorageReference riversRef = ref.child("pro_pics/"+uid+".jpg");
                final UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                riversRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                @SuppressWarnings("VisibleForTests") Uri downloaduri = taskSnapshot.getDownloadUrl();
                                builder.setPhotoUri(downloaduri);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                               Toast.makeText(getApplicationContext(),"Unable to upload your profile",Toast.LENGTH_SHORT);
                            }
                        });
                builder.setDisplayName(displayName);

                UserProfileChangeRequest profileUpdates = builder.build();
                usr.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Profile Updated",Toast.LENGTH_SHORT);
                                }
                            }
                        });

                //Push to the online place
                User doper = new User(firstName,lastName,phoneNum,gender,abouts);
                fdb.child("Users").child(uid).child("Details").setValue(doper);
                startActivity(new Intent(Extra_details_activity.this,MainActivity.class));

            }
        });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK &&  data.getData() != null){
            filePath = data.getData();
            try{
                photo = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profile_pic.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRadioButtonClicked(View v){
        switch (v.getId()){
            case R.id.male: gender="Male" ;break;
            case R.id.female: gender="Female";break;
        }
    }


}


