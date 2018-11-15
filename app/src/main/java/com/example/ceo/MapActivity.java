package com.example.ceo;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Response;
import com.example.ceo.requests.BackendAPI;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private MapboxMap boxMap;
    JSONArray array;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

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

    public double[][] parseJSON(JSONArray array) throws JSONException {
        ArrayList<double[][]> list = new ArrayList<>();
        double[][] data;
        JSONObject object = array.getJSONObject(0);
        JSONArray x = object.getJSONArray("x");
        System.out.println(x.toString());

        JSONArray y = object.getJSONArray("y");
        JSONArray id = object.getJSONArray("id");
        System.out.println(y.toString());
        data = new double[y.length()][3];
        for (int i = 0; i < x.length(); i++) {
            data[i][0] = (Double) x.get(i);
            data[i][1] = (Double) y.get(i);
            data[i][2] = (Integer) id.get(i);
        }
        return data;
    }

    public void initMarkers(double[][] list) {
        boxMap.clear();

        ArrayList<LatLng> init_positions = new ArrayList<>();
        for(int i = 0; i < list.length; i++){
            init_positions.add(new LatLng(list[i][0], list[i][1]));
        }

        // Adding markers at initial positions.
        for (int i = 0; i < init_positions.size(); i++) {
            boxMap.addMarker(new MarkerOptions()
                    .position(init_positions.get(i))
                    .title("Driver " + list[i][2])
                    .snippet(""));
        }
    }

    public void moveMarkers(double[][] list) {
        ArrayList<LatLng> destinations = new ArrayList<>();
        for(int i = 0; i < list.length; i++){
            destinations.add(new LatLng(list[i][0], list[i][1]));
        }

        // Durations of anumation.
        int duration = 50000;

        // Getting the list of markers on our map.
        List<Marker> markers = boxMap.getMarkers();

        // Animating the markers due our coordinates.
        ValueAnimator markerAnimator;
        for (int i = 0; i < markers.size(); i++) {
            Marker current = markers.get(i);

            markerAnimator = ObjectAnimator.ofObject(current, "position",
                    new LatLngEvaluator(), current.getPosition(), destinations.get(i));

            markerAnimator.setDuration(duration);
            markerAnimator.start();
        }
    }

    public void resizeMap() {
        // Build the bounds.
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (Marker driver_i_pos : boxMap.getMarkers()) {
            boundsBuilder.include(driver_i_pos.getPosition());
        }

        // Animate the camera, so all the cars will be in the visible box.
        boxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 100));
    }


    public void makeHTTPGet(Response.Listener<JSONArray> respCb){
        BackendAPI api = new BackendAPI(this);
        api.get("/map", respCb, error -> {
            Toast.makeText(getBaseContext(), "Problems with server, sorry cannot render",
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        boxMap = mapboxMap;

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                System.out.println("Hi from thread");
                makeHTTPGet(response -> {
                    array = response;
                    System.out.println("Map: " + array.toString());
                    try {
                        double[][] list = parseJSON(array);
                        System.out.println(list.toString());
                        if (boxMap.getMarkers().size() > 0) {
                            moveMarkers(list);
                        } else {
                            initMarkers(list);
                        }

                        resizeMap();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

            }
        }, 0, 10, TimeUnit.SECONDS);

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
        scheduler.shutdown();
        System.out.println("shutting down...........");
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
