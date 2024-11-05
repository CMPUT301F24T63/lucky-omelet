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

public class ManageEventMembersDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_manage_event_members, container, false);


        TextView waitingList = view.findViewById(R.id.waitingList);
        TextView chosenList = view.findViewById(R.id.chosenList);
        TextView cancelledList = view.findViewById(R.id.cancelledList);
        TextView finalList = view.findViewById(R.id.finalList);

        Button cancelButton = view.findViewById(R.id.cancel_button_managemembers);
        cancelButton.setOnClickListener(v -> dismiss());

//        waitingList.setOnClickListener(v -> openActivity(WaitingListActivity.class));
//        chosenList.setOnClickListener(v -> openActivity(ChosenListActivity.class));
//        cancelledList.setOnClickListener(v -> openActivity(CancelledListActivity.class));
//        finalList.setOnClickListener(v -> openActivity(FinalListActivity.class));

        return view;
    }

//    private void openActivity(Class<?> activityClass) {
//        Intent intent = new Intent(getActivity(), activityClass);
//        startActivity(intent);
//        dismiss();
//    }
}
