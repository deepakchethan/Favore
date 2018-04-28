package com.favoreme.favore.Login;

import android.content.Intent;
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
import com.favoreme.favore.R;



public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private Button sign_up,log_in,forgot_pass;
    private EditText usrName,pasWord;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instantiating the objects
        sign_up = (Button) findViewById(R.id.signupbutton);
        log_in = (Button) findViewById(R.id.signin);
        usrName = (EditText) findViewById(R.id.email);
        pasWord = (EditText)findViewById(R.id.pass);
        forgot_pass = (Button) findViewById(R.id.forgotPassword);
        progressBar =(ProgressBar) findViewById(R.id.loginProgress);

        // Onclick Listener
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = usrName.getText().toString();
                String pas = pasWord.getText().toString();
                if (TextUtils.isEmpty(usr) ||TextUtils.isEmpty(pas)){
                    Toast.makeText(getApplicationContext(),"All fields are mandatory mate",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //Put your signin here

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


}
