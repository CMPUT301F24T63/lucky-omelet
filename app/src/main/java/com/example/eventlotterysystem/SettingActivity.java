package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import com.google.android.material.materialswitch.MaterialSwitch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {
    private MaterialSwitch notiSwitch;
    private Control control;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_screen);

        control = Control.getInstance();
        curUser = control.getCurrentUser();

        notiSwitch = findViewById(R.id.noti_switch);

        notiSwitch.setChecked(curUser.getNotificationSetting());

        notiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            curUser.setNotificationSetting(isChecked);

            FirestoreManager.getInstance().saveControl(control);
        });

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
    }
}
