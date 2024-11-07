package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Dialog Fragment for displaying a map displaying where entrants join an event from
 * Not yet implemented so currently displays a placeholder image
 */
public class MapDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_map, container, false);

        ImageView mapImageView = view.findViewById(R.id.mapImageView);
        mapImageView.setImageResource(R.drawable.map_placeholder);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dismiss()); // Close the dialog when clicked

        return view;
    }
}