package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
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

    private EditText firstNameEditText, lastNameEditText, emailEditText, contactEditText;
    private OnProfileUpdatedListener profileUpdatedListener;

    public interface OnProfileUpdatedListener {
        void onProfileUpdated(String firstName, String lastName, String email, String contact);
    }

    public void setOnProfileUpdatedListener(OnProfileUpdatedListener listener) {
        this.profileUpdatedListener = listener;
    }

    public static EditProfileFragment newInstance(String firstName, String lastName, String email, String contact) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        args.putString("email", email);
        args.putString("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_user_fragment, container, false);

        firstNameEditText = view.findViewById(R.id.firstName);
        lastNameEditText = view.findViewById(R.id.lastName);
        emailEditText = view.findViewById(R.id.email);
        contactEditText = view.findViewById(R.id.user_contact);

        if (getArguments() != null) {
            firstNameEditText.setHint(getArguments().getString("firstName"));
            lastNameEditText.setHint(getArguments().getString("lastName"));
            emailEditText.setHint(getArguments().getString("email"));
            contactEditText.setHint(getArguments().getString("contact"));
        }

        Button finishButton = view.findViewById(R.id.finish_button);
        Button cancelButton = view.findViewById(R.id.cancel_button);

        finishButton.setOnClickListener(v -> {
            if (profileUpdatedListener != null) {
                profileUpdatedListener.onProfileUpdated(
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
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