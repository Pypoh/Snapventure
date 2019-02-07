package com.raion.snapventure;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mikeafc on 15/11/26.
 */
public class UltraPagerAdapter extends PagerAdapter {
    private boolean isMultiScr;

    public UltraPagerAdapter(boolean isMultiScr) {
        this.isMultiScr = isMultiScr;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.stages, null);
        //new LinearLayout(container.getContext());
        TextView textView = linearLayout.findViewById(R.id.stage_text);


        setBackgroundFromPosition(position,linearLayout,textView);
//        linearLayout.setBackgroundResource(R.drawable.garden);
        //Set Click
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(container.getContext(), "Page : " + position, Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        Intent toGarden = new Intent(container.getContext(), GardenStageActivity.class);
                        container.getContext().startActivity(toGarden);
                        break;
                    case 1:
                        Toast.makeText(container.getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        container.addView(linearLayout);
//        linearLayout.getLayoutParams().width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, container.getContext().getResources().getDisplayMetrics());
//        linearLayout.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, container.getContext().getResources().getDisplayMetrics());
        return linearLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
    }

    private void setBackgroundFromPosition(int position,LinearLayout linearLayout, TextView textView){
        switch (position){
            case 0:
                linearLayout.setBackgroundResource(R.drawable.room);
                textView.setText("Room");
                break;
            case 1:
                linearLayout.setBackgroundResource(R.drawable.garden);
                textView.setText("Garden");
                break;
        }
    }

}