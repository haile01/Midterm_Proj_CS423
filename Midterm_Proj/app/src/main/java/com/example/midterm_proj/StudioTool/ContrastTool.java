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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContrastTool extends StudioTool {

    private final ContrastHandler mContrastHandler;
    private final int DEFAULT_VALUE = 0;
    private int mValue = DEFAULT_VALUE;

    @Override
    public void cancel() {
        mValue = DEFAULT_VALUE;
        updateBitmap();
        super.cancel();
    }

    public interface ContrastHandler {
        void contrastFilter(int value);
        Bitmap getBitmap();
    }

    public ContrastTool (StudioToolManager toolManager, ContrastHandler ContrastHandler) {
        super(toolManager, "Contrast", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.contrast));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.contrast_tool_options, null);
        mContrastHandler = ContrastHandler;
        initializeToolOptionsUI();

    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.contrastValueSlider);
        slider.setValueFrom(-100);
        slider.setValueTo(100);
        slider.setStepSize(1);
        slider.setValue(DEFAULT_VALUE);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if (mContrastHandler.getBitmap()!= null){
                    if (fromUser) {
                        mValue = Float.valueOf(value).intValue();
                        updateBitmap();
                    }
                }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.contrastCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                slider.setValue(DEFAULT_VALUE);
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.contrastCommit);
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
        mContrastHandler.contrastFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mContrastHandler.getBitmap(), false);
    }
}
