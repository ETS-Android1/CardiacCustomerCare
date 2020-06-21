package com.kfs.cardiaccustomercare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.material.tabs.TabLayout;
import com.kfs.cardiaccustomercare.Fragements.SimplePageAdapter;

public class Home extends AppCompatActivity {

    Button start , stop ,logout;
    String path = Environment.getExternalStorageDirectory().getPath()+"/MyRecords.wav";

    SharedPreferencesConfig sharedPreferencesConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);


            Toast.makeText(getApplicationContext(), "kosom 7ayaty Etneen", Toast.LENGTH_LONG).show();
        }


        SimplePageAdapter adapter=new SimplePageAdapter(getSupportFragmentManager());
        ViewPager viewPager=findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout=findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);







        sharedPreferencesConfig = new SharedPreferencesConfig(getApplicationContext());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=new MenuInflater(this);
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch(item.getItemId())
        {
            case R.id.setting:
                intent=new Intent(Home.this,Settings.class);
                startActivity(intent);
                break;
            case R.id.log_out:
                LogOut();
            break;
        }
        return true;
    }

    private void LogOut() {
        sharedPreferencesConfig.writeLoginStatus(false);
        LoginManager.getInstance().logOut();
        startActivity(new Intent(Home.this,Login.class));
    }
}
