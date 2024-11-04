package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class EditProfileFragment extends DialogFragment {

    private EditText nameEditText, emailEditText, contactEditText;
    private OnProfileUpdatedListener profileUpdatedListener;

    public interface OnProfileUpdatedListener {
        void onProfileUpdated(String firstName, String lastName, String email);
    }

    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.profileUpdatedListener = listener;
    }

    public static EditProfileFragment newInstance(String name, String email, String contact) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("Name", name);
        args.putString("email", email);
        args.putString("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_user_fragment, container, false);

        nameEditText = view.findViewById(R.id.name);
        emailEditText = view.findViewById(R.id.email);
        contactEditText = view.findViewById(R.id.user_contact);

        if (getArguments() != null) {
            nameEditText.setText(getArguments().getString("name"));
            emailEditText.setText(getArguments().getString("email"));
            contactEditText.setText(getArguments().getString("contact"));
        }

        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        finishButton.setOnClickListener(v -> {
            if (profileUpdatedListener != null) {
                profileUpdatedListener.onProfileUpdated(
                        nameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        contactEditText.getText().toString()
                );
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        return view;
    }
}