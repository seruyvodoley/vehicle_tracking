package com.example.vehicletracking.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.vehicletracking.R;
import com.example.vehicletracking.fragments.MapFragment;
import com.example.vehicletracking.fragments.ProfileFragment;
import com.example.vehicletracking.fragments.SignInFragment;
import com.example.vehicletracking.fragments.TransportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE); // Скрываем по умолчанию

        FirebaseApp.initializeApp(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new SignInFragment()) // По умолчанию экран входа
                    .commit();
        }

        checkUserStatus();
    }

    // Метод для проверки статуса пользователя
    private void checkUserStatus() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            openSignInFragment(); // Открываем экран входа и скрываем панель навигации
        } else {
            currentUser.getIdToken(true).addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().getToken() != null) {
                    openMainNavigation();
                } else {
                    openSignInFragment();
                }
            });
        }
    }

    // Метод для показа навигационной панели
    private void openMainNavigation() {
        bottomNavigationView.setVisibility(View.VISIBLE); // Делаем панель видимой

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else if (item.getItemId() == R.id.nav_transport) {
                selectedFragment = new TransportFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, selectedFragment)
                        .commit();
            }
            return true;
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ProfileFragment())
                .commit();
    }

    // Метод для выхода из профиля
    public void logOut() {
        firebaseAuth.signOut();
        openSignInFragment();
    }

    // Метод для открытия экрана входа и скрытия навигации
    private void openSignInFragment() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE); // Гарантированно скрываем панель
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignInFragment())
                .commit();
    }
}
