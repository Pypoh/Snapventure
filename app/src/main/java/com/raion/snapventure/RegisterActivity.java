package com.raion.snapventure;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raion.snapventure.Data.User;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText _inputEmail, _inputName, _inputPasswd, _inputConfPasswd;
    private ActionProcessButton _singUpBtn;

    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("User");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _inputEmail = findViewById(R.id.input_email_signup);
        _inputName = findViewById(R.id.input_name_signup);
        _inputPasswd = findViewById(R.id.input_password_signup);
        _inputConfPasswd = findViewById(R.id.input_confirm_password_signup);
        _singUpBtn = findViewById(R.id.btn_SignUp);

        mAuth = FirebaseAuth.getInstance();

        _singUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _singUpBtn.setEnabled(false);
                handleRegister();
            }
        });

    }

    private void handleRegister() {
        String email = _inputEmail.getText().toString();
        String name = _inputName.getText().toString();
        String password = _inputPasswd.getText().toString();
        String conf_password = _inputConfPasswd.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    inputDataToDatabase();
                } else {
                    Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    _singUpBtn.setEnabled(true);
                }
            }
        });
    }

    private void inputDataToDatabase() {
        String email = _inputEmail.getText().toString();
        String name = _inputName.getText().toString();

        User userData = new User(email, name);

        userRef.add(userData);
    }
}
