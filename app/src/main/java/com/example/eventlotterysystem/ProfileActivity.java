package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class ProfileActivity extends AppCompatActivity implements EditProfileFragment.OnProfileUpdatedListener {
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView contactTextView;
    private User curUser;
    private Control control = Control.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        curUser = control.getCurrentUser();

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        contactTextView = findViewById(R.id.contact);
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
        FirestoreManager fm = new FirestoreManager();
        fm.saveControl(control);
    }
}