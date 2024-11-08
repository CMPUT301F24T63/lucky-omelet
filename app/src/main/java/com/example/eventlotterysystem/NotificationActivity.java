package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {
    private Control control;
    private User curUser;
    private LinearLayout List;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_screen);

        control = Control.getInstance();
        control.setCurrentUser(control.getUserList().get(0));
        curUser = control.getCurrentUser();

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());
//
        List = findViewById(R.id.notificationList);

//        Toast.makeText(this, "aaaaaaa", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, curUser.getNotificationList().get(0).getCustomMessage(), Toast.LENGTH_SHORT).show();

        for (Notification noti : curUser.getNotificationList()) {
            addNotiToSection(noti, List);
        }

    }

    private void addNotiToSection(Notification noti, LinearLayout section) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View NotifiView = inflater.inflate(R.layout.notification_content, section, false);

        TextView titleTextView = NotifiView.findViewById(R.id.notification_title);
        TextView messageTextView = NotifiView.findViewById(R.id.notification_message);
        titleTextView.setText(noti.getEvent().getName());
        messageTextView.setText(noti.getCustomMessage());

        Button AcceptButton = NotifiView.findViewById(R.id.btnAccept);
        Button DeclineButton = NotifiView.findViewById(R.id.btnDecline);
        Button DeleteButton = NotifiView.findViewById(R.id.btnRemove);
//
//
//        if (noti.needAccept()) {
//            AcceptButton.setVisibility(View.INVISIBLE);
//            DeclineButton.setVisibility(View.INVISIBLE);
//        }
//
//        AcceptButton.setOnClickListener(v -> {
//        });
//
//        DeclineButton.setOnClickListener(v -> {
//
//        });

    }
}