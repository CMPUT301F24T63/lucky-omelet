package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        // Start QR code scan
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setCameraId(0); // Use back camera (use 1 for front camera)
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Handle the result of the QR code scan
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String contents = result.getContents();
            if (contents == null || contents.trim().isEmpty()) {
                Toast.makeText(this, "Scan canceled or no data found", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int eventID = Integer.parseInt(contents.trim());
                Toast.makeText(this,eventID , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ViewEventActivity.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid QR code format: " + contents, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No scan data received", Toast.LENGTH_SHORT).show();
        }
    }
}
