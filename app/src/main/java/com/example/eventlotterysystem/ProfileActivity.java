package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }
}
