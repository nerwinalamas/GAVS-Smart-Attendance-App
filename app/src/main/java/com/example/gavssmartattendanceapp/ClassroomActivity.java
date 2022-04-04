package com.example.gavssmartattendanceapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gavssmartattendanceapp.adapters.ClassroomAdapter;
import com.example.gavssmartattendanceapp.models.Classroom;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassroomActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton1;
    private DatabaseReference databaseReference;
    private ClassroomAdapter createAdapter;
    private ArrayList<Classroom> classes;
    ArrayList<String> id, className, subjectCode, section;
    private RecyclerView recyclerView;
    private String userId;
    private ValueEventListener classRoomValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        userId = FirebaseAuth.getInstance().getUid();

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_create_classroom);

        floatingActionButton1 = findViewById(R.id.fab_create_classroom);

        id = new ArrayList<>();
        className = new ArrayList<>();
        subjectCode = new ArrayList<>();
        section = new ArrayList<>();

        classes = new ArrayList<>();
        createAdapter = new ClassroomAdapter(classes);

        recyclerView = findViewById(R.id.recyclerView_class);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(createAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ClassroomActivity.this));

        getClassRoom();

        createAdapter.setOnItemClickListener(this::gotoCCActivity);

        //Floating Button
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ClassroomActivity.this);
                dialog.setContentView(R.layout.activity_dialogbox_cc);
                dialog.setTitle("Create Classroom");

                TextInputLayout className = (TextInputLayout) dialog.findViewById(R.id.class_name);
                TextInputLayout subjectCode = (TextInputLayout) dialog.findViewById(R.id.subject_code);
                TextInputLayout section = (TextInputLayout) dialog.findViewById(R.id.section);

                Button add = dialog.findViewById(R.id.add);
                Button cancel = dialog.findViewById(R.id.cancel);

                // fab1
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String cn = className.getEditText().getText().toString().trim();
                        String sc = subjectCode.getEditText().getText().toString().trim();
                        String s = section.getEditText().getText().toString().trim();

                        if (cn.isEmpty()) {
                            className.setError("Class Name is required");
                            className.requestFocus();
                            return;
                        }

                        if (sc.isEmpty()) {
                            subjectCode.setError("Subject Code is required");
                            subjectCode.requestFocus();
                            return;
                        }

                        if (s.isEmpty()) {
                            section.setError("Section is required");
                            section.requestFocus();
                            return;
                        }

                        Classroom classroom = new Classroom();
                        classroom.setName(className.getEditText().getText().toString());
                        classroom.setSubjectCode(subjectCode.getEditText().getText().toString());
                        classroom.setSection(section.getEditText().getText().toString());
                        createClass(classroom);
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        //Bottom Navigation Bar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_create_event:
                        startActivity(new Intent(getApplicationContext(), EventRoomActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_classroom:
                        return true;
                    case R.id.btm_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_qrcode:
                        startActivity(new Intent(getApplicationContext(), QRCodeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_appointment:
                        startActivity(new Intent(getApplicationContext(),AppointmentRoomActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void gotoCCActivity(int position) {
        Classroom classroom = classes.get(position);
        Intent intent = new Intent(this, ClassActivity.class);
        intent.putExtra("classname", classroom.getName());
        intent.putExtra("subject", classroom.getSubjectCode());
        intent.putExtra("section", classroom.getSection());
        startActivity(intent);
    }

    private void getClassRoom(){
        classRoomValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes.clear();
                if(snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Classroom classroom = dataSnapshot.getValue(Classroom.class);
                        if(classroom != null) {
                            classroom.setClassId(dataSnapshot.getKey());
                            classes.add(classroom);
                        }
                    }
                }
                createAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference
                .child("Classrooms")
                .child(userId)
                .addValueEventListener(classRoomValueEventListener);
    }

    private void createClass(Classroom classroom){

        databaseReference.child("Classrooms")
                .child(userId)
                .push()
                .setValue(classroom, (error, ref) -> Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        databaseReference
                .child("Classrooms")
                .child(userId)
                .removeEventListener(classRoomValueEventListener);
        super.onDestroy();
    }
}