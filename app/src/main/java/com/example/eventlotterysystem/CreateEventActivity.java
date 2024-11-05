package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {
    private EditText editTitle, editDescription, editWaitingListLimit,
    editEventLimit, editOpenDate, editRegisterDate, editPrice;
    private Switch switchGeolocation;
    private Button buttonPublish, buttonCancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_fragment);

        editTitle = findViewById(R.id.firstName);
        editDescription = findViewById(R.id.title_edit5);
        editWaitingListLimit = findViewById(R.id.editTextNumber);
        editEventLimit = findViewById(R.id.editTextNumber2);
        editOpenDate = findViewById(R.id.editTextDate3);
        editRegisterDate = findViewById(R.id.editTextDate4);
        editPrice = findViewById(R.id.editTextNumber3);
        switchGeolocation = findViewById(R.id.location_loc);
        buttonPublish = findViewById(R.id.finish_button);
        buttonCancel = findViewById(R.id.cancel_button);

        buttonCancel.setOnClickListener(view -> finish());
        buttonPublish.setOnClickListener(view -> {

            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String waitingListLimit = editWaitingListLimit.getText().toString().trim();
            String eventLimit = editEventLimit.getText().toString().trim();
            String openDate = editOpenDate.getText().toString().trim();
            String registerDate = editRegisterDate.getText().toString().trim();
            String price = editPrice.getText().toString().trim();

            int intEventLimit = Integer.parseInt(eventLimit);
            int intWaitingListLimit = Integer.parseInt(waitingListLimit);
            int intPrice = Integer.parseInt(price);

            if (title.isEmpty() || description.isEmpty() || eventLimit.isEmpty() ||
            openDate.isEmpty() || registerDate.isEmpty() || price.isEmpty()) {
                Toast.makeText(CreateEventActivity.this,
                        "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

//            Control control = Control.getInstance();
//            User me = new User(69);
//            control.getUserList().add(me);
//            me.createEvent(control, title, description, intEventLimit, intWaitingListLimit);
//
//            // How to retrieve the eventID to pass to later activities?
//
//            FirestoreManager.getInstance().saveControl(Control.getInstance());

            showQRDialog();
        });
    }

    private void showQRDialog() {
        QRCodeDialogFragment dialog = QRCodeDialogFragment.newInstance(false);
        dialog.show(getSupportFragmentManager(), "QRCodeDialog");
    }

}
