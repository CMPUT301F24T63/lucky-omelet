package com.example.eventlotterysystem;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.OnProfileUpdatedListener {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView contactTextView;
    private User curUser;
    private Control control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        Control control = Control.getInstance();
        // Test control data
        Log.i("checkControlData", "Profile Activity Control Data Test");
        Utils.checkControlData(control);
        // try different index here to get different user (this suggest database read is successful)
        // curUser = control.getUserList().get(0);
        // curUser = control.getEventList().get(0).getWaitingList().get(0);// Entrant 5 (This confirms database read is successful!)
        curUser = Control.getCurrentUser();
        Log.i("my index", control.getUserList().indexOf(Control.getCurrentUser())+"");
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        contactTextView = findViewById(R.id.contact);

        nameTextView.setText(curUser.getName());
        emailTextView.setText("Email: " +curUser.getEmail());
        contactTextView.setText("Contact: " +curUser.getContact());

        if ("000-000-0000".equals(curUser.getContact())) {
            nameTextView.setText(curUser.getName());
            emailTextView.setText("Email: " +curUser.getEmail());
            contactTextView.setText("Contact: " +curUser.getContact());
            openEditProfileFragment();
        } else {
            nameTextView.setText(curUser.getName());
            emailTextView.setText("Email: " +curUser.getEmail());
            contactTextView.setText("Contact: " +curUser.getContact());
        }

        findViewById(R.id.edit_button).setOnClickListener(v -> openEditProfileFragment());

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());

    }

    private void openEditProfileFragment() {
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(
                curUser.getName(),
                curUser.getEmail(),
                curUser.getContact()
        );
        editProfileFragment.setOnProfileUpdatedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        editProfileFragment.show(fragmentManager, "editProfileFragment");
        editProfileFragment.setOnProfileUpdatedListener(this);

    }

    @Override
    public void onProfileUpdated(String name, String email, String contact) {
        curUser.setName(name);
        curUser.setEmail(email);
        curUser.setContact(contact);

        updateProfileUI();
    }


    private void updateProfileUI() {
        nameTextView.setText(curUser.getName());
        emailTextView.setText("Email: " + curUser.getEmail());
        contactTextView.setText("Contact: " + curUser.getContact());
        Utils.checkControlData(Control.getInstance());
        FirestoreManager.getInstance().saveControl(Control.getInstance());
    }
}