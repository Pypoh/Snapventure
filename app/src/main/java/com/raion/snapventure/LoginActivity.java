package com.raion.snapventure;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText _inputEmail, _inputPasswd;
    private ActionProcessButton _signInButton;
    private TextView _toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _inputEmail = findViewById(R.id.input_email_login);
        _inputPasswd = findViewById(R.id.input_password_login);
        _signInButton = findViewById(R.id.btn_SignIn);
        _toSignUp = findViewById(R.id.to_signUp_text);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            toHomePageActivity();
        }

        //Set Button Mode
        _signInButton.setMode(ActionProcessButton.Mode.ENDLESS);

        _signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _signInButton.setEnabled(false);
                handleLogin();
            }
        });

        _toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toRegisterActivity();
            }
        });
    }

    private void handleLogin() {
        _signInButton.setProgress(1);
        String email = _inputEmail.getText().toString();
        String password = _inputPasswd.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    toHomePageActivity();
                } else {
                    _signInButton.setProgress(0);
                    Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    _signInButton.setEnabled(true);
                }
            }
        });
    }

    private void toHomePageActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void toRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
