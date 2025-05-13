package com.example.vehicletracking.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.R;
import com.example.vehicletracking.tracking.utils.DirectionsHelper;
import com.example.vehicletracking.tracking.utils.MapTrackingViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MapFragment extends Fragment {

    private MapView mapView;
    private double latitude;
    private double longitude;
    private String carName;
    private MapTrackingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);

        // ✅ Получаем аргументы сразу, до getMapAsync
        if (getArguments() != null) {
            try {
                latitude = Double.parseDouble(getArguments().getString("latitude", "0.0"));
                longitude = Double.parseDouble(getArguments().getString("longitude", "0.0"));
            } catch (NumberFormatException e) {
                latitude = 0.0;
                longitude = 0.0;
                e.printStackTrace(); // или лог
            }
            carName = getArguments().getString("carName", "Автомобиль");
        }


        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(requireActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewModel = new ViewModelProvider(this).get(MapTrackingViewModel.class);

        // ✅ Единственный вызов getMapAsync — уже с правильными координатами
        mapView.getMapAsync(googleMap -> {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            googleMap.setBuildingsEnabled(true);
            googleMap.setTrafficEnabled(true);
            googleMap.setIndoorEnabled(true);

            // Центровка карты
            LatLng origin = new LatLng(latitude, longitude); // старт
            LatLng destination = new LatLng(latitude + 0.005, longitude + 0.005); // примерная цель (можно заменить)

// вызов API
            DirectionsHelper.getRoute(origin, destination, getString(R.string.google_maps_key),
                    new DirectionsHelper.RouteCallback() {
                        @Override
                        public void onRouteReceived(List<LatLng> routePoints) {
                            requireActivity().runOnUiThread(() -> {
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .addAll(routePoints)
                                        .color(Color.BLUE)
                                        .width(10);
                                googleMap.addPolyline(polylineOptions);

                                Marker carMarker = googleMap.addMarker(new MarkerOptions().position(origin).title("Авто"));
                                viewModel.RouteSimulateMovement(routePoints, googleMap, carMarker);
                            });
                        }

                        @Override
                        public void onError(String message) {
                            Log.e("RouteError", message);
                        }
                    });


            // Старт трекинга
            viewModel.startTracking(latitude, longitude, googleMap, carName);

            // Масштабная шкала
            googleMap.setOnCameraIdleListener(() -> {
                float zoom = googleMap.getCameraPosition().zoom;

                String scaleText;
                if (zoom >= 18) {
                    scaleText = "Масштаб: 20 м";
                } else if (zoom >= 16) {
                    scaleText = "Масштаб: 50 м";
                } else if (zoom >= 14) {
                    scaleText = "Масштаб: 100 м";
                } else if (zoom >= 12) {
                    scaleText = "Масштаб: 500 м";
                } else {
                    scaleText = "Масштаб: 1 км+";
                }

                TextView scaleView = requireView().findViewById(R.id.scaleText);
                scaleView.setText(scaleText);
            });
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        viewModel.stopTracking();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
