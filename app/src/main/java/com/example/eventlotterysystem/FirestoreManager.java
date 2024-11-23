/**
 * This java class is used to save and read data from the firestore database.
 * Problem: only allow one notification for one user:event pair.
 */

package com.example.eventlotterysystem;

import android.graphics.Picture;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
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

    // Save Control data
    public void saveControl(Control control) {
        Map<String, Object> controlData = new HashMap<>();
        controlData.put("currentUserID", control.getCurrentUserID());
        controlData.put("currentEventID", control.getCurrentEventID());

        // Save control Data
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
            userIDRef.collection("Events").document(String.valueOf(notification.getEvent().getEventID())).set(notificationData);
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

//        if (facility.getPoster() != null) {
//            facilityData.put("posterRef", db.collection("pictures").document(String.valueOf(facility.getPoster().getUploader().getUserID())));
//        }

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
        eventData.put("geoSetting", event.getGeoSetting());

        // save coordinates
        eventData.put("latitudeList", event.getLatitudeList());
        eventData.put("longitudeList", event.getLongitudeList());

        // Save references
        eventData.put("creatorRef", db.collection("users").document(String.valueOf(event.getCreator().getUserID())));

//        if (event.getPoster() != null) {
//            eventData.put("posterRef", db.collection("pictures").document(String.valueOf(event.getPoster().getUploader().getUserID())));
//        }

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

    public void loadControl(Control control) {
        if (db == null) {
            Log.e("Database Error", "Database not initialized");
        } else {
            Log.i("Database Success", "Database Connection Success");
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
        loadUsers(control);
        loadFacilities(control);
        loadEvents(control);
//        loadNotifications(control);
    }

    private void loadUsers(Control control) {
        db.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        User user = new User(
                                document.getLong("userID").intValue(),
                                document.getString("name"),
                                document.getString("email"),
                                document.getString("contact"),
                                document.getBoolean("isAdmin"),
                                document.contains("Picture") ? document.getString("Picture") : null
                        );
                        user.setNotificationSetting(document.getBoolean("notificationSetting"));
                        user.setFID(document.getString("FID"));
                        control.getUserList().add(user);
                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading users: " + e));
    }

    private void loadFacilities(Control control) {
        db.collection("facilities").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        DocumentReference creatorRef = (DocumentReference) document.get("creatorRef");
                        Facility facility = new Facility(
                                document.getString("name"),
                                document.getString("description"),
                                Integer.parseInt(creatorRef.getId())
                        );
                        control.getFacilityList().add(facility);

                    }
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading facilities: " + e));
    }

    private void loadEvents(Control control) {
        CollectionReference eventRef;

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
                        Boolean geoSetting = doc.getBoolean("geoSetting");
                        String qr = doc.getString("hashCodeQR");
                        String poster = doc.contains("Picture") ? doc.getString("Picture") : null;

                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
                        creatorRef.get().addOnCompleteListener(creatorTask -> {
                            DocumentSnapshot creatorDoc = creatorTask.getResult();
                            int creatorId = Integer.parseInt(creatorDoc.getId());
                            Event curEvent = new Event(id, name, description, limitChosenList, limitWaitingList, creatorId, geoSetting , qr, poster);
                            control.getEventList().add(curEvent);

                            List<DocumentReference> waitingList = (List<DocumentReference>) doc.get("waitingList");
                            if (waitingList != null) {
                                for (DocumentReference userRef : waitingList) {
                                    int userId = Integer.parseInt(userRef.getId());
                                    curEvent.getWaitingListRef().add(userId);
                                }
                            }
                            List<DocumentReference> cancelList = (List<DocumentReference>) doc.get("waitingList");
                            if (cancelList != null) {
                                for (DocumentReference userRef : cancelList) {
                                    int userId = Integer.parseInt(userRef.getId());
                                    curEvent.getCancelledListRef().add(userId);
                                }
                            }
                            List<DocumentReference> chosenList = (List<DocumentReference>) doc.get("waitingList");
                            if (chosenList != null) {
                                for (DocumentReference userRef : chosenList) {
                                    int userId = Integer.parseInt(userRef.getId());
                                    curEvent.getChosenListRef().add(userId);
                                }
                            }
                            List<DocumentReference> finalList = (List<DocumentReference>) doc.get("waitingList");
                            if (finalList != null) {
                                for (DocumentReference userRef : finalList) {
                                    int userId = Integer.parseInt(userRef.getId());
                                    curEvent.getFinalListRef().add(userId);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

//    public void loadNotifications(Control control) {
//        Log.i("Hello", "Hello from loadNotifications!!!!!");
//        Log.i("UserListSize", String.valueOf(control.getUserList().size()));
//            db.collection("notifications").document(userID).collection("Events").get()
//                    .addOnSuccessListener(queryDocumentSnapshots -> {
//                        for (DocumentSnapshot document : queryDocumentSnapshots) {
//                            Notification notification = new Notification(
//                                    Integer.parseInt(document.getId()),
//                                    Integer.parseInt((DocumentReference) document.get("userRef").getId()),
//                                    document.getBoolean("needAccept"),
//                                    document.getString("customMessage"),
//                                    document.getBoolean("isAccepted"),
//                                    document.getBoolean("isDeclined")
//                            );
//
//                        }
//                    })
//                    .addOnFailureListener(e -> Log.e("Database Error", "Error loading notifications: " + e));
//    }

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

    // delete functions: these functions will only delete data from database, not delete in ram or frontend
    public void deleteEventFromDatabase(Event event) {
        // Before deleting event, delete all related notifications
        for (User user: Control.getInstance().getUserList()) {
            // a list of notifications to be deleted
            ArrayList<Notification> deleteList = new ArrayList<>();
            for (Notification notification: user.getNotificationList()) {
                if (notification.getEvent().getEventID() == event.getEventID()) {
                    deleteList.add(notification);
                    deleteNotificationFromDatabase(notification);
                }
            }
            user.getNotificationList().removeAll(deleteList);
        }

        db.collection("events").document(String.valueOf(event.getEventID())).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i("Database Success", "Event deleted: " + event.getEventID());
                })
                .addOnFailureListener(e -> {
                    Log.e("Database Error", "Error deleting event: " + e);
                });
    }

    // Here is an example:
    // FirestoreManager.getInstance().deleteFacilityFromDatabase(Control.getCurrentUser().getFacility());
    public void deleteFacilityFromDatabase(Facility facility) {
        // Delete facility will also delete all events created by this user
        for (Event event: facility.getCreator().getOrganizedList()) {
            deleteEventFromDatabase(event);
            Control.getInstance().getEventList().remove(event);
        }
        facility.getCreator().getOrganizedList().clear();

        db.collection("facilities").document(String.valueOf(facility.getCreator().getUserID())).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i("Database Success", "Facility deleted: " + facility.getCreator().getUserID());
                })
                .addOnFailureListener(e -> {
                    Log.e("Database Error", "Error deleting facility: " + e);
                });
    }

    public void deletePictureFromDatabase(Picture picture) {
        // will be implemented later
    }

    public void deleteNotificationFromDatabase(Notification notification) {
        db.collection("notifications").document(String.valueOf(notification.getUser().getUserID()))
                .collection("Events").document(String.valueOf(notification.getEvent().getEventID())).delete()
                .addOnSuccessListener(aVoid -> {
                    Log.i("Database Success", "Notification Delete successful");
                })
                .addOnFailureListener(e -> {
                    Log.e("Database Error", "Error deleting notification: " + e);
                });
    }


}
