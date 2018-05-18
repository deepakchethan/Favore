package com.favoreme.favore.Login;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.favoreme.favore.R;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText email;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        email = (EditText)findViewById(R.id.emaill);
        btn = (Button)findViewById(R.id.rsetPass);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em = email.getText().toString();

            }
        });

    }
}
