package com.example.vehicletracking.tracking.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectionsHelper {

    public interface RouteCallback {
        void onRouteReceived(List<LatLng> routePoints);
        void onError(String message);
    }

    public static void getRoute(LatLng origin, LatLng destination, String apiKey, RouteCallback callback) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&key=" + apiKey;
        Log.d("DirectionsHelper", "URL запроса: " + url);

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("Ошибка сети");
                    return;
                }

                String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    String status = json.getString("status");
                    Log.e("DirectionsStatus", status);
                    if (!"OK".equals(status)) {
                        callback.onError("Ошибка Directions API: " + status);
                        return;
                    }
                    JSONArray routes = json.getJSONArray("routes");

                    if (routes.length() == 0) {
                        callback.onError("Маршрут не найден");
                        return;
                    }

                    JSONObject overviewPolyline = routes.getJSONObject(0).getJSONObject("overview_polyline");
                    String points = overviewPolyline.getString("points");

                    List<LatLng> decodedPath = decodePoly(points);
                    callback.onRouteReceived(decodedPath);

                } catch (JSONException e) {
                    callback.onError("Ошибка парсинга: " + e.getMessage());
                }
            }
        });
    }

    // Метод декодирования polyline
    private static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((lat / 1E5), (lng / 1E5));
            poly.add(p);
        }

        return poly;
    }
}
