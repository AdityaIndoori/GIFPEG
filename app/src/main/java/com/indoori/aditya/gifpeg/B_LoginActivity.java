package com.indoori.aditya.gifpeg;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class B_LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button fb_login,google_login,skip_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }
        fb_login= (Button) findViewById(R.id.button_fbLogin);
        google_login = (Button) findViewById(R.id.button_googleLogin);
        skip_login = (Button) findViewById(R.id.button_skipLogin);

        fb_login.setOnClickListener(this);
        google_login.setOnClickListener(this);
        skip_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.button_fbLogin:
                A_SplashScreen.setToast("Facebook Login",getApplicationContext());
                break;
            case R.id.button_googleLogin:
                A_SplashScreen.setToast("Google+ Login",getApplicationContext());
                break;
            case R.id.button_skipLogin:
                Intent intent = new Intent(getApplicationContext(),C_NavigationDrawerActivity.class);
                finish();
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
