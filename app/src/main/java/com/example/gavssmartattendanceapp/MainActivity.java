package com.example.gavssmartattendanceapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //ICCT LOGO - SPLASH SCREEN
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(() -> {

            try {
                Thread.sleep(2000);

                Intent i = new Intent(getBaseContext(), SecondSplashScreenActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.start();
    }
}