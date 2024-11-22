package com.example.eventlotterysystem;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Previous UI approach to creating an event
 */
public class CreateEventDialogFragment extends DialogFragment {

    private CreateEventListener listener;
    private User curUser;

    private ImageView imagePreview;
    private ImageButton removeImageButton;
    private Button uploadImageButton;
    private Uri selectedImageUri;

    // ActivityResultLauncher for image selection
    private ActivityResultLauncher<String> pickImageLauncher;

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
        ImageView imagePreview = view.findViewById(R.id.imagePreview);
        ImageButton removeImageButton = view.findViewById(R.id.removeImageButton);
        Button uploadImageButton = view.findViewById(R.id.uploadImage_button);
        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        // Initialize ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imagePreview.setVisibility(View.VISIBLE);
                        removeImageButton.setVisibility(View.VISIBLE);
                        Glide.with(this)
                                .load(uri)
                                .into(imagePreview);
                    }
                }
        );

        // Set Upload Image Button Listener
        uploadImageButton.setOnClickListener(v -> {
            // Launch the image picker
            pickImageLauncher.launch("image/*");
        });

        removeImageButton.setOnClickListener(v -> {
            selectedImageUri = null;
            imagePreview.setVisibility(View.GONE);
            removeImageButton.setVisibility(View.GONE);
            imagePreview.setImageDrawable(null);
            Toast.makeText(getContext(), "Image removed", Toast.LENGTH_SHORT).show();
        });

        finishButton.setOnClickListener(v -> {
            // Create a new Event using user input
            String eventTitle = titleEdit.getText().toString().trim();
            String eventDescription = descriptionEdit.getText().toString().trim();
            String limitChosenString = limitChosenEdit.getText().toString().trim();
            String limitWaitingString = limitWaitingEdit.getText().toString().trim();

            if (eventTitle.isEmpty() || eventDescription.isEmpty() || limitChosenString.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in required fields", Toast.LENGTH_SHORT).show();
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

//            // Show a Progress Dialog
//            ProgressDialog progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Creating event...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();

            dismiss(); // Close the dialog
        });

        cancelButton.setOnClickListener(v -> dismiss()); // Close the dialog if canceled

        return view;
    }
}
