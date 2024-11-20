package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminViewFacilityActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView descriptionTextView;
    private Facility facility;

    private ImageView deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_facility);

        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.description);
        deleteButton = findViewById(R.id.del_button);

        // Get facility details from Intent
        facility = (Facility) getIntent().getSerializableExtra("facility");

        // Display facility details
        if (facility != null) {
            nameTextView.setText("Facility Name: " + facility.getName());
            descriptionTextView.setText("Facility Description: " + facility.getDescription());
        }

        // Set up back button listener
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(AdminViewFacilityActivity.this)
                    .setTitle("Delete Facility (Admin)")
                    .setMessage("Are you sure you want to delete this facility as Admin?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        User currentUser = Control.getCurrentUser();
                        if (facility != null && currentUser != null) {
                            currentUser.adminDeleteFacility(Control.getInstance(), facility);
                            FirestoreManager.getInstance().saveControl(Control.getInstance());
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


    }
}