package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private Switch notiSwitch;
    private Control control = Control.getInstance();
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_screen);

        curUser = control.getCurrentUser();

        notiSwitch = findViewById(R.id.noti_switch);

        notiSwitch.setChecked(curUser.getNotificationSetting());

        notiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            curUser.setNotificationSetting(isChecked);

            FirestoreManager fm = new FirestoreManager();
            fm.saveControl(control);
        });

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }
}
