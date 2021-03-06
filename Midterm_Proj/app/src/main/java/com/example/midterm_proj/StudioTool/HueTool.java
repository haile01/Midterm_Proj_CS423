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

public class HueTool extends StudioTool {

    private static final int DEFAULT_VALUE = 0;
    private final HueTool.HueHandler mHueHandler;
    private int mValue = DEFAULT_VALUE;

    @Override
    public void cancel() {
        mValue = DEFAULT_VALUE;
        updateBitmap();
        super.cancel();
    }

    public interface HueHandler {
        void hueFilter(int value);
        Bitmap getBitmap();
    }

    public HueTool (StudioToolManager toolManager, HueTool.HueHandler HueHandler) {
        super(toolManager, "Hue", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.hue));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.hue_tool_options, null);
        mHueHandler = HueHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.hueValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(360);
        slider.setStepSize(1);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                if (mHueHandler.getBitmap() != null){
                    if (fromUser) {
                        mValue = Float.valueOf(value).intValue();
                        updateBitmap();
                    }
                }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.hueCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setValue(DEFAULT_VALUE);
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.hueCommit);
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
        mHueHandler.hueFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mHueHandler.getBitmap(), false);
    }
}
