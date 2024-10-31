package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EditFacilityActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_facility_fragment);

        // Finish Button
        Button finishButton = findViewById(R.id.finish_button);
        finishButton.setOnClickListener(v -> {
            //  saving facility details here
            finish();
        });

        // Cancel Button
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> finish());
    }
}
