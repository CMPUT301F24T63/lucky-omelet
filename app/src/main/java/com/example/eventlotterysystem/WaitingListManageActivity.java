package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class WaitingListManageActivity extends AppCompatActivity {
    private Event event;
    private Control control;

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
        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, event.getWaitingList());
        memberList.setAdapter(adapter);

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());
    }
}