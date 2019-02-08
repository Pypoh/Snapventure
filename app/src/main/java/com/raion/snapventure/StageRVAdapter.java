package com.raion.snapventure;

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

import com.raion.snapventure.Data.DataStage;
import com.raion.snapventure.Data.DataUserStage;

import java.util.List;

public class StageRVAdapter extends RecyclerView.Adapter<StageRVAdapter.ViewHolder> {

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
                Toast.makeText(mContext, "Maaf dek belum dibuka", Toast.LENGTH_SHORT).show();
            }
        });

        holder._itemName.setText(data.getDifficulty());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCamera = new Intent(mContext, CameraViewActivity.class);
                toCamera.putExtra("STAGE_ID", data.getId());
                toCamera.putExtra("RIDDLE_EN", data.getRiddleEn());
                toCamera.putExtra("RIDDLE_ID", data.getRiddleId());
                toCamera.putExtra("ANSWER", data.getAnswer());
                mContext.startActivity(toCamera);
            }
        });
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

}
