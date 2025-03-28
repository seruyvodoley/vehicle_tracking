package com.example.vehicletracking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vehicletracking.R;
import com.example.vehicletracking.activities.MainActivity;
import com.example.vehicletracking.modelview.SignInViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class SignInFragment extends Fragment {

    private EditText edEmail, edPassword;
    private Button SignInbutton, GoogleSignIn_button;
    private SignInViewModel signInViewModel;
    private KProgressHUD progressHUD;

    private final int REQUEST_CODE = 100;
    private GoogleSignInClient signInClient;

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        SignInbutton = view.findViewById(R.id.signin);
        GoogleSignIn_button = view.findViewById(R.id.GoogleSignIn_button);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        signInViewModel.getLoginStatus().observe(getViewLifecycleOwner(), status -> {
            if (progressHUD != null && progressHUD.isShowing()) {
                progressHUD.dismiss();
            }

            Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
            if (status.equals("Вход выполнен успешно")) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        // Кнопка перехода на регистрацию
        Button btnRegister = view.findViewById(R.id.btnRegisterTab);
        btnRegister.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new SignUpFragment()); // Переключаем на регистрацию
            transaction.addToBackStack(null);
            transaction.commit();
        });

        GoogleSignIn_button.setOnClickListener(v -> SignIn());
        SignInbutton.setOnClickListener(v -> SignInUser());

        CreateRequest();

        return view;
    }

    private void SignIn() {
        // Инициализация и отображение прогресса
        ProgressBar();
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                signInViewModel.signInWithGoogle(credential);
            } catch (ApiException e) {
                if (progressHUD != null && progressHUD.isShowing()) {
                    progressHUD.dismiss();
                }
                Toast.makeText(getContext(), "Ошибка входа через Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SignInUser() {
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        signInViewModel.signInWithEmailAndPassword(email, password);
    }

    private void CreateRequest() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getContext(), signInOptions);
    }

    // Метод для отображения индикатора загрузки
    private void ProgressBar() {
        progressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setBackgroundColor(R.color.blue_light)
                .show();
        progressHUD.setProgress(90);
    }
}
