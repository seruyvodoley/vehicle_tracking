package com.example.vehicletracking.fragments;

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
import com.example.vehicletracking.modelview.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChangePasswordFragment extends Fragment {

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button savePasswordButton;
    private ProfileViewModel profileViewModel;
    private BottomNavigationView bottomNavigationView;

    public ChangePasswordFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        oldPasswordEditText = view.findViewById(R.id.oldpassword);
        newPasswordEditText = view.findViewById(R.id.newpassword);
        savePasswordButton = view.findViewById(R.id.savepassword);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        profileViewModel = new ProfileViewModel();

        bottomNavigationView.setVisibility(View.VISIBLE);

        savePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            } else {
                profileViewModel.changePassword(oldPassword, newPassword);
                Toast.makeText(getContext(), "Пароль успешно изменен", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}
