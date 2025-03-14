package com.example.vehicletracking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class SignInFragment extends Fragment {
    private EditText edEmail, edPassword;
    private AppCompatButton SignInbutton;
    private FirebaseAuth firebaseAuth;
    private KProgressHUD progressHUD;
    private GoogleSignInClient signInClient;
    private Button GoogleSignIn_button;

    private final int REQUEST_CODE=100;

    public SignInFragment(){}

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        Button btnRegister = view.findViewById(R.id.btnRegisterTab);
        btnRegister.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new SignUpFragment()); // Переключаем на регистрацию
            transaction.addToBackStack(null);
            transaction.commit();
        });
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        SignInbutton = view.findViewById(R.id.signin);
        GoogleSignIn_button = view.findViewById(R.id.GoogleSignIn_button);
        firebaseAuth = FirebaseAuth.getInstance();
        CreateRequest();
        GoogleSignIn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
        SignInbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInUser();
            }

        });
        return view;
    }
    private void SignIn() {
        ProgressBar();
        Intent intent=signInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), ""+task.getResult(), Toast.LENGTH_SHORT).show();
                progressHUD.dismiss();
            }
        }

    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void ProgressBar(){
        progressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setBackgroundColor(R.color.blue_light)
                .show();
        progressHUD.setProgress(90);
    }

    private void CreateRequest() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(getContext(), signInOptions);
    }
    private void SignInUser(){
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
            Toast.makeText(getContext(), "Введите email и пароль", Toast.LENGTH_SHORT).show();
        } else {
            ProgressBar();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressHUD.dismiss();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
    }
    }
}
