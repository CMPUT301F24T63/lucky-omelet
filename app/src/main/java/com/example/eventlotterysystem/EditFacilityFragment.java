package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditFacilityFragment extends DialogFragment {
    private EditText nameEditText, descriptionEditText;
    private OnFacilityUpdatedListener facilityUpdatedListener;

    public interface OnFacilityUpdatedListener {
        void onFacilityUpdated(String name, String description);
    }

    public void setOnFacilityUpdatedListener(OnFacilityUpdatedListener listener) {
        this.facilityUpdatedListener = listener;
    }

    public static EditFacilityFragment newInstance(String name, String description) {
        EditFacilityFragment fragment = new EditFacilityFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_facility_fragment, container, false);

        nameEditText = view.findViewById(R.id.facility_name_edit);
        descriptionEditText = view.findViewById(R.id.facility_description_edit);

        if (getArguments() != null) {
            nameEditText.setText(getArguments().getString("name"));
            descriptionEditText.setText(getArguments().getString("description"));
        }

        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        finishButton.setOnClickListener(v -> {
            if (facilityUpdatedListener != null) {
                facilityUpdatedListener.onFacilityUpdated(
                        nameEditText.getText().toString(),
                        descriptionEditText.getText().toString()
                );
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.99);
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);
    }
}
