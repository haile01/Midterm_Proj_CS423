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

public class ExposureTool extends StudioTool {

    private static final int DEFAULT_VALUE = 0;
    private final ExposureHandler mExposureHandler;
    private int mValue = DEFAULT_VALUE;

    public interface ExposureHandler {
        void exposureFilter(int value);
        Bitmap getBitmap();
    }

    public ExposureTool (StudioToolManager toolManager, ExposureHandler ExposureHandler) {
        super(toolManager, "Exposure", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.exposure));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.exposure_tool_options, null);
        mExposureHandler = ExposureHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.exposureValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
               if (mExposureHandler.getBitmap()!= null){
                   if (fromUser) {
//                    Fucking lag :/
                       mValue = Float.valueOf(value).intValue();
                       updateBitmap();
                   }
               }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.exposureCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValue = DEFAULT_VALUE;
                updateBitmap();
                slider.setValue(DEFAULT_VALUE);
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.exposureCommit);
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
        mExposureHandler.exposureFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mExposureHandler.getBitmap(), false);
    }
}
