package com.example.eventlotterysystem;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class NotifyFragment extends DialogFragment {

    public interface NotificationListener {
        void onNotify(String message);
    }

    private NotificationListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NotificationListener) {
            listener = (NotificationListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement NotificationListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View dialogView = requireActivity().getLayoutInflater().inflate(R.layout.dialog_list_manage_notify, null);

        // Reference the correct EditText within TextInputLayout
        EditText messageEditText = dialogView.findViewById(R.id.textInputEditText);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogView)
                .setTitle("Notify List?")
                .setPositiveButton("Notify", (dialog, id) -> {
                    String message = messageEditText.getText().toString().trim();
                    if (listener != null) {
                        listener.onNotify(message);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        return builder.create();
    }
}
