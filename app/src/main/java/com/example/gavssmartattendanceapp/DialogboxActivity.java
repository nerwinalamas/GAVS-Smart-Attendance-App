package com.example.gavssmartattendanceapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.annotations.Nullable;

public class DialogboxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialogbox_cc);
        setContentView(R.layout.activity_dialogbox_ce);
        setContentView(R.layout.activity_dialogbox_dv);
    }
}
