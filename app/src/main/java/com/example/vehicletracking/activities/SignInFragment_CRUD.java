package com.example.vehicletracking.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.vehicletracking.R;
import com.example.vehicletracking.database.DatabaseHelper;

public class SignInFragment_CRUD extends Fragment {
    private EditText etEmail, etPassword;
    private Button btnSignIn, btnRegisterTab;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);
        btnSignIn = view.findViewById(R.id.signin);
        btnRegisterTab = view.findViewById(R.id.btnRegisterTab); // Кнопка переключения на регистрацию
        dbHelper = new DatabaseHelper(getContext());

        // **Вход пользователя**
        btnSignIn.setOnClickListener(v -> signInUser());

        // **Переключение на регистрацию**
        btnRegisterTab.setOnClickListener(v -> switchToSignUp());

        return view;
    }

    private void signInUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Cursor cursor = dbHelper.getUser(email);
        if (cursor != null && cursor.moveToFirst()) {
            String storedPassword = cursor.getString(3); // Индекс 2 — столбец "password"
            if (password.equals(storedPassword)) {
                Toast.makeText(getContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).openMainNavigation(email);
            } else {
                Toast.makeText(getContext(), "Неверный пароль!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Пользователь не найден!", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToSignUp() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, new SignUpFragment_CRUD()) // Переход на SignUpFragment
                .commit();
    }
}
