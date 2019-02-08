package com.raion.snapventure;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.raion.snapventure.Auth.LoginActivity;
import com.raion.snapventure.Data.User;
import com.raion.snapventure.Helper.DatabaseHelper;
import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.UltraViewPagerAdapter;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;

public class MainActivity extends AppCompatActivity {

    private UltraViewPager ultraViewPager;
    private SweetSheet mSweetSheet;
    private RelativeLayout layout;
    private Button btnDetect;
    private ImageView imgBackBlack;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = db.collection("User");
    private DocumentReference documentReference;

    private int stages_unlocked;

    private String uid;
    private DatabaseHelper db_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_sqlite = new DatabaseHelper(this);

        mAuth = FirebaseAuth.getInstance();
        documentReference = collectionRef.document(mAuth.getCurrentUser().getUid());
        uid = mAuth.getCurrentUser().getUid();

        db_sqlite.insertDataStage(0, "Easy 1", "Room", 0, 0, "You'll carry me if you have more money to bring", "Kamu akan membawa aku apabila kamu mempunyai uang lebih untuk dibawa", "Wallet");
        db_sqlite.insertDataStage(1, "Easy 2", "Room", 0, 0, "They use me for writing on their paper with ink", "Mereka biasa menggunakanku untuk menulis dikertas dengan tinta", "Pen");
        db_sqlite.insertDataStage(2, "Easy 3", "Room", 0, 0, "I'm like an animal, but i'm used beside a keyboard", "Aku seperti binatang, tetapi aku digunakan disebelah papan ketik", "Mouse");
        db_sqlite.insertDataStage(3, "Medium 1", "Room", 0, 0, "Portable for typing with screen", "Mudah dibawa untuk mengetik dengan layar", "Laptop");
        db_sqlite.insertDataStage(4, "Medium 2", "Room", 0, 0, "To Unlock something", "Untuk membuka sesuatu", "Key");
        db_sqlite.insertDataStage(5, "Medium 3", "Room", 0, 0, null, null, null);
        db_sqlite.insertDataStage(6, "Hard 1", "Room", 0, 0, null, null, null);
        db_sqlite.insertDataStage(7, "Hard 2", "Room", 0, 0, null, null, null);


        Log.d("stages_uid", uid);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        stages_unlocked = Integer.parseInt(String.valueOf(documentSnapshot.get("stages_unlocked")));
                        Log.d("stages_unlocked", String.valueOf(stages_unlocked));
                        for (int i = 0; i <= stages_unlocked; i++) {
                            db_sqlite.setStageStatusByID(i, 1);
                            Log.d("setstagestatus", i + " : 1" );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("stages_error", e.toString());
            }
        });



        layout = findViewById(R.id.mainLayout);
        btnDetect = findViewById(R.id.button_detect);
        ultraViewPager = findViewById(R.id.menu_stage);
        imgBackBlack = findViewById(R.id.home_backBlackImg);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        //Initialize UltraPagerAdapter
        PagerAdapter adapter = new UltraPagerAdapter(true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.6f);
        ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
        //ultraViewPager.setCurrentItem(2);

        mSweetSheet = new SweetSheet(layout);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBackBlack.setVisibility(View.VISIBLE);
                mSweetSheet.show();
            }
        });

        imgBackBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBackBlack.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRiddles();
    }

    @Override
    public void onBackPressed() {
        if (mSweetSheet.isShow()) {
            imgBackBlack.setVisibility(View.GONE);
            mSweetSheet.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    private void setupRiddles() {
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation);
        View view = LayoutInflater.from(this).inflate(R.layout.home_custom_sweet_sheet, null);
        customDelegate.setCustomView(view);
        mSweetSheet.setDelegate(customDelegate);

        getUserData(view);

        view.findViewById(R.id.homeSweet_homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSweetSheet.isShow()) {
                    imgBackBlack.setVisibility(View.GONE);
                    mSweetSheet.dismiss();
                }
            }
        });

        view.findViewById(R.id.homeSweet_logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    private void getUserData(final View view) {
        final TextView tvName = view.findViewById(R.id.homeSweet_namaTv);
        final TextView tvEnergy = view.findViewById(R.id.homeSweet_energyTv);
        final TextView tvPoint = view.findViewById(R.id.homeSweet_coinTv);
        DocumentReference userRef = collectionRef.document(uid);
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Document", "DocumentSnapshot data: " + document.getData());
                        tvName.setText(document.getString("name"));
                        tvEnergy.setText(String.valueOf(document.get("energy")));
                        tvPoint.setText(String.valueOf(document.get("point")));
                    } else {
                        Log.d("Document", "No such document");
                    }
                } else {
                    Log.d("Exception", "get failed with ", task.getException());
                }
            }
        });
    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
