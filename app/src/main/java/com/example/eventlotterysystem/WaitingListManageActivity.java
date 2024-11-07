package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WaitingListManageActivity extends AppCompatActivity {
    private Event event;
    private Control control;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitinglist_manage);

        control = Control.getInstance();
        int eventId = getIntent().getIntExtra("eventId", -1);
        event = control.getEventById(eventId);

        if (event == null) {
            Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ListView memberList = findViewById(R.id.member_list);
        adapter = new UserAdapter(this, event.getWaitingList());
        memberList.setAdapter(adapter);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bot_nav_bar);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_waiting) {
                    // Already in WaitingListManageActivity
                    return true;
                } else if (itemId == R.id.nav_selected) {
                    intent = new Intent(WaitingListManageActivity.this, ChosenListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_cancelled) {
                    intent = new Intent(WaitingListManageActivity.this, CancelledListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_final) {
                    intent = new Intent(WaitingListManageActivity.this, FinalListManageActivity.class);
                    intent.putExtra("eventId", event.getEventID());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_waiting);

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> navigateBackToViewEvent());

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
}