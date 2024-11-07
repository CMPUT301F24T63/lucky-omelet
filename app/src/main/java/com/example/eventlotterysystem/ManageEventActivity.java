package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ManageEventActivity extends AppCompatActivity {

    private TextView eventTitle;
    private TextView eventDetail;
    private ImageView eventPoster;
    private Button buttonManage;
    private Button buttonEdit;
    private Button buttonQRCode;
    private Button buttonMap;
    private ImageView deleteButton;
    private ImageView returnButton;
    private Event curEvent;
    private Control control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_org_manage);  // Using the provided layout

        control = Control.getInstance();
//        Toast.makeText(this, "Event clicked", Toast.LENGTH_SHORT).show();
        // Initialize views
        eventTitle = findViewById(R.id.name);
        eventDetail = findViewById(R.id.event_detail);
        eventPoster = findViewById(R.id.poster);
        buttonManage = findViewById(R.id.manage_member_button);
        buttonEdit = findViewById(R.id.event_edit_button);
        buttonQRCode = findViewById(R.id.qr_code_button);
        buttonMap = findViewById(R.id.show_map_button);
        deleteButton = findViewById(R.id.del_button);
        returnButton = findViewById(R.id.return_button);

        // Retrieve the Event object passed via intent
        int id = (int) getIntent().getSerializableExtra("eventID");
        for (Event event : control.getEventList()) {
            if (event.getEventID() == id){
                curEvent = event;
                break;
            }
        }
        if (curEvent != null) {
            // Populate the UI with event data
            eventTitle.setText(curEvent.getName());
            eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                    + "Event Size: (0/" + curEvent.getLimitChosenList() + ")\n"
                    + "Waiting List Size: (" + curEvent.getLimitWaitinglList() + ")");
        }

        // Return button to go back
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageEventActivity.this, Landing_page.class);
            startActivity(intent);
        });

        // Manage members
        buttonManage.setOnClickListener(v -> {
            Intent intent = new Intent(ManageEventActivity.this, WaitingListManageActivity.class);
            intent.putExtra("eventId", curEvent.getEventID());  // Pass the eventId
            startActivity(intent);
        });
    }
}
