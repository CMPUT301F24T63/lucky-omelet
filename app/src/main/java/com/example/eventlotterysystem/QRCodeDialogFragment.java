package com.example.eventlotterysystem;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * Dialog Fragment for displaying a QR Code
 * @param "fromViewEvent" if the QR Code is displayed from ViewEventActivity,
 * hides hash code and buttons that only appear when the event is first created
 * @param "eventId" the ID of the event that is then passed to ViewEventActivity
 * Code to generate QR and Hash codes not yet implemented, so there are placeholders
 */
public class QRCodeDialogFragment extends DialogFragment {
    private static final String viewEventArg = "fromViewEvent";
    private static final String eventIdArg = "eventId";

    /**
     * Factory method to create a new instance of QRCodeDialogFragment
     * @param fromViewEvent check if the dialog is displayed from ViewEventActivity
     * @param eventId the ID of the event that is then passed to ViewEventActivity
     * @return the fragment with the correct arguments
     */
    public static QRCodeDialogFragment newInstance(boolean fromViewEvent, int eventId) {
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(viewEventArg, fromViewEvent);
        args.putInt(eventIdArg, eventId); // Pass eventId as an argument
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_qrcode, container, false);

        //assert getArguments() != null;
        boolean fromViewEvent = getArguments().getBoolean(viewEventArg, false);
        int eventId = getArguments().getInt(eventIdArg, -1);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> dismiss());

        TextView textViewHash = view.findViewById(R.id.textViewHash);
        TextView textViewStore = view.findViewById(R.id.textViewStore);

        Button buttonPublish = view.findViewById(R.id.buttonPublish);
        buttonPublish.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageEventActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
            dismiss();
        });

        if (fromViewEvent) {
            textViewHash.setVisibility(View.GONE);
            textViewStore.setVisibility(View.GONE);
            buttonPublish.setVisibility(View.GONE);
        }

        return view;
    }
}