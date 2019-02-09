package com.raion.snapventure;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MoreInfoActivity extends AppCompatActivity {
    private String ANSWER,STAGE_ID;
    private Bitmap bitmap;

    private TextView tvAnswer,tvDesc;
    private ImageView image;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mRef = db.collection("Stages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);

        tvAnswer = findViewById(R.id.moreInfo_labelTv);
        tvDesc = findViewById(R.id.moreInfo_descTv);
        image = findViewById(R.id.moreInfo_img);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            STAGE_ID = extras.getString("STAGE_ID");
            ANSWER = extras.getString("ANSWER");
//            bitmap = extras.getParcelable("bitmap");

        }

        Log.d("bitmapisinya", bitmap.toString());
//        bitmap = (Bitmap) getIntent().getParcelableExtra("bitmap");

        image.setImageBitmap(bitmap);
        tvAnswer.setText(ANSWER);
        getMoreInfo(STAGE_ID);
    }

    private void getMoreInfo(String id){
        DocumentReference stageRef = mRef.document(String.valueOf(id));
        stageRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot data = task.getResult();
                    if (data.exists()){
                        String moreInfo = data.getString("more_info");
                        tvDesc.setText(moreInfo);
                    }
                }
            }
        });
    }
}
