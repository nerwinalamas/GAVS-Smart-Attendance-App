package com.example.gavssmartattendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginAndSignUpActivity extends AppCompatActivity {

    Button signup, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);

        signup.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(),SignUpActivity.class);
            startActivity(i);
        });

        login.setOnClickListener(v -> {
            Intent i = new Intent(getBaseContext(),LoginActivity.class);
            startActivity(i);
        });
    }
}