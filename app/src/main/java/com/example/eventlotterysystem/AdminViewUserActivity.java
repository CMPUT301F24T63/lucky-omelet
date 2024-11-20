package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminViewUserActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView contactTextView;
    private TextView emailTextView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

        nameTextView = findViewById(R.id.name);
        contactTextView = findViewById(R.id.contact);
        emailTextView = findViewById(R.id.email);

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
    }
}