package com.example.vehicletracking.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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

public class ChangePasswordFragment extends Fragment {
    private EditText etOldPassword, etNewPassword;
    private Button btnSavePassword;
    private DatabaseHelper dbHelper;
    private String email;

    public static ChangePasswordFragment newInstance(String email) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        etOldPassword = view.findViewById(R.id.oldpassword);
        etNewPassword = view.findViewById(R.id.newpassword);
        btnSavePassword = view.findViewById(R.id.savepassword);
        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            email = getArguments().getString("email");
        }

        btnSavePassword.setOnClickListener(v -> updatePassword());

        return view;
    }

    private void updatePassword() {
        String oldPassword = etOldPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверяем старый пароль перед обновлением
        Cursor cursor = dbHelper.getPassword(oldPassword);
        if (cursor == null || !cursor.moveToFirst()) {
            Log.d("DB Result", "Нет пароля: " + oldPassword);  // Логирование
            Toast.makeText(getContext(), "Неверный текущий пароль!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (dbHelper.updateUserPassword(email, newPassword)) {
                Log.d("DB Result", "Пароль найден...");  // Логирование успешного поиска пароля
                Toast.makeText(getContext(), "Пароль успешно изменен!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack(); // Возвращаемся назад
            } else {
                Toast.makeText(getContext(), "Ошибка изменения пароля!", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
