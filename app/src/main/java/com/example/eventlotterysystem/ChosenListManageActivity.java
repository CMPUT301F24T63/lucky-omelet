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

public class ChosenListManageActivity extends AppCompatActivity implements NotifyFragment.NotificationListener{
    private Event event;
    private Control control;
    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chosenlist_manage);

        control = Control.getInstance();
        int eventId = getIntent().getIntExtra("eventId", -1);
        event = control.getEventById(eventId);

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
            User organizer = control.getCurrentUser();
            if (organizer != null) {
                int remainingSpots = event.getLimitChosenList() - event.getChosenList().size() - event.getFinalList().size();
                if (remainingSpots > 0) {
                    organizer.reRoll(event);
                    adapter.notifyDataSetChanged();
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

    private void navigateBackToViewEvent() {
        Intent intent = new Intent(this, ManageEventActivity.class);
        intent.putExtra("eventID", event.getEventID());
        startActivity(intent);
        finish();
    }

    @Override
    public void onNotify(String message) {
        Notification Noti = new Notification(event, control.getCurrentUser(), true, message);
        for (User allUser:event.getWaitingList()){
            if (allUser.getNotificationSetting()){
                allUser.getNotificationList().add(Noti);
            }
        }
    }

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