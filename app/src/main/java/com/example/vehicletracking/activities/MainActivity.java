package com.example.vehicletracking.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.vehicletracking.R;
import com.example.vehicletracking.fragments.MapFragment;
import com.example.vehicletracking.activities.ProfileFragment_CRUD;
import com.example.vehicletracking.fragments.SignInFragment;
import com.example.vehicletracking.fragments.TransportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private String currentUsername; // Имя текущего пользователя

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE); // По умолчанию скрываем навигацию

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SignInFragment_CRUD()) // Стартовый экран - вход
                    .commit();
        }
    }

    // **Метод для показа навигационной панели после входа**
    public void openMainNavigation(String username) {
        this.currentUsername = username; // Запоминаем пользователя
        bottomNavigationView.setVisibility(View.VISIBLE); // Показываем навигацию

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else if (item.getItemId() == R.id.nav_transport) {
                selectedFragment = new TransportFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = ProfileFragment_CRUD.newInstance(currentUsername);
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Открываем профиль сразу после входа
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ProfileFragment_CRUD.newInstance(currentUsername))
                .commit();
    }

    // **Метод для выхода из системы**
    public void logOut() {
        currentUsername = null;
        bottomNavigationView.setVisibility(View.GONE); // Скрываем навигацию
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignInFragment()) // Переход на экран входа
                .commit();
    }
}
