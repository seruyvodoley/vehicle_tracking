package com.example.vehicletracking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.R;
import com.example.vehicletracking.presentation.SignUpIntent;
import com.example.vehicletracking.presentation.SignUpState;
import com.example.vehicletracking.presentation.SignUpViewModel;

public class SignUpFragment extends Fragment {

    private EditText edName, edEmail, edPassword;
    private Button signUpButton;
    private SignUpViewModel viewModel;

    public SignUpFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        edName = view.findViewById(R.id.name);
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        signUpButton = view.findViewById(R.id.register);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        observeViewModel();

        signUpButton.setOnClickListener(v -> {
            String name = edName.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            viewModel.processIntent(new SignUpIntent.Submit(name, email, password));
        });

        return view;
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;

            if (state.isLoading) {
                Toast.makeText(getContext(), "Регистрация...", Toast.LENGTH_SHORT).show();
            } else if (state.success) {
                Toast.makeText(getContext(), state.message, Toast.LENGTH_LONG).show();
                navigateToSignIn();
            } else if (state.message != null) {
                Toast.makeText(getContext(), state.message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToSignIn() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new SignInFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
