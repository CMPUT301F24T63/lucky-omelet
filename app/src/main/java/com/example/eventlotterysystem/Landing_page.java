package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Landing_page extends AppCompatActivity {

    private static final int DOUBLE_BACK_TIME = 2000; // Time in milliseconds
    private long lastBackPressedTime = 0;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);


        FirestoreManager.getInstance().loadNotifications(Control.getInstance());

        if (Control.getCurrentUser() == null){
            checkDevice(Control.getInstance());
        }
//        else {
//            int currentUserID = Control.getCurrentUser().getUserID();
//            Control control = new Control();
//            Control.setInstance(control);
//            FirestoreManager.getInstance().loadControl(Control.getInstance());
//            for (User user : Control.getInstance().getUserList()) {
//                if (user.getUserID() == currentUserID) {
//                    Control.setCurrentUser(user);
//                    break;
//                }
//            }
//        }

        // Set up the OnBackPressedCallback
        OnBackPressedDispatcher dispatcher = this.getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (lastBackPressedTime + DOUBLE_BACK_TIME > System.currentTimeMillis()) {
                    finishAffinity(); // Exit the app
                } else {
                    Toast.makeText(Landing_page.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                    lastBackPressedTime = System.currentTimeMillis();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {setEnabled(true);}
                    }, DOUBLE_BACK_TIME);
                }
            }
        });



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
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        ImageView SettingIcon = findViewById(R.id.settingsIcon);
        SettingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Landing_page.this, SettingActivity.class);
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
                Intent intent = new Intent(Landing_page.this, facilityActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null); // Clean up any pending callbacks
        super.onDestroy();
    }

    protected void checkDevice(Control control){
        Log.i("checkDevice", "checkDevice function Control Data Test");
        Utils.checkControlData(Control.getInstance());
        for (User user : Control.getInstance().getUserList()) {
            if (user.getFID().equals(Control.getLocalFID())) {
                Control.setCurrentUser(user);
                return;
            }
        }
        if (Control.getCurrentUser() == null){
            User me = new User(Control.getInstance().getUserIDForUserCreation());
            me.setFID(Control.getLocalFID());
            Control.getInstance().getUserList().add(me);
            Control.setCurrentUser(me);
            // Just don't save... Saving is causing the app to crash
            // FirestoreManager.getInstance().saveControl(control);
            // FirestoreManager.getInstance().saveUser(me);
        }

        Log.i("checkDevice", "After checkDevice function Control Data Test");
        Utils.checkControlData(Control.getInstance());
        FirestoreManager.getInstance().saveControl(Control.getInstance());
        // Or set user by using index: 0: entrant   10: organizer   11: admin
        // Control.setCurrentUser(control.getUserList().get(0));

    }
}





