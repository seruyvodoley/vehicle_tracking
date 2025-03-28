package com.example.vehicletracking.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.vehicletracking.R;
import com.example.vehicletracking.modelview.SignUpViewModel;

public class SignUpFragment extends Fragment {

    private EditText edName, edEmail, edPassword;
    private Button signUpButton;
    private SignUpViewModel signUpViewModel;

    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        edName = view.findViewById(R.id.name);
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        signUpButton = view.findViewById(R.id.register);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpViewModel.getRegistrationStatus().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Toast.makeText(getContext(), status, Toast.LENGTH_LONG).show();
                if (status.equals("Пользователь успешно зарегистрирован")) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, new SignInFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        signUpButton.setOnClickListener(v -> {
            String name = edName.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            signUpViewModel.signUpUser(name, email, password);
        });

        return view;
    }
}
