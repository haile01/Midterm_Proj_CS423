package com.example.midterm_proj.StudioTool;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.google.android.material.slider.RangeSlider;

import org.jetbrains.annotations.NotNull;

public class HueTool extends StudioTool {

    private HueTool.HueHandler mHueHander;
    private int mValue = 0;

    public interface HueHandler {
        void hueFilter(int value);
        Bitmap getBitmap();
    }

    public HueTool (StudioToolManager toolManager, HueTool.HueHandler HueHandler) {
        super(toolManager, "Hue", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.hue));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.hue_tool_options, null);
        mHueHander = HueHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        RangeSlider slider = mToolOptions.findViewById(R.id.hueValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
                if (mHueHander.getBitmap() != null){
                    if (fromUser) {
//                    Fucking lag :/
                        mValue = Float.valueOf(value).intValue();
                        updateBitmap();
                    }
                }
            }
        });
    }
    public void updateBitmap () {
//        Do sth, then
        mHueHander.hueFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mHueHander.getBitmap(), false);
    }
}
