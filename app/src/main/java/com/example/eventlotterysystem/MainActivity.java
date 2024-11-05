// Do not use any data fetched from database in MainActivity.
// Somehow this is causing the app to crash.
// You can start to use control data in other activities.
// You can try to do this by uncomment line 45 (checkDevice function call).
// This will cause the app to crash because the in checkDevice function,
// we tried to use data in userList read from database. (control.getUserList().get(index))

package com.example.eventlotterysystem;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private Control control; // Instance of Control
    private User curUser;
    private CollectionReference facRef;
    private Facility curFac;
    private CollectionReference eventRef;
    private Event curEvent;
    private FirestoreManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Control control = Control.getInstance();

        // Refresh Database data
//        Utils.setUpMockData(control);
//        fm = new FirestoreManager();
//        fm.saveControl(control);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_device);

        fm = FirestoreManager.getInstance();
        fm.loadControl(control);

        Button checkDeviceButton = findViewById(R.id.check_device_button);
        checkDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Landing_page.class);
                startActivity(intent);
            }
        });


    }



}