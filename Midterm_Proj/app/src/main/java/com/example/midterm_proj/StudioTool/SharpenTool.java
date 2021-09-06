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

public class SharpenTool extends StudioTool {

    private static final int DEFAULT_VALUE = 0;
    private final SharpenHandler mSharpenHandler;
    private int mValue = DEFAULT_VALUE;

    @Override
    public void cancel() {
        mValue = DEFAULT_VALUE;
        updateBitmap();
        super.cancel();
    }

    public interface SharpenHandler {
        void sharpenFilter(int i);
        Bitmap getBitmap();
    }

    public SharpenTool (StudioToolManager toolManager, SharpenHandler SharpenHandler) {
        super(toolManager, "Sharpen", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.sharpen));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.sharpen_tool_options, null);
        mSharpenHandler = SharpenHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        Slider slider = mToolOptions.findViewById(R.id.sharpenValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                if (mSharpenHandler.getBitmap() != null){
                    if (fromUser) {
                        mValue = Float.valueOf(value).intValue();
                        updateBitmap();
                    }
                }
            }
        });

        ImageButton cancelBtn = mToolOptions.findViewById(R.id.sharpenCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slider.setValue(DEFAULT_VALUE);
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.sharpenCommit);
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
        mSharpenHandler.sharpenFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mSharpenHandler.getBitmap(), false);
    }
}
