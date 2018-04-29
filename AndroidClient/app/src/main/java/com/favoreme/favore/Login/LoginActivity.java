package com.favoreme.favore.Login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


public class LoginActivity extends AppCompatActivity {

    private Button sign_up,log_in,forgot_pass;
    private EditText usrName,pasWord;
    private ProgressBar progressBar;
    private Backend backend;
    private Favore favore;
    private  String TAG="Login";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiating the objects
        sign_up = (Button) findViewById(R.id.signupbutton);
        log_in = (Button) findViewById(R.id.signin);
        usrName = (EditText) findViewById(R.id.email);
        pasWord = (EditText)findViewById(R.id.password);
        forgot_pass = (Button) findViewById(R.id.forgotPassword);
        progressBar =(ProgressBar) findViewById(R.id.loginProgress);

        backend = Backend.get(getApplicationContext());
        favore = Favore.get(getApplicationContext());

        // Onclick Listener
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = usrName.getText().toString();
                String pas = pasWord.getText().toString();
                setState(false);
                if (TextUtils.isEmpty(usr) ||TextUtils.isEmpty(pas)){
                    Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                    setState(true);
                    return;
                }
                //Put your signin here
                try {
                    backend.Signin(usr,pas).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            setState(true);
                            favore.toasty("Unable to singin");
                        }

                        @Override
                        public void onResponse(Call call, Response dope) throws IOException {
                            try {
                                JSONObject jsonObject = new JSONObject(dope.body().string());
                                if (jsonObject.getBoolean("success")){
                                    String jwt = jsonObject.getString("token").split(" ")[1];
                                    favore.logIn(jwt);
                                    favore.toasty("Logged In "+jwt);
                                    Intent i = new Intent(LoginActivity.this, Extra_details_activity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                }else{
                                    favore.toasty(jsonObject.getString("msg"));
                                    setState(true);
                                }
                            } catch (JSONException e) {
                                setState(true);
                                favore.toasty("Unable to sign in");
                            }
                        }
                    });
                } catch (IOException e) {
                    setState(true);
                    favore.toasty("Unable to sign in");
                }

            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void setState(Boolean state){
        if (state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usrName.setEnabled(true);
                    pasWord.setEnabled(true);
                    log_in.setEnabled(true);
                    sign_up.setEnabled(true);
                    forgot_pass.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                }
            });


        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    usrName.setEnabled(false);
                    pasWord.setEnabled(false);
                    log_in.setEnabled(false);
                    sign_up.setEnabled(false);
                    forgot_pass.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }


}
