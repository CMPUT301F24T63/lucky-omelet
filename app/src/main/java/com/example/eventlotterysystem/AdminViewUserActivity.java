package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AdminViewUserActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView contactTextView;
    private TextView emailTextView;
    private User user;

    private ImageView deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

        nameTextView = findViewById(R.id.name);
        contactTextView = findViewById(R.id.contact);
        emailTextView = findViewById(R.id.email);
        deleteButton = findViewById(R.id.del_button);

        // Get user details from Intent
        user = (User) getIntent().getSerializableExtra("user");

        // Display user details
        if (user != null) {
            nameTextView.setText("Name: " + user.getName());
            contactTextView.setText("Contact: " + user.getContact());
            emailTextView.setText("Email: " + user.getEmail());
        }

        // Set up back button listener
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(AdminViewUserActivity.this)
                    .setTitle("Delete Profile (Admin)")
                    .setMessage("Are you sure you want to delete this Profile as Admin?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        User currentUser = Control.getCurrentUser();
                        if (user != null && currentUser != null) {
                            //currentUser.adminDeleteUser(Control.getInstance(), facility);
                            FirestoreManager.getInstance().saveControl(Control.getInstance());
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}