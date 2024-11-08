/**
 * The {@code Utils} class provides utility methods for common operations in the event lottery system.
 */
package com.example.eventlotterysystem;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

    /**
     * Draws {@code n} unique random numbers from the range [0, {@code m}], where {@code m > n},
     * and returns them in descending order.
     * <p>
     * This method ensures that all drawn numbers are unique by using a {@code Set} to store
     * them, then converts the {@code Set} to a {@code List} and sorts it in descending order.
     *
     * @param n the number of unique random numbers to draw
     * @param m the upper bound (inclusive) for the random numbers
     * @return a {@code List<Integer>} containing {@code n} unique random numbers in descending order
     * @throws IllegalArgumentException if {@code m <= n}, as it would be impossible to draw {@code n} unique numbers
     */
    public static List<Integer> drawRandomNumbers(int n, int m) {
        if (m <= n) {
            throw new IllegalArgumentException("Upper bound m must be greater than n for unique draws.");
        }
        Set<Integer> resultSet = new HashSet<>();
        Random random = new Random();

        // Keep drawing until we have n unique numbers
        while (resultSet.size() < n) {
            int randomNumber = random.nextInt(m + 1); // Generate a number between 0 and m
            resultSet.add(randomNumber); // Add to the set (duplicates automatically handled)
        }

        // Convert the set to a list and sort it in descending order
        List<Integer> resultList = new ArrayList<>(resultSet);
        resultList.sort(Collections.reverseOrder());

        return resultList;
    }

    public static void setUpMockData(Control control){
        // set up mock data

        // 10 entrants (index 0-9)
        for (int i = 0; i < 10; i++) {
            User entrant = new User(control.getUserIDForUserCreation(), "Entrant" + i, "entrant" + i + "@example.com", "contact" + i, false);
            control.getUserList().add(entrant);
        }

        // 1 organizer (index 10)
        User organizer = new User(control.getUserIDForUserCreation(), "Organizer", "organizer" + "@example.com", "contact", false);
        control.getUserList().add(organizer);
        organizer.createFacility(control, "Test Facility Name", "Test Facility Location", "Test Facility Description", "Test Facility Open Time");


        // 1 admin (index 11)
        User admin = new User(control.getUserIDForUserCreation(), "Admin", "email", "contact", true);
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
        control.getEventList().get(0).getChosenList().add(control.getUserList().get(4));
        // CancelledList
        control.getUserList().get(2).cancelEvent(control.getEventList().get(0));
        control.getUserList().get(3).cancelEvent(control.getEventList().get(0));
        control.getEventList().get(0).getCancelledList().add(control.getUserList().get(2));
        control.getEventList().get(0).getCancelledList().add(control.getUserList().get(3));
        // FinalList
        control.getEventList().get(0).getFinalList().add(control.getUserList().get(0));
        control.getEventList().get(0).getFinalList().add(control.getUserList().get(1));

        // For event 1,2, the organizer reroll, so that everyone gets an invitation message
        organizer.reRoll(organizer.getOrganizedList().get(1));
        organizer.reRoll(organizer.getOrganizedList().get(2));


    }

    public static void checkControlData(Control control){
         // Control Data Test
        Log.i("UserListSize", String.valueOf(control.getUserList().size()));
        Log.i("FacilityListSize", String.valueOf(control.getFacilityList().size()));
        Log.i("EventListSize", String.valueOf(control.getEventList().size()));
        Log.i("E0 Waiting List Size", String.valueOf(control.getEventList().get(0).getWaitingList().size()));
        Log.i("E0 Chosen List Size", String.valueOf(control.getEventList().get(0).getChosenList().size()));
        Log.i("E0 Cancelled List Size", String.valueOf(control.getEventList().get(0).getCancelledList().size()));
        Log.i("E0 FinalList size", String.valueOf(control.getEventList().get(0).getFinalList().size()));
        Log.i("U0 FID", control.getUserList().get(0).getFID());
        Log.i("U0 notification list size", String.valueOf(control.getUserList().get(0).getNotificationList().size()));
        Log.i("U0 EnrolledList size", String.valueOf(control.getUserList().get(0).getEnrolledList().size()));
        Log.i("U0 OrganizedList size", String.valueOf(control.getUserList().get(0).getOrganizedList().size()));
        Log.i("U10 OrganizedEventsize", String.valueOf(control.getUserList().get(2).getOrganizedList().size()));
        Log.i("Local FID", Control.getLocalFID());


    }
}
