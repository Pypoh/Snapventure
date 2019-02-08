package com.raion.snapventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.raion.snapventure.Data.DataStage;
import com.raion.snapventure.Data.DataUserStage;
import com.raion.snapventure.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class GardenStageActivity extends AppCompatActivity {
    private SweetSheet mSweetSheet;
    private RelativeLayout layout;
    private Button btnDetect;
    private ImageView imgBackBlack;
    private CardView stage1;
    private RecyclerView rv_stageItem;
    private StageRVAdapter stageAdapter;
    private List<DataUserStage> mDataStage = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    DatabaseHelper db_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_stage);
        layout = findViewById(R.id.stagesLayout);
        btnDetect = findViewById(R.id.stages_button_detect);

        mSweetSheet = new SweetSheet(layout);
        setupRiddles();

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSweetSheet.show();
            }
        });

        db_sqlite = new DatabaseHelper(this);
        mDataStage.addAll(db_sqlite.getAllData());

        // Setup RecyclerView Stage Item
        int numberOfColumns = 3;
        rv_stageItem = findViewById(R.id.rv_stage);
        rv_stageItem.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        stageAdapter = new StageRVAdapter(mDataStage, this);
        rv_stageItem.setAdapter(stageAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mSweetSheet.isShow()){
//            imgBackBlack.setVisibility(View.GONE);
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
//                    imgBackBlack.setVisibility(View.GONE);
                    mSweetSheet.dismiss();
                }
            }
        });

        view.findViewById(R.id.homeSweet_logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                signOut();
            }
        });
    }
}
