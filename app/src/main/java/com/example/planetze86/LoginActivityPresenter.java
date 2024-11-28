package com.example.planetze86;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class LoginActivityPresenter {
    private final LoginActivityView view;
    private final LoginActivityModel model;

    public LoginActivityPresenter(LoginActivityView view, LoginActivityModel model) {
        this.view = view;
        this.model = model;
    }

    public void loginUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            view.setOutput("Please enter your email and password");
            return;
        }

        model.loginUser(email, password, new LoginActivityModel.LoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user, boolean firstLogin) {
                if (firstLogin) {
                    goOnboarding();
                    model.updateFirstLogin(user.getUid());
                } else {
                    goEco();
                }
                view.finish();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.setOutput(errorMessage);
            }

            @Override
            public void onEmailNotVerified() {
                view.setOutput("Please verify your email before logging in.");
                model.signOut();
            }
        });
    }

    public void onForgotPasswordClicked() {
        Intent intent = new Intent(view, forgetPass.class);
        view.startActivity(intent);
    }

    public void onSignupButtonClicked() {
        Intent intent = new Intent(view, RegisterActivity.class);
        view.startActivity(intent);
    }

    public void onBackButtonClicked() {
        Intent intent = new Intent(view, MainActivity.class);
        view.startActivity(intent);
    }

    public void goOnboarding() {
        Intent intent = new Intent(view, Onboarding.class);
        view.startActivity(intent);
    }

    public void goEco() {
        Intent intent = new Intent(view, EcoTracker.class);
        view.startActivity(intent);
    }
}