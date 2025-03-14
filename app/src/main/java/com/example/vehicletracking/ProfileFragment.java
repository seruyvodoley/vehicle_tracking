package com.example.vehicletracking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Инициализация FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Обновляем данные профиля
        TextView userName = view.findViewById(R.id.userName);
        TextView userEmail = view.findViewById(R.id.userEmail);

        if (mAuth.getCurrentUser() != null) {
            userName.setText(mAuth.getCurrentUser().getDisplayName() != null ? mAuth.getCurrentUser().getDisplayName() : "Имя пользователя");
            userEmail.setText(mAuth.getCurrentUser().getEmail());
        }

        // Кнопка выхода из аккаунта
        Button logoutButton = view.findViewById(R.id.btnLogout);
        logoutButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).logOut();
        });


        return view;
    }
}
