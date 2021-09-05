package com.example.midterm_proj.StudioTool;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

public class SaturationTool extends StudioTool {

    private static final int DEFAULT_VALUE = 0;
    private final SaturationHandler mSaturationHandler;
    private int mValue = DEFAULT_VALUE;

    public interface SaturationHandler {
        void saturationFilter(int value);
        Bitmap getBitmap();
    }

    public SaturationTool (StudioToolManager toolManager, SaturationHandler SaturationHandler) {
        super(toolManager, "Saturation", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.saturation));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.saturation_tool_options, null);
        mSaturationHandler = SaturationHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.saturationValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                if (mSaturationHandler.getBitmap() != null){
                    if (fromUser) {
                        mValue = Float.valueOf(value).intValue();
                        updateBitmap();
                    }
                }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.saturationCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValue = DEFAULT_VALUE;
                updateBitmap();
                slider.setValue(DEFAULT_VALUE);
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.saturationCommit);
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
        mSaturationHandler.saturationFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mSaturationHandler.getBitmap(), false);
    }
}
