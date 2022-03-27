package com.example.gavssmartattendanceapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gavssmartattendanceapp.adapters.AppointmentAdapter;
import com.example.gavssmartattendanceapp.models.Appointments;
import com.example.gavssmartattendanceapp.models.HealthForm;
import com.example.gavssmartattendanceapp.models.ScannedAppointment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AppointmentActivity extends AppCompatActivity implements AppointmentAdapter.OnItemClickListener {

    Button btn_scan;
    RecyclerView recyclerView;
    AppointmentAdapter appointmentAdapter;
    Appointments appointments;
    ArrayList<HealthForm> scannedAppointments;
    ValueEventListener scannedAppointmentListener;
    DatabaseReference databaseReference;
    String userId;

    private WritableWorkbook writableWorkbook;

    String TAG = "AppointmentPage2ActivityTAG";

    FloatingActionButton fab_dl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        Log.d(TAG, "onCreate: ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        appointments = (Appointments) getIntent().getSerializableExtra("appointment");

        scannedAppointments = new ArrayList<>();

        TextView tvRoomName = findViewById(R.id.tv_rname_page2);
        TextView tvSubName = findViewById(R.id.tv_sname_page2);
        TextView tvTime = findViewById(R.id.tv_time_page2);

        fab_dl = (FloatingActionButton) findViewById (R.id.download_btn_appointment);

        tvRoomName.setText(appointments.getApRoomName());
        tvSubName.setText(appointments.getApSubName());
        tvTime.setText(appointments.getApTime());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_room);
        recyclerView.setHasFixedSize(true);

        btn_scan = (Button) findViewById(R.id.btn_scan_appointment);

        appointmentAdapter = new AppointmentAdapter(scannedAppointments, this);
        recyclerView.setAdapter(appointmentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AppointmentActivity.this));

        getScannedAppointments();

        btn_scan.setOnClickListener(view -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator(AppointmentActivity.this);
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.setBeepEnabled(true);
            intentIntegrator.setCaptureActivity(ScanQR3.class);
            intentIntegrator.initiateScan();
        });

        //APPOINTMENT DOWNLOAD BUTTON
        fab_dl.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }else{
                    createExcelSheet();
                }
            }else{
                createExcelSheet();
            }
        });
    }

    private void createExcelSheet(){
        Log.d(TAG, "Creating Excel File");
        String csvFile = appointments.getApRoomName()+".xls";
//        val  = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$csvFile")
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        try {
            ContentResolver resolver = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.MediaColumns.DISPLAY_NAME, appointments.getApRoomName());
            cv.put(MediaStore.MediaColumns.MIME_TYPE, "application/vnd.ms-excel");
            cv.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = resolver.insert(MediaStore.Files.getContentUri("external"), cv);
            OutputStream outputStream = resolver.openOutputStream(uri);

            writableWorkbook = Workbook.createWorkbook(outputStream, wbSettings);
            WritableSheet sheet = writableWorkbook.createSheet("sheet1", 0);
            sheet.addCell(new Label(0, 0, "Name"));
            sheet.addCell(new Label(1, 0, "Address"));
            sheet.addCell(new Label(2, 0, "Contact"));
            sheet.addCell(new Label(3, 0, "Purpose of Visit"));
            sheet.addCell(new Label(4, 0, "Temp"));
            sheet.addCell(new Label(5, 0, "Question #1"));
            sheet.addCell(new Label(6, 0, "Question #2"));
            sheet.addCell(new Label(7, 0, "Question #3"));
            sheet.addCell(new Label(8, 0, "Question #4"));
            sheet.addCell(new Label(9, 0, "Question #5"));

            for(int i = 0; scannedAppointments.size() > i; i++){
                HealthForm sf = scannedAppointments.get(i);
                int position = i+1;
                sheet.addCell(new Label(0, position, sf.getHfName()));
                sheet.addCell(new Label(1, position, sf.getHfAddress()));
                sheet.addCell(new Label(2, position, sf.getHfContact()));
                sheet.addCell(new Label(3, position, sf.getHfPurposeOfVisit()));
                sheet.addCell(new Label(4, position, sf.getHfTemperature()));
                sheet.addCell(new Label(5, position, sf.getHfQuestion1()));
                sheet.addCell(new Label(6, position, sf.getHfQuestion2()));
                sheet.addCell(new Label(7, position, sf.getHfQuestion3()));
                sheet.addCell(new Label(8, position, sf.getHfQuestion4()));
                sheet.addCell(new Label(9, position, sf.getHfQuestion5()));
            }

            writableWorkbook.write();
            writableWorkbook.close();
            Toast.makeText(this, "Successfully downloaded!", Toast.LENGTH_LONG).show();;
        } catch (Exception e) {
            Log.d(TAG, "createExcelSheet: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            createExcelSheet();
        }else
            Toast.makeText(this, "Please grant permission to proceed.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                makeText(getApplicationContext(), "No result found", LENGTH_SHORT).show();
            } else {
                String scannedAppointments = result.getContents();
                Log.d(TAG, "User id: " + scannedAppointments);
                getVisitorsInfo(scannedAppointments);
//                getDatabasePath("scan_appointments");
//                getScannedAppointments();
//                saveScannedAppointments();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveScannedAppointments(HealthForm healthForm){
        ScannedAppointment scannedAppointment = new ScannedAppointment();
        scannedAppointment.setAppointments(appointments);
        scannedAppointment.setHealthForm(healthForm);
        databaseReference
                .child("scan_appointments")
                .child(appointments.getAppointmentId())
                .child(healthForm.getHfUserId())
                .setValue(scannedAppointment, (error, ref) -> {
                    Log.d(TAG, "saveScannedAppointments: ");
                });
    }

    private void getScannedAppointments(){
        scannedAppointmentListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scannedAppointments.clear();
                if(snapshot.hasChildren()){
                    for(DataSnapshot child:snapshot.getChildren()){
                        for(DataSnapshot ds:child.getChildren()) {
                            HealthForm sa = ds.getValue(HealthForm.class);
                            if (sa != null && sa.getHfUserId() != null)
                                scannedAppointments.add(sa);
                        }
                    }
                }
                appointmentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        databaseReference
                .child("scan_appointments")
                .child(appointments.getAppointmentId())
                .addValueEventListener(scannedAppointmentListener);
    }

    private void getVisitorsInfo(String userId){
        databaseReference
                .child("Health Declarations")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HealthForm userProfile = snapshot.getValue(HealthForm.class);
                        if(userProfile != null) {
                            userProfile.setHfUserId(snapshot.getKey());
                            saveScannedAppointments(userProfile);
                            Log.d(TAG, "User Full name: " + userProfile.getHfUserId());
                        } else
                            Log.d(TAG, "onDataChange: user is null");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

    @Override
    public void onClick(int position) {
        HealthForm healthForm = scannedAppointments.get(position);
//        scannedAppointments.get(position);
        Intent intent = new Intent(this, VisitorsInfoActivity.class);
        intent.putExtra("temperature", healthForm.getHfTemperature());
        intent.putExtra("name", healthForm.getHfName());
        intent.putExtra("contact", healthForm.getHfContact());
        intent.putExtra("purpose", healthForm.getHfPurposeOfVisit());
        intent.putExtra("address", healthForm.getHfAddress());
        intent.putExtra("question1", healthForm.getHfQuestion1());
        intent.putExtra("question2", healthForm.getHfQuestion2());
        intent.putExtra("question3", healthForm.getHfQuestion3());
        intent.putExtra("question4", healthForm.getHfQuestion4());
        intent.putExtra("question5", healthForm.getHfQuestion5());
        startActivity(intent);
    }

}