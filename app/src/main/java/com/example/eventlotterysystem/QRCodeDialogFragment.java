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

public class QRCodeDialogFragment extends DialogFragment {
    private static final String viewEventArg = "fromViewEvent";

    public static QRCodeDialogFragment newInstance(boolean fromViewEvent) {
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean(viewEventArg, fromViewEvent);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_qrcode, container, false);

        assert getArguments() != null;
        boolean fromViewEvent = getArguments().getBoolean(viewEventArg, false);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> dismiss());

        TextView textViewHash = view.findViewById(R.id.textViewHash);
        TextView textViewStore = view.findViewById(R.id.textViewStore);

        Button buttonPublish = view.findViewById(R.id.buttonPublish);
        buttonPublish.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewEventActivity.class);
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