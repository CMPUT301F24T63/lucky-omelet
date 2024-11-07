package com.example.eventlotterysystem;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that displays event name, details and navigates to different functionalities
 * associated with an event
 * @Button manage_member_button opens ManageEventMembersDialogFragment, which allows user to navigate to
 * different user-containing lists
 * @Button event_edit_button opens EditEventActivity, which allows user to edit event details
 * @Button  qr_code_button opens QRCodeDialogFragment, which displays event's QR Code (currently placeholder)
 * @Button show_map_button opens MapDialogFragment, which displays a map of entrant's join locations (currently placeholder)
 */
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

    /**
     * Function that opens the ManageEventMembersDialogFragment
     */
    private void showManageMemberDialog() {
        ManageEventMembersDialogFragment dialog = new ManageEventMembersDialogFragment();
        dialog.show(getSupportFragmentManager(), "ManageMemberDialog");
    }

    /**
     * Function that opens the QRCodeDialogFragment
     */
    private void showQRCodeDialog() {
        QRCodeDialogFragment dialog = QRCodeDialogFragment.newInstance(true, 0);
        dialog.show(getSupportFragmentManager(), "QRCodeDialog");
    }

    /**
     * Function that opens the MapDialogFragment
     */
    private void showMapDialog() {
        MapDialogFragment dialog = new MapDialogFragment();
        dialog.show(getSupportFragmentManager(), "MapDialog");
    }
}