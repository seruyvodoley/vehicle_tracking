package com.example.vehicletracking.utils;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherHelper {

    public interface WeatherCallback {
        void onSuccess(String description, double temperature);
        void onError(String message);
    }

    public static void getWeather(LatLng location, String apiKey, WeatherCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" +
                location.latitude + "&lon=" + location.longitude +
                "&units=metric&lang=ru&appid=" + apiKey;

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Response error: " + response.code());
                    return;
                }

                String responseBody = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseBody);
                    String description = json.getJSONArray("weather")
                            .getJSONObject(0)
                            .getString("description");
                    double temperature = json.getJSONObject("main").getDouble("temp");
                    callback.onSuccess(description, temperature);
                } catch (JSONException e) {
                    callback.onError("JSON parsing error: " + e.getMessage());
                }
            }
        });
    }
}
