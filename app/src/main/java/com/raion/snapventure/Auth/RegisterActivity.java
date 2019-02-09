package com.raion.snapventure.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.raion.snapventure.Data.User;
import com.raion.snapventure.MainActivity;
import com.raion.snapventure.R;

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
        _singUpBtn.setProgress(1);
        String email = _inputEmail.getText().toString();
        String name = _inputName.getText().toString();
        String password = _inputPasswd.getText().toString();
        String conf_password = _inputConfPasswd.getText().toString();
        
        if(email.isEmpty() || name.isEmpty() || password.isEmpty() || conf_password.isEmpty()){
            Toast.makeText(this, "Fill in the blank column!", Toast.LENGTH_SHORT).show();
        }else{
            if (conf_password.equals(password)){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getUid();
                            inputDataToDatabase(uid);
                        } else {
                            _singUpBtn.setProgress(0);
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            _singUpBtn.setEnabled(true);
                        }
                    }
                });
            }else{
                _singUpBtn.setProgress(0);
                Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void inputDataToDatabase(String uid) {
        String email = _inputEmail.getText().toString();
        String name = _inputName.getText().toString();

        User userData = new User(email, name, 5, 0, 0);

        userRef.document(uid).set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                _singUpBtn.setProgress(0);
                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                toHomePageActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                _singUpBtn.setProgress(0);
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                _singUpBtn.setEnabled(true);
            }
        });
    }

    private void toHomePageActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
