package com.example.eventlotterysystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * An Activity that displays the user's profile information and allows editing of the profile.
 */
public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.OnProfileUpdatedListener {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView contactTextView;
    private User curUser;

    /**
     * Called when the activity is first created. Initializes the UI and sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        // Test control data
        Log.i("checkControlData", "Profile Activity Control Data Test");
        Utils.checkControlData(Control.getInstance());
        curUser = Control.getCurrentUser();
        Log.i("my index", Control.getInstance().getUserList().indexOf(Control.getCurrentUser())+"");
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        contactTextView = findViewById(R.id.contact);

        // Set initial profile information
        nameTextView.setText(curUser.getName());
        emailTextView.setText("Email: " +curUser.getEmail());
        contactTextView.setText("Contact: " +curUser.getContact());

        // If contact is default, prompt user to edit profile
        if ("000-000-0000".equals(curUser.getContact())) {
            openEditProfileFragment("", "", "");
        } else {
            // Just display the profile unless the user clicks edit
            // openEditProfileFragment(curUser.getName(), curUser.getEmail(), curUser.getContact());
        }

        // Set up edit button listener
        findViewById(R.id.edit_button).setOnClickListener(v -> openEditProfileFragment(
                curUser.getName(), curUser.getEmail(), curUser.getContact()
        ));

        // Set up return button listener
        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());

    }

    /**
     * Opens the EditProfileFragment to allow the user to edit their profile information.
     *
     * @param name    The current name of the user.
     * @param email   The current email of the user.
     * @param contact The current contact information of the user.
     */
    private void openEditProfileFragment(String name, String email, String contact) {
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(name, email, contact);
        editProfileFragment.setOnProfileUpdatedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        editProfileFragment.show(fragmentManager, "editProfileFragment");
    }

    /**
     * Callback method invoked when the profile has been updated.
     *
     * @param name    The updated name of the user.
     * @param email   The updated email of the user.
     * @param contact The updated contact information of the user.
     */
    @Override
    public void onProfileUpdated(String name, String email, String contact) {
        curUser.setName(name);
        curUser.setEmail(email);
        curUser.setContact(contact);

        updateProfileUI();
    }

    /**
     * Updates the UI elements to reflect the latest profile information.
     */
    private void updateProfileUI() {
        nameTextView.setText(curUser.getName());
        emailTextView.setText("Email: " + curUser.getEmail());
        contactTextView.setText("Contact: " + curUser.getContact());
        Utils.checkControlData(Control.getInstance());
        FirestoreManager.getInstance().saveControl(Control.getInstance());
    }
}