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
import com.favoreme.favore.api.Backend;
import com.favoreme.favore.api.Favore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SignupActivity extends AppCompatActivity {

    Button btn;
    EditText pass,repass,email;
    ProgressBar progressBar2;
    private Backend backend;
    Favore favore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        backend = Backend.get(getApplicationContext());
        favore = Favore.get(getApplicationContext());
        email = (EditText) findViewById(R.id.semail);
        pass = (EditText) findViewById(R.id.spass);
        repass = (EditText) findViewById(R.id.srpass);
        btn = (Button) findViewById(R.id.signUp);
        progressBar2 = (ProgressBar) findViewById(R.id.signupProgress);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_add = email.getText().toString();
                String password = pass.getText().toString();
                String rpassaword = repass.getText().toString();
                setState(false);
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(rpassaword) || TextUtils.isEmpty(email_add)){
                    Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                    setState(true);
                    return;
                }

                if (!password.equals(rpassaword)){
                    Toast.makeText(getApplicationContext(),"Passwords Doesn't match!",Toast.LENGTH_SHORT).show();
                    setState(true);
                    return;
                }
                if (password.length() < 8){
                    Toast.makeText(getApplicationContext(),"Password must be longer than 8 digits",Toast.LENGTH_SHORT).show();
                    setState(true);
                    return;
                }

                try {
                    backend.Signup(email_add,password).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            favore.toasty("Unable to signup");
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                if (jsonObject.getBoolean("success")){
                                    Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    favore.toasty("Successful signup, login now!");
                                    startActivity(intent);
                                }else{
                                    setState(true);
                                    favore.toasty("Unable to signup! "+jsonObject.getString("msg"));
                                }
                            } catch (JSONException e) {
                                setState(true);
                                favore.toasty("Unable to signup!");
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    setState(true);
                    favore.toasty("Unable to signup!");
                    e.printStackTrace();
                }

            }
        });

    }
    void setState(Boolean state){
        if (state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email.setEnabled(true);
                    pass.setEnabled(true);
                    repass.setEnabled(true);
                    progressBar2.setVisibility(View.GONE);
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    email.setEnabled(false);
                    pass.setEnabled(false);
                    repass.setEnabled(false);
                    progressBar2.setVisibility(View.VISIBLE);
                }
            });
        }

    }
}
