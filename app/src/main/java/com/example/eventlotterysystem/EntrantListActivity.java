package com.example.eventlotterysystem;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class EntrantListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EntrantAdapter adapter;
    private ArrayList<User> entrantList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        entrantList = new ArrayList<>();
        adapter = new EntrantAdapter(entrantList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Get event ID from intent
        String eventId = getIntent().getStringExtra("eventId");
        String listType = getIntent().getStringExtra("listType");

        // Fetch data based on list type
        fetchEntrantList(eventId, listType);
    }

    private void fetchEntrantList(String eventId, String listType) {
        getEventById(eventId, event -> {
            if (event != null) {
                switch (listType) {
                    case "entrants":
                        entrantList.addAll(event.getWaitingList());
                        break;
                    case "chosen":
                        entrantList.addAll(event.getChosenList());
                        break;
                    case "cancelled":
                        entrantList.addAll(event.getCancelledList());
                        break;
                    case "enrolled":
                        entrantList.addAll(event.getFinalList());
                        break;
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Event not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEventById(String eventId, EventCallback callback) {
        db.collection("events").document(eventId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Event event = document.toObject(Event.class);
                    callback.onCallback(event);
                } else {
                    callback.onCallback(null);
                    Toast.makeText(this, "Event does not exist", Toast.LENGTH_SHORT).show();
                }
            } else {
                callback.onCallback(null);
                Toast.makeText(this, "Error fetching event", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private interface EventCallback {
        void onCallback(Event event);
    }
}