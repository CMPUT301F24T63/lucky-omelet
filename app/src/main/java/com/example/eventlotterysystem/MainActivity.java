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


        // Chose which user to test for MainActivity in checkDevice function
        // checkDevice(control);
        // Get current user by calling Control.getCurrentUser();
        // e.g. Control.getCurrentUser().joinEvent(event);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_device);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        usersRef = db.collection("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    control.getUserList().clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        int id = Integer.parseInt(doc.getId());
                        String name = doc.getString("name");
                        String contact = doc.getString("contact");
                        String email = doc.getString("email");
                        Boolean isAdmin = doc.getBoolean("isAdmin");
                        curUser = new User(id, name, email, contact, isAdmin);
                        control.getUserList().add(curUser);
                    }
                }
            }
        });
//        Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();

        facRef = db.collection("facilities");
        facRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    control.getFacilityList().clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        int id = Integer.parseInt(doc.getId());
                        String name = doc.getString("name");
                        String description = doc.getString("description");
                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
                        creatorRef.get().addOnCompleteListener(creatorTask -> {
                            DocumentSnapshot creatorDoc = creatorTask.getResult();
                            int creatorId = Integer.parseInt(creatorDoc.getId());
                            for (User user : control.getUserList()) {
                                if (user.getUserID()==creatorId) {
                                    curFac = new Facility(name, description, user);
                                    control.getFacilityList().add(curFac);
                                    user.setFacility(curFac);
                                    break;
                                }
                            }
                        });
                    }
                }
            }
        });

        eventRef = db.collection("events");
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    control.getEventList().clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        int id = Integer.parseInt(doc.getId());
                        String name = doc.getString("name");
                        String description = doc.getString("description");
                        int limitChosenList = doc.getLong("limitChosenList").intValue();
                        int limitWaitingList = doc.getLong("limitWaitingList").intValue();
                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
                        creatorRef.get().addOnCompleteListener(creatorTask -> {
                            DocumentSnapshot creatorDoc = creatorTask.getResult();
                            int creatorId = Integer.parseInt(creatorDoc.getId());
                            for (User user : control.getUserList()) {
                                if (user.getUserID()==creatorId) {
                                    curEvent = new Event(id, name, description, limitChosenList, limitWaitingList, user);
                                    control.getEventList().add(curEvent);

                                    List<DocumentReference> waitingList = (List<DocumentReference>) doc.get("waitingList");
                                    if (waitingList != null) {
                                        for (DocumentReference userRef : waitingList) {
                                            int userId = Integer.parseInt(userRef.getId());
                                            for (User waituser : control.getUserList()) {
                                                if (waituser.getUserID()==userId) {
                                                    curEvent.getWaitingList().add(waituser);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    List<DocumentReference> chosenList = (List<DocumentReference>) doc.get("chosenList");
                                    if (chosenList != null) {
                                        for (DocumentReference userRef : chosenList) {
                                            int userId = Integer.parseInt(userRef.getId());
                                            for (User chosenuser : control.getUserList()) {
                                                if (chosenuser.getUserID()==userId) {
                                                    curEvent.getChosenList().add(chosenuser);
                                                }
                                            }
                                        }
                                    }
                                    List<DocumentReference> cancelledList = (List<DocumentReference>) doc.get("cancelledList");
                                    if (cancelledList != null) {
                                        for (DocumentReference userRef : cancelledList) {
                                            int userId = Integer.parseInt(userRef.getId());
                                            for (User cancelleduser : control.getUserList()) {
                                                if (cancelleduser.getUserID()==userId) {
                                                    curEvent.getCancelledList().add(cancelleduser);
                                                }
                                            }
                                        }
                                    }
                                    List<DocumentReference> FinalList = (List<DocumentReference>) doc.get("FinalList");
                                    if (FinalList != null) {
                                        for (DocumentReference userRef : FinalList) {
                                            int userId = Integer.parseInt(userRef.getId());
                                            for (User finaluser : control.getUserList()) {
                                                if (finaluser.getUserID()==userId) {
                                                    curEvent.getFinalList().add(finaluser);
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        });
                    }
                }
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


    protected void checkDevice(Control control){
        // find index of current user in user list by IMEI
        int index = 0;
        // 0: entrant   10: organizer   11: admin
        Control.setCurrentUser(control.getUserList().get(index));
    }
}
