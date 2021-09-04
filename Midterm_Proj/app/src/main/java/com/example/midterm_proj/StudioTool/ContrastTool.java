package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;
import com.google.android.material.slider.RangeSlider;

import org.jetbrains.annotations.NotNull;

public class ContrastTool extends StudioTool {

    private ContrastHandler mContrastHander;
    private int mValue = 0;
    TextView debug;

    public interface ContrastHandler {
        void contrastFilter(int value);
        Bitmap getBitmap();
    }

    public ContrastTool (StudioToolManager toolManager, ContrastHandler ContrastHandler) {
        super(toolManager.mInflater, toolManager.mToolOptionsView, "Contrast", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.contrast));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.contrast_tool_options, null);
        mContrastHander = ContrastHandler;
        debug = mToolOptions.findViewById(R.id.contrastValueDebug);
        initializeToolOptionsUI();

    }

    private void initializeToolOptionsUI() {
        RangeSlider slider = mToolOptions.findViewById(R.id.contrastValueSlider);
        slider.setValueFrom(0);
        slider.setValueTo(255);
        slider.setStepSize(1);
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull RangeSlider slider, float value, boolean fromUser) {
             if (mContrastHander.getBitmap()!= null){
                 if (fromUser) {
//                    Fucking lag :/
                     mValue = Float.valueOf(value).intValue();
                     debug.setText("" + value);
                     Log.d("CONTRAST", "" + value);
                     updateBitmap();
                 }
             }
            }
        });
    }


    public void updateBitmap () {
//        Do sth, then
        mContrastHander.contrastFilter(mValue);
        mChangeBitmapHandler.changeBitmap(mContrastHander.getBitmap(), false);
    }
}
