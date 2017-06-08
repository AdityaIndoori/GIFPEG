package com.indoori.aditya.gifpeg;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class A_SplashScreen extends AppCompatActivity {
    public static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final int interval = 2000; // 1 Second
        Handler handler = new Handler();
        Runnable runnable = new Runnable(){
            public void run() {
                Intent intent = new Intent(getApplicationContext(),B_LoginActivity.class);
                finish();
                startActivity(intent);
            }
        };
        handler.postAtTime(runnable, System.currentTimeMillis()+interval);
        handler.postDelayed(runnable, interval);
    }

    public static void setToast(String toastMessage, Context context) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
