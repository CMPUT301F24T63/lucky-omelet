package com.example.eventlotterysystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapDialogFragment extends DialogFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog_MinWidth);  // Default theme
    }

//    @Override
    public View onCreateView(LayoutInflater inflater, View container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_map, (ViewGroup) container, false);

        // Set up the cancel button to dismiss the dialog
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> dismiss());

        // Initialize the map
        initializeGoogleMap(view);

        return view;
    }

    private void initializeGoogleMap(View view) {
        // Get the FrameLayout where the map fragment will be added
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFrame);

        if (mapFragment == null) {
            // If no map fragment exists, create a new one
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.mapFrame, mapFragment);
            transaction.commit();
        }

        // Set up the map once it's ready
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Set a default world map view centered on the equator with zoom level 2
        LatLng worldCenter = new LatLng(0.0, 0.0);  // Coordinates for the center of the world map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(worldCenter, 2));
    }
}
