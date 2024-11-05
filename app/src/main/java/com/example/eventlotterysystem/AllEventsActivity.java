package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllEventsActivity extends AppCompatActivity {
    private RecyclerView eventsRecyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_events_screen);

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(eventList);
        eventsRecyclerView.setAdapter(eventAdapter);

        loadMockEvents();

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());
    }

    private void loadMockEvents() {
        eventList.clear();

        eventList.add(new Event(0, "Mock Event 0", "Mockery Event",
                                5, 10, Control.getCurrentUser()));
        eventList.add(new Event(1, "Mock Event 1", "Mockery Event",
                                10, 20, Control.getCurrentUser()));

        eventAdapter.notifyDataSetChanged();
    }
}