package com.raion.snapventure;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.raion.snapventure.Helper.InternetCheck;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class CameraViewActivity extends AppCompatActivity {

    CameraView cameraView;
    Button btnDetect,btnHint;
    SweetSheet mSweetSheet;
    RelativeLayout layout;
    private ProgressBar progressBar;
    private Dialog trueResultDialog,falseResultDialog;
    private String RIDDLE_EN, RIDDLE_ID, ANSWER;
    private TextToSpeech ttsEN,ttsID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("Main");
    TextView riddle_tv;


    private String en,id;


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }

    @Override
    public void onBackPressed() {
        if (mSweetSheet.isShow()){
            mSweetSheet.dismiss();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        cameraView = findViewById(R.id.camera_view);
        btnDetect = findViewById(R.id.button_detect);
        btnHint = findViewById(R.id.button_hint);
        layout = findViewById(R.id.cameraLayout);
        progressBar = findViewById(R.id.camera_progressBar);


        // Get Data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            RIDDLE_EN = extras.getString("RIDDLE_EN");
            RIDDLE_ID = extras.getString("RIDDLE_ID");
            ANSWER = extras.getString("ANSWER");
        }

        cameraView.setLifecycleOwner(this);
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // Pinch to zoom!
        cameraView.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER); // Tap to focus!
        cameraView.mapGesture(Gesture.LONG_TAP, GestureAction.CAPTURE); // Long tap to shoot!

        falseResultDialog = new Dialog(this);
        trueResultDialog = new Dialog(this);
        mSweetSheet = new SweetSheet(layout);

        setupFalseResultDialog();
        setupTextToSpeechEnglish();
        setupTextToSpeechIndonesia();
//        getStagesData();
        setupRiddles();

        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] jpeg) {
                CameraUtils.decodeBitmap(jpeg, 1000, 1000, new CameraUtils.BitmapCallback() {
                    @Override
                    public void onBitmapReady(Bitmap bitmap) {
                        runDetector(bitmap);
                        cameraView.stop();
                    }
                });
            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.start();
                cameraView.capturePicture();
            }
        });

        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                tts.speak(text,TextToSpeech.QUEUE_FLUSH,null);
                if (!mSweetSheet.isShow()){
                    mSweetSheet.show();
                    riddle_tv.setText(RIDDLE_EN);
                    ttsID.speak(RIDDLE_EN,TextToSpeech.QUEUE_FLUSH,null);
                }else {
                    ttsID.stop();
                    ttsEN.stop();
                    mSweetSheet.dismiss();
                }
            }
        });

    }

    private void runDetector(Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        final FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(boolean internet) {
                if (internet) {
                    FirebaseVisionCloudDetectorOptions options =
                            new FirebaseVisionCloudDetectorOptions.Builder()
                                    .setMaxResults(10)
                                    .build();
                    FirebaseVisionCloudLabelDetector detector =
                            FirebaseVision.getInstance().getVisionCloudLabelDetector(options);

                    detector.detectInImage(image)
                            .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionCloudLabel>>() {
                                @Override
                                public void onSuccess(List<FirebaseVisionCloudLabel> firebaseVisionCloudLabels) {
                                    processDataResultCloud(firebaseVisionCloudLabels);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ErrorOccured", e.getMessage());
                        }
                    });
                } else {
                    FirebaseVisionLabelDetectorOptions options =
                            new FirebaseVisionLabelDetectorOptions.Builder()
                                    .setConfidenceThreshold(0.5f)
                                    .build();
                    FirebaseVisionLabelDetector detector =
                            FirebaseVision.getInstance().getVisionLabelDetector(options);

                    detector.detectInImage(image)
                            .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
                                @Override
                                public void onSuccess(List<FirebaseVisionLabel> firebaseVisionLabels) {
                                    processDataResult(firebaseVisionLabels);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ErrorOccured", e.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void processDataResultCloud(List<FirebaseVisionCloudLabel> firebaseVisionCloudLabels) {
        for (FirebaseVisionCloudLabel label : firebaseVisionCloudLabels) {
//            Toast.makeText(this, "Cloud Result : " + label.getLabel(), Toast.LENGTH_SHORT).show();
            if (label.getLabel().equals("Laptop")){
                progressBar.setVisibility(View.GONE);
                setupTrueResultDialog("Laptop");
                trueResultDialog.show();
            }else{
                falseResultDialog.show();
            }
        }
    }

    private void processDataResult(List<FirebaseVisionLabel> firebaseVisionLabels) {
        for (FirebaseVisionLabel label : firebaseVisionLabels) {
//            Toast.makeText(this, "Device Result : " + label.getLabel(), Toast.LENGTH_SHORT).show();
            if (label.getLabel().equals("Laptop")){
                progressBar.setVisibility(View.VISIBLE);
                setupTrueResultDialog("Laptop");
                trueResultDialog.show();
            }else{
                falseResultDialog.show();
            }
        }

    }

    private void setupTrueResultDialog(String result){
        trueResultDialog.setContentView(R.layout.dialog_jawaban_benar);

        trueResultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        trueResultDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvResult = trueResultDialog.findViewById(R.id.dialogTrue_resultTv);
        TextView tvNext = trueResultDialog.findViewById(R.id.dialogTrue_nextTv);
        ImageView imgNext = trueResultDialog.findViewById(R.id.dialogTrue_nextImg);

        tvResult.setText(result);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trueResultDialog.dismiss();
            }
        });

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trueResultDialog.dismiss();
            }
        });
    }

    private void setupFalseResultDialog(){
        falseResultDialog.setContentView(R.layout.dialog_jawaban_salah);

        falseResultDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        falseResultDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void setupTextToSpeechEnglish(){
        ttsEN = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR){
                    ttsEN.setLanguage(new Locale("en","EN"));
                }
            }
        });
    }

    private void setupTextToSpeechIndonesia(){
        ttsID = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR){
                    ttsID.setLanguage(new Locale("id","ID"));
                }
            }
        });
    }

    private void setupRiddles(){
        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.DuangAnimation);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.camera_custom_sweet_sheet,null);
        riddle_tv = view.findViewById(R.id.hint_questionTv);
        customDelegate.setCustomView(view);
        mSweetSheet.setDelegate(customDelegate);


        Log.d("riddle_TV", riddle_tv.getText().toString());
        Log.d("riddle_ID", RIDDLE_ID);
        Log.d("riddle_EN", RIDDLE_EN);

        Switch hintSwicth = view.findViewById(R.id.hint_switch);
        hintSwicth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    riddle_tv.setText(RIDDLE_ID);
                    ttsEN.stop();
                    ttsID.speak(RIDDLE_ID,TextToSpeech.QUEUE_FLUSH,null);
                }else{
                    riddle_tv.setText(RIDDLE_EN);
                    ttsID.stop();
                    ttsEN.speak(RIDDLE_EN,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

    }

//    private void getStagesData(){
//        DocumentReference docRef = ref.document("roleplace").collection("room").document("stage-1");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()){
//                        en = document.getString("en");
//                        id = document.getString("id");
//
//                        Log.d("EN",en);
//                    }
//                }
//            }
//        });
//    }

}
