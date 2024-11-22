package com.example.eventlotterysystem;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {
    /*
    Here all users share the same control.
    Later, each user will have its own control.
    Control will fetch data from the database.
    They are different control instances on different devices but they have the same data.
    */
    private Control control;

    @Before
    public void setUp() {
        control = new Control();
        // All users are new
        User organizer = new User(control.getUserIDForUserCreation(),"Organizer", "email", "contact", false );
        User entrant = new User(control.getUserIDForUserCreation(),"Entrant", "email", "contact", false );
        control.getUserList().add(organizer);
        control.getUserList().add(entrant);

        // Create facility (This is what makes a user become organizer)
        organizer.createFacility(control, "Test Facility Name", "Test Facility Location", "Test Facility Description", "Test Facility Open Time");
    }


    @Test
    public void testEventCreation() {
        User organizer = control.getUserList().get(0);
        User entrant = control.getUserList().get(1);
        organizer.createFacility(control, "Test Facility Name", "Test Facility Location", "Test Facility Description", "Test Facility Open Time");
        assertEquals(0, control.getEventList().size());
        // Entrant tries to create an event (fail)
        entrant.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        assertEquals(0, control.getEventList().size()); // Entrant should not be able to create an event
        // Organizer tries to create an event
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        assertEquals(1, control.getEventList().size());
        // Entrant should be able to see new event
        assertEquals(control.getEventList().get(0), organizer.getOrganizedList().get(0));
    }

    @Test
    public void testEventID() {
        User organizer = control.getUserList().get(0);
        // Organizer create 5 events
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        // Check event ids
        assertEquals(0, organizer.getOrganizedList().get(0).getEventID());
        assertEquals(1, organizer.getOrganizedList().get(1).getEventID());
        assertEquals(2, organizer.getOrganizedList().get(2).getEventID());
        assertEquals(3, organizer.getOrganizedList().get(3).getEventID());
        assertEquals(4, organizer.getOrganizedList().get(4).getEventID());
    }

    @Test
    public void testJoinEvent() {
        User organizer = control.getUserList().get(0);
        User entrant = control.getUserList().get(1);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        // Entrant tries to join an event
        entrant.joinEvent(control.getEventList().get(0));
        // Organizer should be able to see the user in their waiting list
        assertEquals(1, organizer.getOrganizedList().get(0).getWaitingList().size());
    }

    @Test
    public void testWaitingListLimit() {
        User organizer = control.getUserList().get(0);
        User bot0 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot1 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot2 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot3 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 2, 2, false);
        bot0.joinEvent(control.getEventList().get(0));
        bot1.joinEvent(control.getEventList().get(0));
        bot2.joinEvent(control.getEventList().get(0));
        bot3.joinEvent(control.getEventList().get(0));
        // Check waiting list
        assertEquals(2, organizer.getOrganizedList().get(0).getWaitingList().size());
        assertEquals(1, bot0.getEnrolledList().size());
    }

    @Test
    public void testCancelEventInWaitingList() {
        User organizer = control.getUserList().get(0);
        User entrant = control.getUserList().get(1);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        entrant.joinEvent(control.getEventList().get(0));
        // Entrant tries to cancel an event
        entrant.cancelEvent(control.getEventList().get(0));
        // Organizer should be able to see the user is no longer in their waiting list
        assertEquals(0, organizer.getOrganizedList().get(0).getWaitingList().size());
    }

    @Test
    public void testRerollRandom() {
        User organizer = control.getUserList().get(0);
        // create 11 users
        User entrant = control.getUserList().get(1);
        User bot1 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot2 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot3 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot4 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot5 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot6 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot7 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot8 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot9 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot10 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        // Event limit set to 5 people, 11 people join the event
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 5, 20, false);
        entrant.joinEvent(control.getEventList().get(0));
        bot1.joinEvent(control.getEventList().get(0));
        bot2.joinEvent(control.getEventList().get(0));
        bot3.joinEvent(control.getEventList().get(0));
        bot4.joinEvent(control.getEventList().get(0));
        bot5.joinEvent(control.getEventList().get(0));
        bot6.joinEvent(control.getEventList().get(0));
        bot7.joinEvent(control.getEventList().get(0));
        bot8.joinEvent(control.getEventList().get(0));
        bot9.joinEvent(control.getEventList().get(0));
        bot10.joinEvent(control.getEventList().get(0));
        // Check waiting list
        assertEquals(11, organizer.getOrganizedList().get(0).getWaitingList().size());
        // Check chosen list
        assertEquals(0, organizer.getOrganizedList().get(0).getChosenList().size());
        // Roll
        organizer.reRoll(control.getEventList().get(0));

        Event event = organizer.getOrganizedList().get(0);
        // Check chosen list
        // Set a breakpoint below to check randomness: passed
        assertEquals(5, event.getChosenList().size());
        // 5 people should be removed from the waiting list
        assertEquals(6, event.getWaitingList().size());

        // Test notifications
        for (int i = 0; i < event.getChosenList().size(); i++) {
            assertEquals(1, event.getChosenList().get(i).getNotificationList().size());
        }
        for (int i = 0; i < event.getWaitingList().size(); i++) {
            assertEquals(0, event.getWaitingList().get(i).getNotificationList().size());
        }
        // Test accept and decline (final list & cancelled list)
        User user1 = event.getChosenList().get(0);
        User user2 = event.getChosenList().get(1);
        User user3 = event.getChosenList().get(2);
        user1.acceptInvitation(user1.getNotificationList().get(0));
        user2.declineInvitation(user2.getNotificationList().get(0));
        user3.declineInvitation(user3.getNotificationList().get(0));
        assertEquals(1, event.getFinalList().size());
        assertEquals(2, event.getCancelledList().size());
        assertEquals(5-2-1, event.getChosenList().size());

    }

    @Test
    public void testRerollAll() {
        User organizer = control.getUserList().get(0);
        // create 3 users
        User entrant = control.getUserList().get(1);
        User bot1 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        User bot2 = new User(control.getUserIDForUserCreation(), "Bot", "email", "contact", false);
        // Event limit set to 5 people, only 3 people join the event
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 5, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        entrant.joinEvent(event);
        bot1.joinEvent(event);
        bot2.joinEvent(event);

        // Check waiting list
        assertEquals(3, event.getWaitingList().size());
        organizer.reRoll(event);
        // Check chosen list
        assertEquals(3, event.getChosenList().size());
        // 3 people should be removed from the waiting list
        assertEquals(0, event.getWaitingList().size());
        // Test notifications
        for (int i = 0; i < event.getChosenList().size(); i++) {
            assertEquals(1, event.getChosenList().get(i).getNotificationList().size());
        }
        entrant.acceptInvitation(entrant.getNotificationList().get(0));
        bot1.acceptInvitation(bot1.getNotificationList().get(0));
        bot2.declineInvitation(bot2.getNotificationList().get(0));
        assertEquals(2, event.getFinalList().size());
        assertEquals(1, event.getCancelledList().size());
        assertEquals(3-1-2, event.getChosenList().size());
    }
    @Test
    public void testViewWaitingList() {
        // US 02.02.01
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        user1.joinEvent(event);
        user2.joinEvent(event);
        assertEquals(2, event.getWaitingList().size());
        assertTrue(event.getWaitingList().contains(user1));
        assertTrue(event.getWaitingList().contains(user2));
    }

    @Test
    public void testViewChosenList() {
        // US 02.06.01
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        event.getChosenList().add(user1);
        event.getChosenList().add(user2);
        assertEquals(2, event.getChosenList().size());
        assertTrue(event.getChosenList().contains(user1));
        assertTrue(event.getChosenList().contains(user2));
    }

    @Test
    public void testViewCancelledList() {
        // US 02.06.02
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        event.getCancelledList().add(user1);
        event.getCancelledList().add(user2);
        assertEquals(2, event.getCancelledList().size());
        assertTrue(event.getCancelledList().contains(user1));
        assertTrue(event.getCancelledList().contains(user2));
    }

    @Test
    public void testViewFinalList() {
        // US 02.06.03
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        event.getFinalList().add(user1);
        event.getFinalList().add(user2);
        assertEquals(2, event.getFinalList().size());
        assertTrue(event.getFinalList().contains(user1));
        assertTrue(event.getFinalList().contains(user2));
    }

    @Test
    public void testDrawReplacementApplicant() {
        // US 02.05.03
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        event.getWaitingList().add(user1);
        event.getWaitingList().add(user2);

        int initialChosenSize = event.getChosenList().size();
        organizer.reRoll(event);

        int newChosenSize = event.getChosenList().size();
        assertTrue(newChosenSize > initialChosenSize);
        assertTrue(event.getChosenList().contains(user1) || event.getChosenList().contains(user2));
        assertTrue(!user1.getNotificationList().isEmpty() || !user2.getNotificationList().isEmpty());
    }

    @Test
    public void testCancelEntrants() {
        // US 02.06.04
        User organizer = control.getUserList().get(0);
        User user1 = new User(control.getUserIDForUserCreation(), "User1", "user1@example.com", "1234567891", false);
        User user2 = new User(control.getUserIDForUserCreation(), "User2", "user2@example.com", "1234567892", false);
        organizer.createEvent(control, "Test Event Name", "Test Event Description", 10, 20, false);
        Event event = organizer.getOrganizedList().get(0);
        event.getChosenList().add(user1);
        event.getChosenList().add(user2);
        event.getChosenList().remove(user1);
        event.getCancelledList().add(user1);
        assertEquals(1, event.getChosenList().size());
        assertFalse(event.getChosenList().contains(user1));
        assertTrue(event.getChosenList().contains(user2));
        assertEquals(1, event.getCancelledList().size());
        assertTrue(event.getCancelledList().contains(user1));
    }
}
