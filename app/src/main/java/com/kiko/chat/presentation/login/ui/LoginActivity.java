package com.kiko.chat.presentation.login.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.kiko.chat.R;
import com.kiko.chat.presentation.base.BaseActivity;
import com.kiko.chat.presentation.login.LoginPresenter;
import com.kiko.chat.presentation.main.ui.MainActivity;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginPresenter.View {

    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.buttonNewUser)
    Button buttonNewUser;
    @BindView(R.id.emailInput)
    TextInputEditText emailInput;
    @BindView(R.id.passwordInput)
    TextInputEditText passwordInput;

    @Inject
    LoginPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getLifecycle().addObserver((LifecycleObserver) presenter);
        presenter.checkForAuthenticatedUser();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void enableInputs() {
        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    void setInputs(boolean enable) {
        emailInput.setEnabled(enable);
        passwordInput.setEnabled(enable);
        buttonLogin.setEnabled(enable);
        buttonNewUser.setEnabled(enable);
    }

    @Override
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @OnClick(R.id.buttonLogin)
    public void handleLogin() {
        String email = Objects.requireNonNull(emailInput.getText()).toString();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        presenter.validateLogin(email, password);
    }

    @OnClick(R.id.buttonNewUser)
    public void handleNewUser() {
        String email = Objects.requireNonNull(emailInput.getText()).toString();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        presenter.registerNewUser(email, password);
    }
}
