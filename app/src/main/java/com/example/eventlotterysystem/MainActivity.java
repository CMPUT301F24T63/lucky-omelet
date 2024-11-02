package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private Control control; // Instance of Control
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_device);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        // Use singleton instance of Control
        control = Control.getInstance();
        control.set_currentUserID(3);

        // Fetch users from Firestore
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    control.getUserList().clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        int id = Integer.parseInt(doc.getId());
                        String firstName = doc.getString("firstName");
                        String lastName = doc.getString("lastName");
                        String contact = doc.getString("contact");
                        String email = doc.getString("email");
                        Boolean isAdmin = doc.getBoolean("isAdmin");
                        curUser = new User(id, firstName, lastName, email, contact, isAdmin);
                        control.getUserList().add(curUser);
                    }
                }
                 // Set the current user in Control
            }
        });

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
