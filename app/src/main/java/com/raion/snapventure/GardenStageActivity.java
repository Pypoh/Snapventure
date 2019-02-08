package com.raion.snapventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;

public class GardenStageActivity extends AppCompatActivity {
    private SweetSheet mSweetSheet;
    private RelativeLayout layout;
    private Button btnDetect;
    private ImageView imgBackBlack;
    private CardView stage1;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String KEY_STAGE = "garden";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_stage);
//        GridView gridview = (GridView) findViewById(R.id.gridview);
//        gridview.setAdapter(new ImageAdapter(this));
//
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(GardenStageActivity.this, "" + position, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(),CameraViewActivity.class));
//            }
//        });
        layout = findViewById(R.id.stagesLayout);
        btnDetect = findViewById(R.id.stages_button_detect);
        imgBackBlack = findViewById(R.id.stages_backBlackImg);
        stage1 = findViewById(R.id.card1);

        mSweetSheet = new SweetSheet(layout);
        setupRiddles();

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

        stage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CameraViewActivity.class));
            }
        });

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

//        getUserData(view);

        view.findViewById(R.id.homeSweet_homeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSweetSheet.isShow()){
                    imgBackBlack.setVisibility(View.GONE);
                    mSweetSheet.dismiss();
                }
            }
        });

<<<<<<< HEAD
        view.findViewById(R.id.homeSweet_logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signOut();
            }
        });
=======


>>>>>>> f72e14337fb81ebc7895e28c2caa6f40dfc5666b
    }
}
