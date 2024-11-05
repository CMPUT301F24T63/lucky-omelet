package com.example.eventlotterysystem;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChosenListManageActivity extends AppCompatActivity {
    private Event event;
    private Control control;
    private ArrayAdapter<User> adapter;

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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, event.getChosenList());
        memberList.setAdapter(adapter);

        memberList.setOnItemLongClickListener((parent, view, position, id) -> {
            User user = event.getChosenList().get(position);
            showDeleteConfirmationDialog(user);
            return true;
        });

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());

        findViewById(R.id.roll_button).setOnClickListener(v -> {
            User organizer = control.getCurrentUser();
            if (organizer != null) {
                organizer.reRoll(event);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Replacement applicant drawn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Current user is not set", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entrant")
                .setMessage("Are you sure you want to delete " + user.getName() + " from the chosen list?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    event.getChosenList().remove(user);
                    event.getCancelledList().add(user);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Entrant moved to cancelled list", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}