package com.example.gavssmartattendanceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeActivity extends AppCompatActivity {

    ImageView generate_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        generate_img = (ImageView) findViewById(R.id.generate_qrcode);
        displayQrCode();

        BottomNavigationView bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setSelectedItemId(R.id.btm_create_qrcode);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.btm_create_event:
                        startActivity(new Intent(getApplicationContext(), EventRoomActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_classroom:
                        startActivity(new Intent(getApplicationContext(), ClassroomActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.btm_create_qrcode:
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

    private void displayQrCode(){
        QRCodeWriter QR = new QRCodeWriter();
        try {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            BitMatrix bitMatrix = QR.encode(userId,
                    BarcodeFormat.QR_CODE, 200, 200);

            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
            for (int x = 0; x<200; x++){
                for (int y=0; y<200; y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }
            generate_img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}