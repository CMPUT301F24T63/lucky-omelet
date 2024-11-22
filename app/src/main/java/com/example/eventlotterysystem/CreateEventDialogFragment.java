package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.time.LocalDate;

/**
 * Previous UI approach to creating an event
 */
public class CreateEventDialogFragment extends DialogFragment {

    private CreateEventListener listener;
    private User curUser;

    public interface CreateEventListener {
        void onEventCreated(Event newEvent);
    }

    public void setCreateEventListener(CreateEventListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_event_fragment, container, false);
        curUser = Control.getCurrentUser();


        Switch locationSwitch = view.findViewById(R.id.location_loc);
        EditText titleEdit = view.findViewById(R.id.firstName);
        EditText descriptionEdit = view.findViewById(R.id.title_edit5);
        EditText limitChosenEdit = view.findViewById(R.id.editTextNumber2);
        EditText limitWaitingEdit = view.findViewById(R.id.editTextNumber);

        EditText registrationStartEdit = view.findViewById(R.id.registration_start);
        EditText registrationEndEdit = view.findViewById(R.id.registration_end);
        EditText eventStartEdit = view.findViewById(R.id.event_start);
        EditText eventEndEdit = view.findViewById(R.id.event_end);

        Button uploadImageButton = view.findViewById(R.id.uploadImage_button);
        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        uploadImageButton.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Coming soon in part 4!", Toast.LENGTH_SHORT).show();
                });

        finishButton.setOnClickListener(v -> {
            // Create a new Event using user input
            String eventTitle = titleEdit.getText().toString().trim();
            String eventDescription = descriptionEdit.getText().toString().trim();
            String limitChosenString = limitChosenEdit.getText().toString().trim();
            String limitWaitingString = limitWaitingEdit.getText().toString().trim();

            String registrationStart = registrationStartEdit.getText().toString().trim();
            String registrationEnd = registrationEndEdit.getText().toString().trim();
            String eventStart = eventStartEdit.getText().toString().trim();
            String eventEnd = eventEndEdit.getText().toString().trim();

            if (eventTitle.isEmpty() || eventDescription.isEmpty() || limitChosenString.isEmpty() ||
                registrationStart.isEmpty() || registrationEnd.isEmpty() || eventStart.isEmpty() || eventEnd.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validDates(registrationStart, registrationEnd, eventStart, eventEnd)) {
                return;
            }

            int limitChosen = Integer.parseInt(limitChosenString);
            int limitWaiting = limitWaitingString.isEmpty() ? 9999 : Integer.parseInt(limitWaitingString);

            if (limitChosen <= 0 || limitWaiting <= 0) {
                Toast.makeText(getContext(), "Limits must be greater than zero.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean geo = locationSwitch.isChecked();
            Event newEvent = new Event(Control.getInstance().getEventIDForEventCreation(), eventTitle, eventDescription,limitChosen,limitWaiting,curUser,geo);

            // Pass the event to the listener
            if (listener != null) {
                listener.onEventCreated(newEvent);
            }
            dismiss(); // Close the dialog
        });

        cancelButton.setOnClickListener(v -> dismiss()); // Close the dialog if canceled

        return view;
    }
    protected boolean validDate(String date) {
        String regex = "^\\d{0,4}(-\\d{0,2})?(-\\d{0,2})?$";
        return date.length() == 10 && date.matches(regex);
    }

    protected boolean validPeriod(String start, String end) {
        LocalDate date1 = LocalDate.parse(start);
        LocalDate date2 = LocalDate.parse(end);
        return date1.isBefore(date2);
    }

    protected boolean validDates(String regStart, String regEnd, String eventStart, String eventEnd) {
        if (!validDate(regStart) || !validDate(regEnd) || !validDate(eventStart) || !validDate(eventEnd)) {
            Toast.makeText(getContext(), "Use date format: YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validPeriod(regStart, regEnd)) {
            Toast.makeText(getContext(), "Registration Start must precede Registration End", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ( !validPeriod(eventStart, eventEnd)) {
            Toast.makeText(getContext(), "Event Start must precede Event End", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!validPeriod(regEnd, eventStart)) {
            Toast.makeText(getContext(), "Registration End must precede Event Start", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

