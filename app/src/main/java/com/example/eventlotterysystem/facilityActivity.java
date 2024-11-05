package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class facilityActivity extends AppCompatActivity implements EditFacilityFragment.OnFacilityUpdatedListener {
    private TextView nameTextView;
    private TextView descriptionTextView;
    private User curUser;
    private Control control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_profile);
        control = Control.getInstance();
        curUser = control.getCurrentUser();

        // Initialize UI elements
        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.textView2);

        if (curUser.getFacility() == null) {
            openEditFacilityFragment();
        } else {
            updateFacilityUI();
        }

        findViewById(R.id.edit_button).setOnClickListener(v -> openEditFacilityFragment());

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }

    private void openEditFacilityFragment() {
        String facilityName = curUser.getFacility() != null ? curUser.getFacility().getName() : "";
        String facilityDescription = curUser.getFacility() != null ? curUser.getFacility().getDescription() : "";

        EditFacilityFragment editFacilityFragment = EditFacilityFragment.newInstance(
                facilityName,
                facilityDescription
        );
        editFacilityFragment.setOnFacilityUpdatedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        editFacilityFragment.show(fragmentManager, "editFacilityFragment");
    }

    @Override
    public void onFacilityUpdated(String name, String description) {
        if (curUser.getFacility() == null) {
            Facility newFacility = new Facility(name, description, curUser); // Assuming curUser is the creator
            curUser.setFacility(newFacility);
            control.getFacilityList().add(newFacility);
        } else {
            curUser.getFacility().setName(name);
            curUser.getFacility().setDescription(description);
        }

        updateFacilityUI();
    }

    private void updateFacilityUI() {
        if (curUser.getFacility() != null) {
            nameTextView.setText(curUser.getFacility().getName());
            descriptionTextView.setText(curUser.getFacility().getDescription());
            FirestoreManager.getInstance().saveControl(control);
        }
    }

}