package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FacilitiesListActivity extends AppCompatActivity {

    private ListView facilitiesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> facilitiesList;
    private ArrayList<Facility> facilities;
    private ArrayList<String> filteredFacilitiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_list);

        facilitiesListView = findViewById(R.id.facilities_list_view);
        facilitiesList = new ArrayList<>();
        filteredFacilitiesList = new ArrayList<>();
        facilities = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredFacilitiesList);
        facilitiesListView.setAdapter(adapter);

        facilitiesListView.setOnItemClickListener((parent, view, position, id) -> {
            Facility selectedFacility = facilities.get(position);
            Intent intent = new Intent(FacilitiesListActivity.this, AdminViewFacilityActivity.class);
            intent.putExtra("facility", selectedFacility);
            startActivity(intent);
        });

        fetchFacilities();

        // Set up back button listener
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(view -> finish());

        // Set up search functionality
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterFacilities(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterFacilities(newText);
                return false;
            }
        });
    }

    private void fetchFacilities() {
        FirestoreManager.getInstance().loadFacilities(Control.getInstance(), new FirestoreManager.FacilitiesCallback() {
            @Override
            public void onCallback(ArrayList<Facility> facilities) {
                FacilitiesListActivity.this.facilities.clear();
                FacilitiesListActivity.this.facilities.addAll(facilities);
                facilitiesList.clear();
                for (Facility facility : facilities) {
                    facilitiesList.add(facility.getName());
                }
                filterFacilities(""); // Initialize the filtered list with all facilities
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FacilitiesListActivity", "Error fetching facilities", e);
            }
        });
    }

    private void filterFacilities(String query) {
        filteredFacilitiesList.clear();
        for (String facilityName : facilitiesList) {
            if (facilityName.toLowerCase().contains(query.toLowerCase())) {
                filteredFacilitiesList.add(facilityName);
            }
        }
        adapter.notifyDataSetChanged();
    }
}