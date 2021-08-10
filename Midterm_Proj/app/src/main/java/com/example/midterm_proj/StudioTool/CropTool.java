package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;

public class CropTool extends StudioTool {

    public CropTool (LayoutInflater inflater, LinearLayout toolOptionsContainer, Context context) {
        super(inflater, toolOptionsContainer);
        if (mToolOptions == null) {
            mToolOptions = (LinearLayout) inflater.inflate(R.layout.crop_tool_options, null);
        }
        if (btnIcon == null) {
            btnIcon = context.getDrawable(R.drawable.ic_baseline_more_horiz);
        }
        btnText = "Crop";
    }
}
