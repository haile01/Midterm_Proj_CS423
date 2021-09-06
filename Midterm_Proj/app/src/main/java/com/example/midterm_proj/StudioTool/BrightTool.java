package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

public class BrightTool extends StudioTool {

    private BrightHandler mBrightHander;
    private final int DEFAULT_VALUE = 0;
    private int mValue = DEFAULT_VALUE; // 0 to 255

    @Override
    public void cancel() {
        mValue = DEFAULT_VALUE;
        updateBitmap();
        super.cancel();
    }

    public interface BrightHandler {
        void brightFilter(int value);
        Bitmap getBitmap();
    }

    public BrightTool (StudioToolManager toolManager, BrightHandler BrightHandler) {
        super(toolManager, "Bright", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.brighten));
        mBrightHander = BrightHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.bright_tool_options, null);
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.brightValueSlider);
        slider.setValueFrom(-255);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.setValue(DEFAULT_VALUE);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
            if (mBrightHander.getBitmap() != null){
                if (fromUser) {
                    mValue = Float.valueOf(value).intValue();
                    updateBitmap();
                }
            }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.brightCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setValue(DEFAULT_VALUE);
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.brightCommit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
                slider.setValue(DEFAULT_VALUE);
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mBrightHander.brightFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mBrightHander.getBitmap(), false);
    }
}