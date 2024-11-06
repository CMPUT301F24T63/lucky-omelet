package com.example.eventlotterysystem;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
// Activity Screen for Viewing an event page, currently handled by
//intents instead of databases
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


//        eventTitle.setText("Title of Event");

        int eventId = getIntent().getIntExtra("eventId", -1);

        Control control = Control.getInstance();
        Event event = control.getEventList().get(eventId);
        String name = event.getName();
        String description = event.getDescription();
        String limitChosen = String.valueOf(event.getLimitChosenList());
        String limitWaiting = String.valueOf(event.getLimitWaitinglList());

        eventTitle.setText(name);

        String details =
                "Description: " + description + "\n"
                + "Event Size: (0/" + limitChosen + ")" + "\n"
                + "Waiting List Size: (" + limitWaiting + ")" + "\n";

        eventDetail.setText(details);




        buttonMap.setOnClickListener(view -> {
            showMapDialog();
        });

        buttonManage.setOnClickListener(view -> {
            showManageMemberDialog();
        });

        buttonQRCode.setOnClickListener(view -> {
            showQRCodeDialog();
        });




    }
    private void showManageMemberDialog() {
        ManageEventMembersDialogFragment dialog = new ManageEventMembersDialogFragment();
        dialog.show(getSupportFragmentManager(), "ManageMemberDialog");
    }

    private void showQRCodeDialog() {
        QRCodeDialogFragment dialog = QRCodeDialogFragment.newInstance(true, 0);
        dialog.show(getSupportFragmentManager(), "QRCodeDialog");
    }

    private void showMapDialog() {
        MapDialogFragment dialog = new MapDialogFragment();
        dialog.show(getSupportFragmentManager(), "MapDialog");
    }
}