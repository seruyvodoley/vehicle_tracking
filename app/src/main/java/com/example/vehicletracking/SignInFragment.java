package com.example.vehicletracking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    private EditText edEmail, edPassword;
    private AppCompatButton SignInbutton;
    private FirebaseAuth firebaseAuth;
    //private KProgressHUD progressHUD;


    public SignInFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        edEmail = view.findViewById(R.id.email);
        edPassword = view.findViewById(R.id.password);
        //SignInbutton = view.findViewById(R.id.SignIn);

        return view;
    }
}
