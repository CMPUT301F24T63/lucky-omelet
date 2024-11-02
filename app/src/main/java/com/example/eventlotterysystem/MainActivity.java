package com.example.eventlotterysystem;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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


        // Chose which user to test for MainActivity

        User me = control.getUserList().get(0); // entrant
        // User me = control.getUserList().get(10); // organizer
        // User me = control.getUserList().get(11); // admin


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.landing_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
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
}