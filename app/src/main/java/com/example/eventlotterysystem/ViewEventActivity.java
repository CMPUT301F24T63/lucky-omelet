package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEventActivity extends AppCompatActivity {

    private TextView eventTitle;
    private TextView eventDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_org_manage);

        eventTitle = findViewById(R.id.name);
        eventDetail = findViewById(R.id.event_detail);

        Button buttonManage = findViewById(R.id.manage_member_button);
        Button buttonEdit = findViewById(R.id.event_edit_button);
        Button buttonQRCode = findViewById(R.id.qr_code_button);
        Button buttonMap = findViewById(R.id.show_map_button);

        // Retrieve the Event object from the intent
        Event event = (Event) getIntent().getSerializableExtra("event");

        if (event != null) {
            eventTitle.setText(event.getName());

            String details = "Description: " + event.getDescription() + "\n"
                    + "Event Size: (0/" + event.getLimitChosenList() + ")" + "\n"
                    + "Waiting List Size: (" + event.getLimitWaitinglList() + ")" + "\n";
            eventDetail.setText(details);
        }

//        buttonMap.setOnClickListener(view -> showMapDialog());
//        buttonManage.setOnClickListener(view -> showManageMemberDialog());
//        buttonQRCode.setOnClickListener(view -> showQRCodeDialog());
    }
}
