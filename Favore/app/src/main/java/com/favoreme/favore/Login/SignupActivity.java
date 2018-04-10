package com.favoreme.favore.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.favoreme.favore.MainActivity;
import com.favoreme.favore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    Button btn;
    EditText fname,lname,dname,pass,repass,pcode,pnum,email;
    ProgressBar progressBar2;
    private FirebaseAuth auth;
    public static String semail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        btn = (Button)findViewById(R.id.signupbutton);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        repass = (EditText) findViewById(R.id.rpass);

        btn = (Button) findViewById(R.id.signUp);
        progressBar2 = (ProgressBar) findViewById(R.id.signupProgress);
        pcode.setText("+91");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_add = email.getText().toString();
                String password = pass.getText().toString();
                String rpassaword = repass.getText().toString();

                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(rpassaword) || TextUtils.isEmpty(email_add)){
                    Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rpassaword)){
                    Toast.makeText(getApplicationContext(),"Passwords Doesn't match!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8){
                    Toast.makeText(getApplicationContext(),"Password must be longer than 8 digits",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar2.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email_add,password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    Toast.makeText(SignupActivity.this,"Unable to create user",Toast.LENGTH_SHORT);
                                }else{
                                    Toast.makeText(SignupActivity.this,"Created User",Toast.LENGTH_SHORT);
                                    startActivity(new Intent(SignupActivity.this, Extra_details_activity.class));
                                    finish();
                                }
                            }
                        });
                semail = email_add;

            }
        });

    }
}
