/*
To do:
    1. Notifications load function
 */

package com.example.eventlotterysystem;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FirestoreManager {
    private FirebaseFirestore db;
    private static FirestoreManager instance;

    public static FirestoreManager getInstance() {
        if (instance == null) {
            instance = new FirestoreManager();
        }
        return instance;
    }

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    public void saveControl(Control control) {
        Map<String, Object> controlData = new HashMap<>();
        controlData.put("currentUserID", control.getCurrentUserID());
        controlData.put("currentEventID", control.getCurrentEventID());

        // Save main control document
        db.collection("control").document("main")
                .set(controlData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "Main data saved"))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving main data: " + e));

        // Save all users
        for (User user : control.getUserList()) {
            saveUser(user);
        }

        // Save all facilities
        for (Facility facility : control.getFacilityList()) {
            saveFacility(facility);
        }

        // Save all events
        for (Event event : control.getEventList()) {
            saveEvent(event);
        }

        // Save all pictures
        for (Picture picture : control.getPictureList()) {
            savePicture(picture);
        }

    }

    public void saveUser(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userID", user.getUserID());
        userData.put("name", user.getName());
        userData.put("email", user.getEmail());
        userData.put("contact", user.getContact());
        userData.put("isAdmin", user.isAdmin());
        userData.put("notificationSetting", user.getNotificationSetting());
        userData.put("FID", user.getFID());

        if (user.getPicture() != null) {
            userData.put("pictureRef", db.collection("pictures").document(String.valueOf(user.getUserID())));
        }

        if (user.getFacility() != null) {
            userData.put("facilityRef", db.collection("facilities").document(String.valueOf(user.getFacility().getCreator().getUserID())));
        }

        // Save enrolled events as references
        ArrayList<DocumentReference> enrolledRefs = new ArrayList<>();
        for (Event event : user.getEnrolledList()) {
            enrolledRefs.add(db.collection("events").document(String.valueOf(event.getEventID())));
        }
        userData.put("enrolledEvents", enrolledRefs);

        // Save organized events as references
        ArrayList<DocumentReference> organizedRefs = new ArrayList<>();
        for (Event event : user.getOrganizedList()) {
            organizedRefs.add(db.collection("events").document(String.valueOf(event.getEventID())));
        }
        userData.put("organizedEvents", organizedRefs);

        // Save notifications
        DocumentReference userIDRef = db.collection("notifications").document(String.valueOf(user.getUserID()));
        for (Notification notification : user.getNotificationList()) {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("eventRef", db.collection("events").document(String.valueOf(notification.getEvent().getEventID())));
            notificationData.put("userRef", db.collection("users").document(String.valueOf(notification.getUser().getUserID())));
            notificationData.put("customMessage", notification.getCustomMessage());
            notificationData.put("needAccept", notification.needAccept());
            notificationData.put("isAccepted", notification.getIsAccepted());
            notificationData.put("isDeclined", notification.getIsDeclined());
            userIDRef.collection(String.valueOf(notification.getEvent().getEventID())).document("info").set(notificationData);
        }


        db.collection("users").document(String.valueOf(user.getUserID()))
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "User saved: " + user.getUserID()))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving user: " + e));
    }

    private void saveFacility(Facility facility) {
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("name", facility.getName());
        facilityData.put("description", facility.getDescription());

        // Save creator as reference
        facilityData.put("creatorRef", db.collection("users").document(String.valueOf(facility.getCreator().getUserID())));

        if (facility.getPoster() != null) {
            facilityData.put("posterRef", db.collection("pictures").document(String.valueOf(facility.getPoster().getUploader().getUserID())));
        }

        db.collection("facilities").document(facility.getCreator().getUserID() + "")
                .set(facilityData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "Facility saved: " + facility.getCreator().getUserID()))
                .addOnFailureListener(e -> Log.i("Database Error", "Error saving facility: " + e));
    }

    private void saveEvent(Event event) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventID", event.getEventID());
        eventData.put("name", event.getName());
        eventData.put("description", event.getDescription());
        eventData.put("limitChosenList", event.getLimitChosenList());
        eventData.put("limitWaitingList", event.getLimitWaitinglList());
        eventData.put("hashCodeQR", event.getHashCodeQR());

        // Save references
        eventData.put("creatorRef", db.collection("users").document(String.valueOf(event.getCreator().getUserID())));

        if (event.getPoster() != null) {
            eventData.put("posterRef", db.collection("pictures").document(String.valueOf(event.getPoster().getUploader().getUserID())));
        }

        // Save user lists as references
        ArrayList<DocumentReference> waitingRefs = new ArrayList<>();
        for (User user : event.getWaitingList()) {
            waitingRefs.add(db.collection("users").document(String.valueOf(user.getUserID())));
        }
        eventData.put("waitingList", waitingRefs);

        ArrayList<DocumentReference> cancelledRefs = new ArrayList<>();
        for (User user : event.getCancelledList()) {
            cancelledRefs.add(db.collection("users").document(String.valueOf(user.getUserID())));
        }
        eventData.put("cancelledList", cancelledRefs);

        ArrayList<DocumentReference> chosenRefs = new ArrayList<>();
        for (User user : event.getChosenList()) {
            chosenRefs.add(db.collection("users").document(String.valueOf(user.getUserID())));
        }
        eventData.put("chosenList", chosenRefs);

        ArrayList<DocumentReference> finalRefs = new ArrayList<>();
        for (User user : event.getFinalList()) {
            finalRefs.add(db.collection("users").document(String.valueOf(user.getUserID())));
        }
        eventData.put("finalList", finalRefs);

        db.collection("events").document(String.valueOf(event.getEventID()))
                .set(eventData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "Event saved: " + event.getEventID()))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving event: " + e));
    }

    private void savePicture(Picture picture) {
        Map<String, Object> pictureData = new HashMap<>();
        pictureData.put("content", picture.getContent());
        pictureData.put("uploaderRef", db.collection("users").document(String.valueOf(picture.getUploader().getUserID())));

        db.collection("pictures").document(String.valueOf(picture.getUploader().getUserID()))
                .set(pictureData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "Picture saved for user: " + picture.getUploader().getUserID()))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving picture: " + e));
    }

    public void loadControl(Control control) {
        Log.i("Hello", "Hello from loadControl!!!");
        if (db == null) {
            Log.e("Database Error", "Database not initialized");
        } else {
            Log.i("Hello", "Database is fine!");
            db.collection("control").document("main").get().addOnCompleteListener(task ->{
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.i("Hello", "Document exists!"); // something went wrong
                    } else {
                        Log.i("Hello", "Document does not exist!");
                    }
                } else{
                    Log.i("Task not successful", "Something went wrong!");
                }
            } );
        }

        db.collection("control").document("main").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        control.setCurrentUserID(documentSnapshot.getLong("currentUserID").intValue());
                        control.setCurrentEventID(documentSnapshot.getLong("currentEventID").intValue());
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading control data: " + e));
        // load lists
        Log.i("Helllo", "Hello!!!!!");
        loadUsers(control);
        loadFacilities(control);
        loadPictures(control);
        loadEvents(control);
        loadNotifications(control);
    }

    private void loadUsers(Control control) {
        Log.i("Hello", "Hello from loadUsers!!!");
        db.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        User user = new User(
                                document.getLong("userID").intValue(),
                                document.getString("name"),
                                document.getString("email"),
                                document.getString("contact"),
                                document.getBoolean("isAdmin")
                        );
                        user.setNotificationSetting(document.getBoolean("notificationSetting"));
                        user.setFID(document.getString("FID"));
                        control.getUserList().add(user);
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading users: " + e));
    }

    private void loadFacilities(Control control) {
        Log.i("Hello", "Hello from loadFacilities!!!");
        db.collection("facilities").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference creatorRef = (DocumentReference) document.get("creatorRef");
                        User creator = findUserById(control, Integer.parseInt(creatorRef.getId()));

                        if (creator != null) {
                            Facility facility = new Facility(
                                    document.getString("name"),
                                    document.getString("location"),
                                    creator
                            );
                            control.getFacilityList().add(facility);
                            creator.setFacility(facility);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading facilities: " + e));
    }

    private void loadPictures(Control control) {
        Log.i("Hello", "Hello from loadPictures!!!");
        db.collection("pictures").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference uploaderRef = (DocumentReference) document.get("uploaderRef");
                        User uploader = findUserById(control, Integer.parseInt(uploaderRef.getId()));

                        if (uploader != null) {
                            Picture picture = new Picture(
                                    uploader,
                                    document.getString("content")
                            );
                            control.getPictureList().add(picture);
                            uploader.setPicture(picture);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading pictures: " + e));
    }

    private void loadEvents(Control control) {
        Log.i("Hello", "Hello from loadEvents!!!!!");
        db.collection("events").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference creatorRef = (DocumentReference) document.get("creatorRef");
                        User creator = findUserById(control, Integer.parseInt(creatorRef.getId()));

                        if (creator != null) {
                            Event event = new Event(
                                    document.getLong("eventID").intValue(),
                                    document.getString("name"),
                                    document.getString("description"),
                                    document.getLong("limitChosenList").intValue(),
                                    document.getLong("limitWaitingList").intValue(),
                                    creator
                            );

                            event.setHashCodeQR(document.getString("hashCodeQR"));

                            // Load poster if exists
                            DocumentReference posterRef = (DocumentReference) document.get("posterRef");
                            if (posterRef != null) {
                                Picture poster = findPictureByUploaderId(control, Integer.parseInt(posterRef.getId()));
                                event.setPoster(poster);
                            }

                            // Load user lists
                            loadUserList(document, "waitingList", event.getWaitingList(), control);
                            loadUserList(document, "cancelledList", event.getCancelledList(), control);
                            loadUserList(document, "chosenList", event.getChosenList(), control);
                            loadUserList(document, "finalList", event.getFinalList(), control);

                            control.getEventList().add(event);
                            creator.getOrganizedList().add(event);
                        }
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading events: " + e));
    }

    public void loadNotifications(Control control) {
        Log.i("Hello", "Hello from loadNotifications!!!!!");
        Log.i("UserListSize", String.valueOf(control.getUserList().size()));
        for (User user : control.getUserList()) {
            String userID = String.valueOf(user.getUserID());
            db.collection("notifications").document(userID).collection("events").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            DocumentReference eventRef = (DocumentReference) document.get("eventRef");
                            Event event = findEventById(control, Integer.parseInt(eventRef.getId()));

                            if (event != null) {
                                Notification notification = new Notification(
                                        event,
                                        user,
                                        document.getBoolean("needAccept"),
                                        document.getString("customMessage")
                                );
                                notification.setAccepted(document.getBoolean("isAccepted"));
                                notification.setDeclined(document.getBoolean("isDeclined"));
                                user.getNotificationList().add(notification);
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.e("Database Error", "Error loading notifications: " + e));
        }
    }

    // Helper methods
    private void loadUserList(DocumentSnapshot document, String listName,
                              ArrayList<User> userList, Control control) {
        ArrayList<DocumentReference> refs = (ArrayList<DocumentReference>) document.get(listName);
        if (refs != null) {
            for (DocumentReference ref : refs) {
                User user = findUserById(control, Integer.parseInt(ref.getId()));
                if (user != null) {
                    userList.add(user);
                    if (!user.getEnrolledList().contains(findEventById(control,
                            document.getLong("eventID").intValue()))) {
                        user.getEnrolledList().add(findEventById(control,
                                document.getLong("eventID").intValue()));
                    }
                }
            }
        }
    }

    private User findUserById(Control control, int userId) {
        for (User user : control.getUserList()) {
            if (user.getUserID() == userId) return user;
        }
        return null;
    }

    private Event findEventById(Control control, int eventId) {
        for (Event event : control.getEventList()) {
            if (event.getEventID() == eventId) return event;
        }
        return null;
    }

    private Picture findPictureByUploaderId(Control control, int uploaderId) {
        for (Picture picture : control.getPictureList()) {
            if (picture.getUploader().getUserID() == uploaderId) return picture;
        }
        return null;
    }
}



// Backup

//        db = FirebaseFirestore.getInstance();
//        usersRef = db.collection("users");
//
//        usersRef = db.collection("users");
//        usersRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    control.getUserList().clear();
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        int id = Integer.parseInt(doc.getId());
//                        String name = doc.getString("name");
//                        String contact = doc.getString("contact");
//                        String email = doc.getString("email");
//                        Boolean isAdmin = doc.getBoolean("isAdmin");
//                        curUser = new User(id, name, email, contact, isAdmin);
//                        control.getUserList().add(curUser);
//                    }
//                }
//            }
//        });
////        Toast.makeText(getApplicationContext(), "aaa", Toast.LENGTH_SHORT).show();
//
//        facRef = db.collection("facilities");
//        facRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    control.getFacilityList().clear();
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        int id = Integer.parseInt(doc.getId());
//                        String name = doc.getString("name");
//                        String description = doc.getString("description");
//                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
//                        creatorRef.get().addOnCompleteListener(creatorTask -> {
//                            DocumentSnapshot creatorDoc = creatorTask.getResult();
//                            int creatorId = Integer.parseInt(creatorDoc.getId());
//                            for (User user : control.getUserList()) {
//                                if (user.getUserID()==creatorId) {
//                                    curFac = new Facility(name, description, user);
//                                    control.getFacilityList().add(curFac);
//                                    user.setFacility(curFac);
//                                    break;
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
//
//        eventRef = db.collection("events");
//        eventRef.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    control.getEventList().clear();
//                    for (QueryDocumentSnapshot doc : querySnapshot) {
//                        int id = Integer.parseInt(doc.getId());
//                        String name = doc.getString("name");
//                        String description = doc.getString("description");
//                        int limitChosenList = doc.getLong("limitChosenList").intValue();
//                        int limitWaitingList = doc.getLong("limitWaitingList").intValue();
//                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
//                        creatorRef.get().addOnCompleteListener(creatorTask -> {
//                            DocumentSnapshot creatorDoc = creatorTask.getResult();
//                            int creatorId = Integer.parseInt(creatorDoc.getId());
//                            for (User user : control.getUserList()) {
//                                if (user.getUserID()==creatorId) {
//                                    curEvent = new Event(id, name, description, limitChosenList, limitWaitingList, user);
//                                    control.getEventList().add(curEvent);
//
//                                    List<DocumentReference> waitingList = (List<DocumentReference>) doc.get("waitingList");
//                                    if (waitingList != null) {
//                                        for (DocumentReference userRef : waitingList) {
//                                            int userId = Integer.parseInt(userRef.getId());
//                                            for (User waituser : control.getUserList()) {
//                                                if (waituser.getUserID()==userId) {
//                                                    curEvent.getWaitingList().add(waituser);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                    List<DocumentReference> chosenList = (List<DocumentReference>) doc.get("chosenList");
//                                    if (chosenList != null) {
//                                        for (DocumentReference userRef : chosenList) {
//                                            int userId = Integer.parseInt(userRef.getId());
//                                            for (User chosenuser : control.getUserList()) {
//                                                if (chosenuser.getUserID()==userId) {
//                                                    curEvent.getChosenList().add(chosenuser);
//                                                }
//                                            }
//                                        }
//                                    }
//                                    List<DocumentReference> cancelledList = (List<DocumentReference>) doc.get("cancelledList");
//                                    if (cancelledList != null) {
//                                        for (DocumentReference userRef : cancelledList) {
//                                            int userId = Integer.parseInt(userRef.getId());
//                                            for (User cancelleduser : control.getUserList()) {
//                                                if (cancelleduser.getUserID()==userId) {
//                                                    curEvent.getCancelledList().add(cancelleduser);
//                                                }
//                                            }
//                                        }
//                                    }
//                                    List<DocumentReference> FinalList = (List<DocumentReference>) doc.get("FinalList");
//                                    if (FinalList != null) {
//                                        for (DocumentReference userRef : FinalList) {
//                                            int userId = Integer.parseInt(userRef.getId());
//                                            for (User finaluser : control.getUserList()) {
//                                                if (finaluser.getUserID()==userId) {
//                                                    curEvent.getFinalList().add(finaluser);
//                                                }
//                                            }
//                                        }
//                                    }
//                                    break;
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });