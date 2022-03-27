package com.example.gavssmartattendanceapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VisitorsInfoActivity extends AppCompatActivity {

    TextView temperature, name, contact, address, purposeOfVisit, q1, q2, q3, q4, q5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors_info);

        temperature = findViewById(R.id.vi_temperature);
        name = findViewById(R.id.vi_name);
        contact = findViewById(R.id.vi_contact);
        address = findViewById(R.id.vi_address);
        purposeOfVisit = findViewById(R.id.vi_purpose_of_visit);
        q1 = findViewById(R.id.vi_question1);
        q2 = findViewById(R.id.vi_question2);
        q3 = findViewById(R.id.vi_question3);
        q4 = findViewById(R.id.vi_question4);
        q5 = findViewById(R.id.vi_question5);

        String Vtemp = getIntent().getStringExtra("temperature");
        String Vname = getIntent().getStringExtra("name");
        String Vcontact = getIntent().getStringExtra("contact");
        String Vpurpose = getIntent().getStringExtra("purpose");
        String Vaddress = getIntent().getStringExtra("address");
        String Vquestion1 = getIntent().getStringExtra("question1");
        String Vquestion2 = getIntent().getStringExtra("question2");
        String Vquestion3 = getIntent().getStringExtra("question3");
        String Vquestion4 = getIntent().getStringExtra("question4");
        String Vquestion5 = getIntent().getStringExtra("question5");

        temperature.setText(Vtemp);
        name.setText(Vname);
        contact.setText(Vcontact);
        purposeOfVisit.setText(Vpurpose);
        address.setText(Vaddress);
        q1.setText(Vquestion1);
        q2.setText(Vquestion2);
        q3.setText(Vquestion3);
        q4.setText(Vquestion4);
        q5.setText(Vquestion5);
    }
}