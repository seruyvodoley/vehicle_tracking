package com.example.vehicletracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.R;
import com.example.vehicletracking.modelview.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProfileViewModel profileViewModel;

    private TextView userName, userEmail;
    private Button btnLogout;
    private ImageButton btnChangePassword, btnDeleteUser;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.userName);
        userEmail = view.findViewById(R.id.userEmail);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnDeleteUser = view.findViewById(R.id.btnDeleteUser);

        mAuth = FirebaseAuth.getInstance();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getProfileStatus().observe(getViewLifecycleOwner(), status -> {
            Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            if (status.equals("Выход из аккаунта выполнен") || status.equals("Аккаунт успешно удален")) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new SignInFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            userName.setText(displayName != null ? displayName : "Имя пользователя");
            userEmail.setText(currentUser.getEmail());
        } else {
            Toast.makeText(getContext(), "Пользователь не аутентифицирован", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(v -> profileViewModel.logOut(getActivity()));

        btnChangePassword.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, new ChangePasswordFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnDeleteUser.setOnClickListener(v -> profileViewModel.deleteUser(getActivity()));

        return view;
    }
}
