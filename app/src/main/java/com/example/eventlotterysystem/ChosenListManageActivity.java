package com.example.eventlotterysystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Activity to manage the chosen list of an event.
 * This activity allows the user to view and manage the list of chosen participants for a specific event.
 * It also provides navigation to other list management activities and the ability to send notifications.
 */
public class ChosenListManageActivity extends AppCompatActivity implements NotifyFragment.NotificationListener{
    private Event event;
    private UserAdapter adapter;

    /**
     * Called when the activity is first created.
     * Initializes the activity, sets up the UI components, and handles navigation and button clicks.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosenlist_manage);

        int eventId = getIntent().getIntExtra("eventId", -1);
        event = Control.getInstance().getEventById(eventId);

        if (event == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ListView memberList = findViewById(R.id.member_list);
        adapter = new UserAdapter(this, event.getChosenList());
        memberList.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_waiting) {
                    intent = new Intent(ChosenListManageActivity.this, WaitingListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_selected) {
                    // Already in ChosenListManageActivity
                    return true;
                } else if (itemId == R.id.nav_cancelled) {
                    intent = new Intent(ChosenListManageActivity.this, CancelledListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_final) {
                    intent = new Intent(ChosenListManageActivity.this, FinalListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        memberList.setOnItemLongClickListener((parent, view, position, id) -> {
            User user = event.getChosenList().get(position);
            showDeleteConfirmationDialog(user);
            return true;
        });


        findViewById(R.id.roll_button).setOnClickListener(v -> {
            User organizer = Control.getCurrentUser();
            if (organizer != null) {
                int remainingSpots = event.getLimitChosenList() - event.getChosenList().size() - event.getFinalList().size();
                if (remainingSpots > 0) {
                    organizer.reRoll(event);
                    adapter.notifyDataSetChanged();
                    // Save user action
                    FirestoreManager.getInstance().saveControl(Control.getInstance());
                    Toast.makeText(this, "Replacement applicant(s) drawn", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No entrants added as Selected list is full", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Current user is not set", Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_selected);
        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> navigateBackToViewEvent());

        Button sentNotiButton = findViewById(R.id.notify_button);
        sentNotiButton.setOnClickListener(v -> {
            // Show the notification dialog
            NotifyFragment dialog = new NotifyFragment();
            dialog.show(getSupportFragmentManager(), "NotificationDialogFragment");
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateBackToViewEvent();
            }
        });
    }

    /**
     * Navigates back to the ViewEventActivity.
     * This method is called when the return button is clicked or the back button is pressed.
     */
    private void navigateBackToViewEvent() {
        Intent intent = new Intent(this, ManageEventActivity.class);
        intent.putExtra("eventID", event.getEventID());
        startActivity(intent);
        finish();
    }

    /**
     * Called when a notification is sent from the NotifyFragment.
     * Adds the notification to the notification list of all users in the waiting list who have notifications enabled.
     *
     * @param message The notification message to be sent.
     */
    @Override
    public void onNotify(String message) {
        Notification Noti = new Notification(event, Control.getCurrentUser(), true, message);
        for (User allUser:event.getWaitingList()){
            if (allUser.getNotificationSetting()){
                allUser.getNotificationList().add(Noti);
            }
        }
    }

    /**
     * Shows a confirmation dialog to delete a user from the chosen list.
     * If confirmed, the user is moved to the cancelled list.
     *
     * @param user The user to be deleted from the chosen list.
     */
    private void showDeleteConfirmationDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entrant")
                .setMessage("Are you sure you want to delete " + user.getName() + " from the Selected list?")
                .setPositiveButton(R.string.dialog_yes, (dialog, which) -> {
                    event.getChosenList().remove(user);
                    event.getCancelledList().add(user);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Entrant moved to cancelled list", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}