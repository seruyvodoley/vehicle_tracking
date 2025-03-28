package com.example.vehicletracking.activities;

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

public class SignUpFragment_CRUD extends Fragment {
    private EditText etUsername, etEmail, etPassword;
    private Button btnSignUp, btnLoginTab;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        etUsername = view.findViewById(R.id.name);
        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);
        btnSignUp = view.findViewById(R.id.register);
        btnLoginTab = view.findViewById(R.id.btnLoginTab); // Кнопка переключения на вход
        dbHelper = new DatabaseHelper(getContext());

        // **Регистрация пользователя**
        btnSignUp.setOnClickListener(v -> signUpUser());

        // **Переключение на вход**
        btnLoginTab.setOnClickListener(v -> switchToSignIn());

        return view;
    }

    private void signUpUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (dbHelper.insertUser(username, email, password)) {
            Toast.makeText(getContext(), "Регистрация успешна!", Toast.LENGTH_SHORT).show();
            switchToSignIn(); // После регистрации сразу на вход
        } else {
            Toast.makeText(getContext(), "Ошибка регистрации. Логин занят!", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToSignIn() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, new SignInFragment_CRUD()) // Переход на SignInFragment
                .commit();
    }
}
