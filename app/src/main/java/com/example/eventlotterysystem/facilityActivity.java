package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * An Activity that displays and allows editing of the user's facility information.
 */
public class facilityActivity extends AppCompatActivity implements EditFacilityFragment.OnFacilityUpdatedListener {
    private TextView nameTextView;
    private TextView descriptionTextView;
    private User curUser;
    private Control control;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_profile);
        control = Control.getInstance();
        curUser = control.getCurrentUser();

        // Initialize UI elements
        nameTextView = findViewById(R.id.name);
        descriptionTextView = findViewById(R.id.email); // Assuming 'email' is a typo and should be 'description'

        // If the user has no facility, prompt to create one
        if (curUser.getFacility() == null) {
            openEditFacilityFragment();
        } else {
            updateFacilityUI();
        }

        // Set up edit button listener
        findViewById(R.id.edit_button).setOnClickListener(v -> openEditFacilityFragment());

        // Set up return button listener
        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }

    /**
     * Opens the EditFacilityFragment to allow the user to create or edit their facility information.
     */
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

    /**
     * Callback method invoked when the facility has been updated.
     *
     * @param name        The updated name of the facility.
     * @param description The updated description of the facility.
     */
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

    /**
     * Updates the UI elements to reflect the latest facility information.
     */
    private void updateFacilityUI() {
        if (curUser.getFacility() != null) {
            nameTextView.setText(curUser.getFacility().getName());
            descriptionTextView.setText(curUser.getFacility().getDescription());
            FirestoreManager.getInstance().saveControl(control);
        }
    }

}