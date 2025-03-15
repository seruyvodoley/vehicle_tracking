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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.vehicletracking.R;
import com.example.vehicletracking.activities.MainActivity;
import com.example.vehicletracking.models.userModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;

public class SignUpFragment extends Fragment {

    private EditText edName, edEmail, edPassword, edNumber;
    private Button signUpButton, googleSignUpButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private GoogleSignInClient signInClient;
    private KProgressHUD progressHUD;
    private static final int REQUEST_CODE = 100;
    private String Userid;

    public SignUpFragment() {
        // Пустой конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Инициализация Firebase и Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Инициализация UI элементов
        edName = view.findViewById(R.id.name);
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        signUpButton = view.findViewById(R.id.register);
        googleSignUpButton = view.findViewById(R.id.GoogleSignIn);
        Button btnLogin = view.findViewById(R.id.btnLoginTab);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(requireContext(), gso);


        // Кнопка перехода на страницу входа
        btnLogin.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.container, new SignInFragment()); // Переключаем на вход
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Кнопка регистрации пользователя
        signUpButton.setOnClickListener(v -> SignUpUser());

        // Кнопка входа через Google
        googleSignUpButton.setOnClickListener(v -> SignIn());

        return view;
    }

    // Метод регистрации пользователя
    private void SignUpUser() {
        String name = edName.getText().toString().trim();
        String email = edEmail.getText().toString().trim();
        String password = edPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_LONG).show();
            return;
        }

        ProgressBar();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Userid = firebaseAuth.getCurrentUser().getUid();
                }
                DocumentReference UserInfo = firestore.collection("Users").document(Userid);
                userModel model = new userModel(name, email, password, Userid);
                UserInfo.set(model, SetOptions.merge()).addOnSuccessListener(unused -> {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    progressHUD.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        }).addOnFailureListener(e -> {
            progressHUD.dismiss();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Метод входа через Google
    private void SignIn() {
        ProgressBar();
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, REQUEST_CODE);
    }

    // Метод обработки результата Google Sign-In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "Ошибка входа через Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Метод авторизации Firebase через Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressHUD.dismiss();
                Toast.makeText(getContext(), "Вход выполнен успешно", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(e -> {
            progressHUD.dismiss();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Метод отображения индикатора загрузки
    private void ProgressBar() {
        progressHUD = KProgressHUD.create(requireContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setMaxProgress(100)
                .setBackgroundColor(R.color.blue_light)
                .show();
        progressHUD.setProgress(90);
    }
}
