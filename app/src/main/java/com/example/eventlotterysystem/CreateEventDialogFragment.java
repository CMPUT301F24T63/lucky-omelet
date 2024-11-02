package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateEventDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this dialog fragment
        View view = inflater.inflate(R.layout.edit_event_fragment, container, false);

        // Initialize UI components
        EditText titleEdit = view.findViewById(R.id.firstName);
        EditText descriptionEdit = view.findViewById(R.id.title_edit5);
        EditText waitingListLimitEdit = view.findViewById(R.id.editTextNumber);
        EditText eventLimitEdit = view.findViewById(R.id.editTextNumber2);
        EditText openDateEdit = view.findViewById(R.id.editTextDate3);
        EditText registerDateEdit = view.findViewById(R.id.editTextDate4);
        EditText priceEdit = view.findViewById(R.id.editTextNumber3);
        Switch locationSwitch = view.findViewById(R.id.location_loc);

        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // Set up button click listeners
        finishButton.setOnClickListener(v -> {
            // Handle the finish action
            dismiss(); // Close the dialog
        });

        cancelButton.setOnClickListener(v -> {
            dismiss(); // Close the dialog
        });

        return view;
    }
}
