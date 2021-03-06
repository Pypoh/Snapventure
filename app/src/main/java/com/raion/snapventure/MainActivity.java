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

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        if (mSweetSheet.isShow()){
            imgBackBlack.setVisibility(View.GONE);
            mSweetSheet.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    private void setupRiddles(){
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation);
        View view = LayoutInflater.from(this).inflate(R.layout.home_custom_sweet_sheet,null);
        customDelegate.setCustomView(view);
        mSweetSheet.setDelegate(customDelegate);

        getUserData(view);

        view.findViewById(R.id.homeSweet_homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSweetSheet.isShow()){
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

    private void getUserData(final View view){
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

    private void signOut(){
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
