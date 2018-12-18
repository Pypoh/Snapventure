package com.raion.snapventure;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.UltraViewPagerAdapter;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;

public class MainActivity extends AppCompatActivity {

    private UltraViewPager ultraViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ultraViewPager = findViewById(R.id.menu_stage);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        //Initialize UltraPagerAdapter
        PagerAdapter adapter = new UltraPagerAdapter(true);
        ultraViewPager.setAdapter(adapter);
        ultraViewPager.setMultiScreen(0.6f);
        ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
        //ultraViewPager.setCurrentItem(2);




    }


}
