package com.kfs.cardiaccustomercare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

            //to change the color of progress bar because the attribute in xml that change the color cause problems with our API
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do any action here. Now we are moving to next page
                    startActivity(new Intent(SplashActivity.this , Login.class));
                    finish();
                }

            }, 2000);
    }
}
