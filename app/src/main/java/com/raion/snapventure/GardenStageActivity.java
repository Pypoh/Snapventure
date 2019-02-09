package com.raion.snapventure;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.raion.snapventure.Data.DataStage;
import com.raion.snapventure.Data.DataUserStage;
import com.raion.snapventure.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class GardenStageActivity extends AppCompatActivity {
    private SweetSheet mSweetSheet;
    private RelativeLayout layout;
    private Button btnDetect;
    private RecyclerView rv_stageItem;
    private StageRVAdapter stageAdapter;
    private List<DataUserStage> mDataStage = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionRef = db.collection("User");

    DatabaseHelper db_sqlite;

    private String uid;
    private int energy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garden_stage);
        layout = findViewById(R.id.stagesLayout);
        btnDetect = findViewById(R.id.stages_button_detect);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        mSweetSheet = new SweetSheet(layout);


        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSweetSheet.show();
            }
        });

        db_sqlite = new DatabaseHelper(this);
//        mDataStage.addAll(db_sqlite.getAllData());

        // Setup RecyclerView Stage Item
        int numberOfColumns = 3;
        rv_stageItem = findViewById(R.id.rv_stage);
        rv_stageItem.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        stageAdapter = new StageRVAdapter(mDataStage, getApplicationContext());
        rv_stageItem.setAdapter(stageAdapter);
//        stageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRiddles();
        checkStages();
        mDataStage.clear();
        mDataStage.addAll(db_sqlite.getAllData());
        stageAdapter.notifyDataSetChanged();
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

        getUserData(view);

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
                        energy = Integer.parseInt(String.valueOf(document.get("energy")));
                        tvEnergy.setText(String.valueOf(energy));
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

    private void checkStages(){
        Log.d("stages_uid", uid);
        DocumentReference documentReference = collectionRef.document(uid);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int stages_unlocked = Integer.parseInt(String.valueOf(documentSnapshot.get("stages_unlocked")));
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
    }
}
