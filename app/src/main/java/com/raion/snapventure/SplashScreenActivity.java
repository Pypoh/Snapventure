package com.raion.snapventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.raion.snapventure.Auth.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mAuth = FirebaseAuth.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (mAuth.getCurrentUser() != null){
                    intent = new Intent(getApplicationContext(),MainActivity.class);
                }else {
                    intent = new Intent(getApplicationContext(),LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
