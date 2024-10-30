package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_screen);

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }
}