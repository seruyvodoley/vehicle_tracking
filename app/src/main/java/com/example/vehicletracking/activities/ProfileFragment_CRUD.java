package com.example.vehicletracking.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.example.vehicletracking.R;
import com.example.vehicletracking.activities.MainActivity;
import com.example.vehicletracking.database.DatabaseHelper;
import com.example.vehicletracking.fragments.ChangePasswordFragment;

public class ProfileFragment_CRUD extends Fragment {
    private TextView tvEmail;
    private ImageButton btnUpdatePassword, btnDeleteUser;
    private Button btnLogout;
    private DatabaseHelper dbHelper;
    private String email;

    public static ProfileFragment_CRUD newInstance(String email) {
        ProfileFragment_CRUD fragment = new ProfileFragment_CRUD();
        Bundle args = new Bundle();
        args.putString("email", email);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        tvEmail = view.findViewById(R.id.userEmail);
        btnDeleteUser = view.findViewById(R.id.btnDeleteUser);
        btnLogout = view.findViewById(R.id.btnLogout);
        dbHelper = new DatabaseHelper(getContext());

        if (getArguments() != null) {
            email = getArguments().getString("email");
            tvEmail.setText(email);
        }

        // **Обновление пароля**
        ImageButton btnGoToChangePassword = view.findViewById(R.id.btnChangePassword);
        btnGoToChangePassword.setOnClickListener(v -> {
            ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance(email);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, changePasswordFragment)
                    .addToBackStack(null)
                    .commit();
        });


        // **Удаление пользователя**
        btnDeleteUser.setOnClickListener(v -> deleteUser());

        // **Выход из системы**
        btnLogout.setOnClickListener(v -> {
            ((MainActivity) getActivity()).logOut();
        });

        return view;
    }

    private void deleteUser() {
        if (dbHelper.deleteUser(email)) {
            Toast.makeText(getContext(), "Аккаунт удален!", Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).logOut();
        } else {
            Toast.makeText(getContext(), "Ошибка удаления!", Toast.LENGTH_SHORT).show();
        }
    }
}
