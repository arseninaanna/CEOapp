package com.example.ceo;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.access_token));
        setContentView(R.layout.activity_map);

        // Add the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.map);
        setSupportActionBar(toolbar);

        // Set back button on toolbar
        toolbar.setNavigationOnClickListener(v -> finish());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        // FIXME: Make this more elegant
        // Hardcoded inflating of map with moving markers.
        ArrayList<LatLng> init_positions = new ArrayList<>();
        init_positions.add(new LatLng(55.740652, 48.787812));
        init_positions.add(new LatLng(55.859167, 48.847512));
        init_positions.add(new LatLng(55.826962, 49.095361));
        init_positions.add(new LatLng(55.842353, 49.133652));
        init_positions.add(new LatLng(55.734666, 48.789760));
        init_positions.add(new LatLng(55.827350, 49.019847));
        init_positions.add(new LatLng(55.807866, 48.943318));
        init_positions.add(new LatLng(55.741655, 48.935471));
        init_positions.add(new LatLng(55.777371, 49.145207));
        init_positions.add(new LatLng(55.743659, 49.105047));

        ArrayList<LatLng> destinations = new ArrayList<>();
        destinations.add(new LatLng(55.761450, 48.817034));
        destinations.add(new LatLng(55.871049, 48.711339));
        destinations.add(new LatLng(55.826981, 49.146921));
        destinations.add(new LatLng(55.813524, 49.133618));
        destinations.add(new LatLng(55.711877, 48.883250));
        destinations.add(new LatLng(55.853112, 48.879084));
        destinations.add(new LatLng(55.801285, 48.976643));
        destinations.add(new LatLng(55.758480, 48.965482));
        destinations.add(new LatLng(55.793184, 49.153616));
        destinations.add(new LatLng(55.757012, 49.105556));


        // Adding markers at initial positions.
        for (int i = 0; i < init_positions.size(); i++) {
            mapboxMap.addMarker(new MarkerOptions()
                    .position(init_positions.get(i))
                    .title("Driver " + i)
                    .snippet(""));
        }

        // Build the bounds.
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng driver_i_from : init_positions) {
            boundsBuilder.include(driver_i_from);
        }

        // Animate the camera, so all the cars will be in the visible box.
        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));

        // Durations of anumation.
        int[] durations = {50000, 50000, 50000, 38400, 40600, 50700, 50000, 50800, 38400, 40000};

        // Getting the list of markers on our map.
        List<Marker> markers = mapboxMap.getMarkers();

        // Animating the markers due our coordinates.
        ValueAnimator markerAnimator;
        for (int i = 0; i < markers.size(); i++) {
            markerAnimator = ObjectAnimator.ofObject(markers.get(i), "position",
                    new LatLngEvaluator(), markers.get(i).getPosition(), destinations.get(i));
            markerAnimator.setDuration(durations[i]);
            markerAnimator.start();
        }
    }


    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {
        // Method is used to interpolate the marker animation.

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
