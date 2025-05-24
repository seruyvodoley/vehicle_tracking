package com.example.vehicletracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.R;
import com.example.vehicletracking.presentation.TrackingIntent;
import com.example.vehicletracking.presentation.TrackingViewModel;
import com.example.vehicletracking.utils.WeatherHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private Marker marker;
    private TrackingViewModel viewModel;
    private String carName;
    private TextView weatherText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carName = getArguments().getString("carName");
        }
        viewModel = new ViewModelProvider(this).get(TrackingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = view.findViewById(R.id.mapView);
        weatherText = view.findViewById(R.id.weatherText); // обязательно добавь в XML
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        if (carName != null) {
            viewModel.processIntent(new TrackingIntent.StartTracking(carName));
        }

        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (state.error != null) {
                Toast.makeText(requireContext(), "Ошибка: " + state.error, Toast.LENGTH_SHORT).show();
            } else if (state.isTracking) {
                LatLng position = new LatLng(state.latitude, state.longitude);

                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions().position(position).title(carName));
                } else {
                    marker.setPosition(position);
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));

                // Получаем и отображаем погоду (один раз)
                fetchWeatherIfNeeded(position);
            }
        });
    }

    private boolean weatherLoaded = false;

    private void fetchWeatherIfNeeded(LatLng latLng) {
        if (weatherLoaded) return;

        WeatherHelper.getWeather(latLng, getString(R.string.openweather_api_key), new WeatherHelper.WeatherCallback() {
            @Override
            public void onSuccess(String description, double temperature) {
                requireActivity().runOnUiThread(() -> {
                    weatherText.setText("Погода: " + description + ", " + temperature + "°C");
                    weatherLoaded = true;
                });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() -> {
                    weatherText.setText("Ошибка загрузки погоды");
                });
            }
        });
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
    public void onDestroy() {
        viewModel.processIntent(new TrackingIntent.StopTracking());
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
