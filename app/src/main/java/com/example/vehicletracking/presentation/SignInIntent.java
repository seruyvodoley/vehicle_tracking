package com.example.vehicletracking.presentation;

import com.google.firebase.auth.AuthCredential;

public interface SignInIntent {
    class SignInWithEmail implements SignInIntent {
        public final String email, password;

        public SignInWithEmail(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    class SignInWithGoogle implements SignInIntent {
        public final AuthCredential credential;

        public SignInWithGoogle(AuthCredential credential) {
            this.credential = credential;
        }
    }
}
