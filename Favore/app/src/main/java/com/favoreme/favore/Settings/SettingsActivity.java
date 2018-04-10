package com.favoreme.favore.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.favoreme.favore.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        String[] values = new String[]{"Account Settings"};
    }
}
