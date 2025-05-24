package com.example.vehicletracking.fragments;

import android.content.Intent;
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
import com.example.vehicletracking.activities.MainActivity;
import com.example.vehicletracking.presentation.SignInEffect;
import com.example.vehicletracking.presentation.SignInIntent;
import com.example.vehicletracking.presentation.SignInState;
import com.example.vehicletracking.presentation.SignInViewModel;
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
    private Button btnSignIn, btnGoogleSignIn;
    private SignInViewModel viewModel;
    private KProgressHUD progressHUD;

    private final int REQUEST_CODE = 100;
    private GoogleSignInClient signInClient;

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        btnSignIn = view.findViewById(R.id.signin);
        btnGoogleSignIn = view.findViewById(R.id.GoogleSignIn_button);

        viewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        // Наблюдаем за состоянием
        viewModel.getState().observe(getViewLifecycleOwner(), this::renderState);
        viewModel.getEffect().observe(getViewLifecycleOwner(), this::handleEffect);

        btnSignIn.setOnClickListener(v -> {
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            viewModel.processIntent(new SignInIntent.SignInWithEmail(email, password));
        });

        btnGoogleSignIn.setOnClickListener(v -> {
            showProgress();
            Intent intent = signInClient.getSignInIntent();
            startActivityForResult(intent, REQUEST_CODE);
        });

        Button btnRegister = view.findViewById(R.id.btnRegisterTab);
        btnRegister.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new SignUpFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        setupGoogleSignIn();

        return view;
    }

    private void renderState(SignInState state) {
        if (state.isLoading) {
            showProgress();
        } else {
            hideProgress();
        }
    }

    private void handleEffect(SignInEffect effect) {
        if (effect instanceof SignInEffect.NavigateToMainScreen) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else if (effect instanceof SignInEffect.ShowToast) {
            String msg = ((SignInEffect.ShowToast) effect).message;
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(requireContext(), options);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                viewModel.processIntent(new SignInIntent.SignInWithGoogle(credential));
            } catch (ApiException e) {
                hideProgress();
                Toast.makeText(getContext(), "Ошибка Google входа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showProgress() {
        if (progressHUD == null) {
            progressHUD = KProgressHUD.create(requireContext())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setCancellable(false)
                    .setLabel("Загрузка...");
        }
        progressHUD.show();
    }

    private void hideProgress() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }
}
