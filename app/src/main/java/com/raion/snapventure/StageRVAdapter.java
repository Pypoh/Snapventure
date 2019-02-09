package com.raion.snapventure;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.raion.snapventure.Data.DataStage;
import com.raion.snapventure.Data.DataUserStage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StageRVAdapter extends RecyclerView.Adapter<StageRVAdapter.ViewHolder> {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String uid = mAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference refUser = db.collection("User");

    private List<DataUserStage> DataUserStage;
    private Context mContext;
    ItemClickListener mClickListener;

    public StageRVAdapter(List<DataUserStage> DataUserStage, Context mContext) {
        this.DataUserStage = DataUserStage;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stage_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataUserStage data = DataUserStage.get(position);

        if (data.isStatus()) {
            holder._disableView.setVisibility(View.GONE);
        } else {
            holder._disableView.setVisibility(View.VISIBLE);
        }
        Log.d("statusdata", String.valueOf(data.isStatus()));
        holder._disableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Locked", Toast.LENGTH_SHORT).show();
            }
        });

        holder._itemName.setText(data.getDifficulty());
        checkEnergy(holder, data);
    }

    @Override
    public int getItemCount() {
        return DataUserStage.size();
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView _itemName;
        private ImageView _disableView;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);

            _itemName = v.findViewById(R.id.item_level_name);
            mCardView = v.findViewById(R.id.card_item);
            _disableView = v.findViewById(R.id.disable_view);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());

        }
    }

    private void updateEnergy() {
        final DocumentReference documentReference = refUser.document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int stages = Integer.parseInt(String.valueOf(documentSnapshot.get("energy")));
                Map<String, Object> stage = new HashMap<>();
                stage.put("energy", stages - 1);
                documentReference.set(stage, SetOptions.merge());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void checkEnergy(final ViewHolder holder, final DataUserStage data) {
        final DocumentReference documentReference = refUser.document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final int energy = Integer.parseInt(String.valueOf(documentSnapshot.get("energy")));

                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Energy", String.valueOf(energy));
                        if (energy != 0) {
                            Intent toCamera = new Intent(mContext, CameraViewActivity.class);
                            toCamera.putExtra("STAGE_ID", data.getId());
                            toCamera.putExtra("RIDDLE_EN", data.getRiddleEn());
                            toCamera.putExtra("RIDDLE_ID", data.getRiddleId());
                            toCamera.putExtra("ANSWER", data.getAnswer());
                            toCamera.putExtra("STAGE_ID", String.valueOf(data.getId()));
                            toCamera.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            updateEnergy();
                            mContext.startActivity(toCamera);
//                            ((Activity) mContext).finish();
                        } else {
                            Toast.makeText(mContext, "Insuffient energy", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
