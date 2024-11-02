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

        curUser = control.getUserList().get(control.getCurrentUserID());

        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);
        contactTextView = findViewById(R.id.contact);

        nameTextView.setText(curUser.getFirstName() + " " + curUser.getLastName());
        emailTextView.setText("Email: " +curUser.getEmail());
        contactTextView.setText("Contact: " +curUser.getContact());

        updateProfileUI();

        findViewById(R.id.edit_button).setOnClickListener(v -> openEditProfileFragment());

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());

    }

    private void openEditProfileFragment() {
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(
                curUser.getFirstName(),
                curUser.getLastName(),
                curUser.getEmail(),
                curUser.getContact()
        );
        editProfileFragment.setOnProfileUpdatedListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        editProfileFragment.show(fragmentManager, "editProfileFragment");
        editProfileFragment.setOnProfileUpdatedListener(this);

    }

    @Override
    public void onProfileUpdated(String firstName, String lastName, String email, String contact) {
        curUser.setFirstName(firstName);
        curUser.setLastName(lastName);
        curUser.setEmail(email);
        curUser.setContact(contact);

        updateProfileUI();
    }


    private void updateProfileUI() {
        String fullName = curUser.getFirstName() + " " + curUser.getLastName();
        nameTextView.setText(fullName);
        emailTextView.setText("Email: " + curUser.getEmail());
        contactTextView.setText("Contact: " + curUser.getContact());
        FirestoreManager fm = new FirestoreManager();
        fm.saveControl(control);
    }
}