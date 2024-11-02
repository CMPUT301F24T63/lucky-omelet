/*
To do:
    1. Notifications (new collection + new document + create refs in users)
    2. modify and test load data
 */

package com.example.eventlotterysystem;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class FirestoreManager {
    private FirebaseFirestore db;
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
                .addOnSuccessListener(aVoid -> Log.i("Database Success","Main data saved"))
                .addOnFailureListener(e -> Log.e("Database Error","Error saving main data: " + e));

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

    private void saveUser(User user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("userID", user.getUserID());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("email", user.getEmail());
        userData.put("contact", user.getContact());
        userData.put("isAdmin", user.isAdmin());

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
        ArrayList<DocumentReference> notificationRefs = new ArrayList<>();

        for (Notification notification : user.getNotificationList()) {
            Map<String, Object> notificationData = new HashMap<>();
            notificationData.put("eventRef", db.collection("events").document(String.valueOf(notification.getEvent().getEventID())));
            notificationData.put("userRef", db.collection("users").document(String.valueOf(notification.getUser().getUserID())));
            notificationData.put("customMessage", notification.getCustomMessage());
            notificationData.put("needAccept", notification.needAccept());
            notificationData.put("isAccepted", notification.getIsAccepted());
            notificationData.put("isDeclined", notification.getIsDeclined());

            notificationRefs.add(db.collection("notifications").document(String.valueOf(notificationData)));
        }
        userData.put("notifications", notificationRefs);

        db.collection("users").document(String.valueOf(user.getUserID()))
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success", "User saved: " + user.getUserID()))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving user: " + e));
    }

    private void saveFacility(Facility facility) {
        Map<String, Object> facilityData = new HashMap<>();
        facilityData.put("name", facility.getName());
        facilityData.put("location", facility.getLocation());
        facilityData.put("description", facility.getDescription());
        facilityData.put("openTime", facility.getOpenTime());

        // Save creator as reference
        facilityData.put("creatorRef", db.collection("users").document(String.valueOf(facility.getCreator().getUserID())));

        if (facility.getPoster() != null) {
            facilityData.put("posterRef", db.collection("pictures").document(String.valueOf(facility.getPoster().getUploader().getUserID())));
        }

        db.collection("facilities").document(facility.getName())
                .set(facilityData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success","Facility saved: " + facility.getCreator().getUserID()))
                .addOnFailureListener(e -> Log.i("Database Error","Error saving facility: " + e));
    }

    private void saveEvent(Event event) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventID", event.getEventID());
        eventData.put("name", event.getName());
        eventData.put("description", event.getDescription());
        eventData.put("limit", event.getLimit());
        eventData.put("hashCodeQR", event.getHashCodeQR());

        // Save references
        eventData.put("creatorRef", db.collection("users").document(String.valueOf(event.getCreator().getUserID())));
        eventData.put("facilityRef", db.collection("facilities").document(event.getFacility().getName()));

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
                .addOnFailureListener(e -> Log.e("Database Error","Error saving event: " + e));
    }

    private void savePicture(Picture picture) {
        Map<String, Object> pictureData = new HashMap<>();
        pictureData.put("content", picture.getContent());
        pictureData.put("uploaderRef", db.collection("users").document(String.valueOf(picture.getUploader().getUserID())));

        db.collection("pictures").document(String.valueOf(picture.getUploader().getUserID()))
                .set(pictureData)
                .addOnSuccessListener(aVoid -> Log.i("Database Success","Picture saved for user: " + picture.getUploader().getUserID()))
                .addOnFailureListener(e -> Log.e("Database Error", "Error saving picture: " + e));
    }


    // Load Control object from Firestore
    public void loadControl(OnControlLoadedListener listener) {
        db.collection("control").document("main")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Control control = new Control();
                        // Load all data asynchronously
                        loadAllData(control, documentSnapshot, listener);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Database Error", "Error loading control: " + e);
                    listener.onError(e);
                });
    }

    private void loadAllData(Control control, DocumentSnapshot controlDoc, OnControlLoadedListener listener) {
        // Load all users first
        db.collection("users").get()
                .addOnSuccessListener(userDocs -> {
                    Map<String, User> userMap = new HashMap<>();

                    for (DocumentSnapshot doc : userDocs) {
                        User user = new User(
                                doc.getLong("userID").intValue(),
                                doc.getString("firstName"),
                                doc.getString("lastName"),
                                doc.getString("email"),
                                doc.getString("contact"),
                                doc.getBoolean("isAdmin")
                        );
                        userMap.put(doc.getId(), user);
                        control.getUserList().add(user);
                    }

                    // After users are loaded, load facilities
                    loadFacilities(control, userMap, () -> {
                        // After facilities are loaded, load events
                        loadEvents(control, userMap, () -> {
                            // After events are loaded, load pictures
                            loadPictures(control, userMap, () -> {
                                // Everything is loaded, notify listener
                                listener.onControlLoaded(control);
                            });
                        });
                    });
                })
                .addOnFailureListener(e -> listener.onError(e));
    }

    private void loadFacilities(Control control, Map<String, User> userMap, Runnable onComplete) {
        db.collection("facilities").get()
                .addOnSuccessListener(facilityDocs -> {
                    for (DocumentSnapshot doc : facilityDocs) {
                        // Get creator reference
                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
                        User creator = userMap.get(creatorRef.getId());

                        if (creator != null) {
                            Facility facility = new Facility(
                                    doc.getString("name"),
                                    doc.getString("location"),
                                    doc.getString("description"),
                                    doc.getString("openTime"),
                                    creator
                            );
                            control.getFacilityList().add(facility);
                            creator.createFacility(control, facility.getName(), facility.getLocation(),
                                    facility.getDescription(), facility.getOpenTime());
                        }
                    }
                    onComplete.run();
                })
                .addOnFailureListener(e -> Log.e("Database Error","Error loading facilities: " + e));
    }

    private void loadEvents(Control control, Map<String, User> userMap, Runnable onComplete) {
        db.collection("events").get()
                .addOnSuccessListener(eventDocs -> {
                    for (DocumentSnapshot doc : eventDocs) {
                        // Get creator and facility references
                        DocumentReference creatorRef = doc.getDocumentReference("creatorRef");
                        DocumentReference facilityRef = doc.getDocumentReference("facilityRef");

                        User creator = userMap.get(creatorRef.getId());
                        Facility facility = null;
                        for (Facility f : control.getFacilityList()) {
                            if (f.getName().equals(facilityRef.getId())) {
                                facility = f;
                                break;
                            }
                        }

                        if (creator != null && facility != null) {
                            Event event = new Event(
                                    doc.getLong("eventID").intValue(),
                                    doc.getString("name"),
                                    doc.getString("description"),
                                    doc.getLong("limit").intValue(),
                                    creator,
                                    facility
                            );

                            // Load user lists
                            ArrayList<DocumentReference> waitingRefs = (ArrayList<DocumentReference>) doc.get("waitingList");
                            if (waitingRefs != null) {
                                for (DocumentReference userRef : waitingRefs) {
                                    User user = userMap.get(userRef.getId());
                                    if (user != null) {
                                        event.getWaitingList().add(user);
                                    }
                                }
                            }

                            ArrayList<DocumentReference> cancelledRefs = (ArrayList<DocumentReference>) doc.get("cancelledList");
                            if (cancelledRefs != null) {
                                for (DocumentReference userRef : cancelledRefs) {
                                    User user = userMap.get(userRef.getId());
                                    if (user != null) {
                                        event.getCancelledList().add(user);
                                    }
                                }
                            }

                            ArrayList<DocumentReference> chosenRefs = (ArrayList<DocumentReference>) doc.get("chosenList");
                            if (chosenRefs != null) {
                                for (DocumentReference userRef : chosenRefs) {
                                    User user = userMap.get(userRef.getId());
                                    if (user != null) {
                                        event.getChosenList().add(user);
                                    }
                                }
                            }

                            ArrayList<DocumentReference> finalRefs = (ArrayList<DocumentReference>) doc.get("finalList");
                            if (finalRefs != null) {
                                for (DocumentReference userRef : finalRefs) {
                                    User user = userMap.get(userRef.getId());
                                    if (user != null) {
                                        event.getFinalList().add(user);
                                    }
                                }
                            }

                            control.getEventList().add(event);
                            creator.getOrganizedList().add(event);
                        }
                    }
                    onComplete.run();
                })
                .addOnFailureListener(e -> Log.e("Database Error","Error loading events: " + e));
    }

    private void loadPictures(Control control, Map<String, User> userMap, Runnable onComplete) {
        db.collection("pictures").get()
                .addOnSuccessListener(pictureDocs -> {
                    for (DocumentSnapshot doc : pictureDocs) {
                        // Get uploader reference
                        DocumentReference uploaderRef = doc.getDocumentReference("uploaderRef");
                        User uploader = userMap.get(uploaderRef.getId());

                        if (uploader != null) {
                            Picture picture = new Picture(
                                    uploader,
                                    doc.getString("content")
                            );
                            control.getPictureList().add(picture);

                            // Check if this picture is associated with a user's profile
                            for (User user : control.getUserList()) {
                                DocumentReference userPictureRef = doc.getDocumentReference("pictureRef");
                                if (userPictureRef != null &&
                                        userPictureRef.getId().equals(String.valueOf(user.getUserID()))) {
                                    user.uploadPicture(control, uploader);
                                }
                            }

                            // Check if this picture is associated with a facility
                            for (Facility facility : control.getFacilityList()) {
                                DocumentReference facilityPosterRef = doc.getDocumentReference("posterRef");
                                if (facilityPosterRef != null &&
                                        facilityPosterRef.getId().equals(facility.getName())) {
                                    facility.setPoster(picture);
                                }
                            }
                        }
                    }
                    onComplete.run();
                })
                .addOnFailureListener(e -> Log.e("Database Error", "Error loading pictures: " + e));
    }

    // Interface for handling async loading
    public interface OnControlLoadedListener {
        void onControlLoaded(Control control);
        void onError(Exception e);
    }

}
