// Do not use any data fetched from database in MainActivity.
// Somehow this is causing the app to crash.
// You can start to use control data in other activities.
// You can try to do this by uncomment line 45 (checkDevice function call).
// This will cause the app to crash because the in checkDevice function,
// we tried to use data in userList read from database. (control.getUserList().get(index))

package com.example.eventlotterysystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Refresh Database data
//        Utils.setUpMockData(Control.getInstance());
//        FirestoreManager.getInstance().saveControl(Control.getInstance());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_device);

        // Load Control data from Firestore
        FirestoreManager.getInstance().loadControl(Control.getInstance());

        // get Firebase installation ID
        FirebaseInstallations.getInstance().getId().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    Control.setLocalFID(task.getResult()+"");
                } else {
                    Log.e("Firebase Error", "Error getting Firebase Installation ID", task.getException());
                }
            }
        });

        Button checkDeviceButton = findViewById(R.id.check_device_button);
        Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
        // Set the button invisible initially
        checkDeviceButton.setVisibility(View.INVISIBLE);

        // Create a handler to delay the visibility change
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the button visible after 3 seconds
                checkDeviceButton.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MainActivity.this, Landing_page.class);
                startActivity(intent);
            }
        }, 3000); // 3000 milliseconds = 3 seconds

        checkDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Landing_page.class);
                startActivity(intent);
            }
        });

    }
}
