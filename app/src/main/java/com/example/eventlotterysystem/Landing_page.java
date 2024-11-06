package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Landing_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        if (Control.getCurrentUser() == null){
            checkDevice(Control.getInstance());
        } else {
            int currentUserID = Control.getCurrentUser().getUserID();
            Control control = new Control();
            Control.setInstance(control);
            FirestoreManager.getInstance().loadControl(Control.getInstance());
            for (User user : Control.getInstance().getUserList()) {
                if (user.getUserID() == currentUserID) {
                    Control.setCurrentUser(user);
                    break;
                }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.landing_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set OnClickListener on eventsIcon
        ImageView eventsIcon = findViewById(R.id.eventsIcon);
        eventsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, EventslistActivity.class);
                startActivity(intent);
            }
        });

        ImageView SettingIcon = findViewById(R.id.settingsIcon);
        SettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, SettingActivity.class); // have not done
                startActivity(intent);
            }
        });

        ImageView profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView notificationIcon = findViewById(R.id.notificationsIcon);
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        ImageView facilityIcon = findViewById(R.id.facilitiesIcon);
        facilityIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, facilityActivity.class); // have not done
                startActivity(intent);
            }
        });

    }

    protected void checkDevice(Control control){
        Log.i("checkDevice", "checkDevice function Control Data Test");
        Utils.checkControlData(control);
        for (User user : control.getUserList()) {
            if (user.getFID().equals(Control.getLocalFID())) {
                Control.setCurrentUser(user);
                return;
            }
        }
        if (Control.getCurrentUser() == null){
            User me = new User(control.getUserIDForUserCreation());
            me.setFID(Control.getLocalFID());
            control.getUserList().add(me);
            Control.setCurrentUser(me);
            // Just don't save... Saving is causing the app to crash
            // FirestoreManager.getInstance().saveControl(control);
            // FirestoreManager.getInstance().saveUser(me);
        }
        Log.i("checkDevice", "After checkDevice function Control Data Test");
        Utils.checkControlData(control);
        FirestoreManager.getInstance().saveControl(control);
        // Or set user by using index: 0: entrant   10: organizer   11: admin
        // Control.setCurrentUser(control.getUserList().get(0));

    }
}





