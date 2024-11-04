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
        setUpMockData(control); // later this will fetch data from database

        // Control Data Test
        Log.i("UserListSize", String.valueOf(control.getUserList().size()));
        Log.i("FacilityListSize", String.valueOf(control.getFacilityList().size()));
        Log.i("EventListSize", String.valueOf(control.getEventList().size()));
        Log.i("Event 0 Waiting List Size", String.valueOf(control.getEventList().get(0).getWaitingList().size()));
        Log.i("Event 0 Chosen List Size", String.valueOf(control.getEventList().get(0).getChosenList().size()));
        Log.i("Event 0 Cancelled List Size", String.valueOf(control.getEventList().get(0).getCancelledList().size()));
        Log.i("Event 0 FinalList size", String.valueOf(control.getEventList().get(0).getFinalList().size()));
        Log.i("User 0 notification list size", String.valueOf(control.getUserList().get(0).getNotificationList().size()));


        /**********************  You should see something like this in your logcat  ******************************
         * 2024-11-02 16:28:28.695 12295-12295 UserListSize            com.example.eventlotterysystem       I  12
         * 2024-11-02 16:28:28.695 12295-12295 FacilityListSize        com.example.eventlotterysystem       I  1
         * 2024-11-02 16:28:28.695 12295-12295 EventListSize           com.example.eventlotterysystem       I  3
         * 2024-11-02 16:28:28.695 12295-12295 Event 0 Wa... List Size com.example.eventlotterysystem       I  5
         * 2024-11-02 16:28:28.695 12295-12295 Event 0 Ch... List Size com.example.eventlotterysystem       I  3
         * 2024-11-02 16:28:28.695 12295-12295 Event 0 Ca... List Size com.example.eventlotterysystem       I  2
         * 2024-11-02 16:28:28.695 12295-12295 Event 0 FinalList size  com.example.eventlotterysystem       I  2
         * 2024-11-02 16:28:28.695 12295-12295 User 0 not... list size com.example.eventlotterysystem       I  2
         **********************************************************************************************************/

        // Chose which user to test for MainActivity in checkDevice function
        checkDevice(control);
        // Get current user by calling Control.getCurrentUser();
        // e.g. Control.getCurrentUser().joinEvent(event);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_device);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        control = Control.getInstance();

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

        FirestoreManager fm = new FirestoreManager();
        fm.saveControl(control);

        Button checkDeviceButton = findViewById(R.id.check_device_button);
        checkDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Landing_page.class);
                startActivity(intent);
            }
        });


    }

    protected void setUpMockData(Control control) {
        // set up mock data

        // 10 entrants (index 0-9)
        for (int i = 0; i < 10; i++) {
            User entrant = new User(control.getCurrentUserID(), "Entrant" + i, "entrant" + i + "@example.com", "contact" + i, false);
            control.getUserList().add(entrant);
        }

        // 1 organizer (index 10)
        User organizer = new User(control.getCurrentUserID(), "Organizer", "organizer" + "@example.com", "contact", false);
        control.getUserList().add(organizer);
        organizer.createFacility(control, "Test Facility Name", "Test Facility Location", "Test Facility Description", "Test Facility Open Time");


        // 1 admin (index 11)
        User admin = new User(control.getCurrentUserID(), "Admin", "email", "contact", true);
        control.getUserList().add(admin);

        // Organizer create multiple events
        organizer.createEvent(control, "Event 1", "Test Event Description", 5, 10); // fixed winner
        organizer.createEvent(control, "Event 2", "Test Event Description", 20, 20); // random winner
        organizer.createEvent(control, "Event 3", "Test Event Description", 20, 20); // random winner

        for (int i = 0; i < 10; i++) {
            control.getUserList().get(i).joinEvent(control.getEventList().get(0));
            control.getUserList().get(i).joinEvent(control.getEventList().get(1));
            control.getUserList().get(i).joinEvent(control.getEventList().get(2));
        }

        // For event 0, we choose user 0-4 to win; 0, 1 accept; 2,3 decline, 4 no response
        // WaitingList: 5-9
        // ChosenList: 0, 1, 4
        // CancelledList: 2, 3
        // FinalList: 0, 1

        // Remove from waiting list
        for (int i = 0; i < 5; i++) {
            control.getEventList().get(0).getWaitingList().remove(control.getUserList().get(i));
        }
        // ChosenList
        control.getEventList().get(0).getChosenList().add(control.getUserList().get(0));
        control.getEventList().get(0).getChosenList().add(control.getUserList().get(1));
        control.getEventList().get(0).getChosenList().add(control.getUserList().get(4));
        // CancelledList
        control.getEventList().get(0).getCancelledList().add(control.getUserList().get(2));
        control.getEventList().get(0).getCancelledList().add(control.getUserList().get(3));
        // FinalList
        control.getEventList().get(0).getFinalList().add(control.getUserList().get(0));
        control.getEventList().get(0).getFinalList().add(control.getUserList().get(1));

        // For event 1,2, the organizer reroll, so that everyone gets an invitation message
        organizer.reRoll(organizer.getOrganizedList().get(1));
        organizer.reRoll(organizer.getOrganizedList().get(2));


    }

    protected void checkDevice(Control control){
        // find index of current user in user list by IMEI
        int index = 0;
        // 0: entrant   10: organizer   11: admin
        Control.setCurrentUser(control.getUserList().get(index));
    }
}
