package com.example.midterm_proj.StudioTool;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;

public class StudioTool {
    private LinearLayout mToolOptionsContainer;
    public LayoutInflater mInflater;
    public LinearLayout mToolOptions;
    public Drawable btnIcon;
    public String btnText;
    public ChangeBitmapHandler mChangeBitmapHandler;

    public StudioTool (LayoutInflater inflater, LinearLayout toolOptionsContainer, String text, Drawable icon) {
        mInflater = inflater;
        mToolOptionsContainer = toolOptionsContainer;
        btnText = text;
        btnIcon = icon;
    }

    public void renderToolOptions () {
        if (mToolOptionsContainer != null) {
            mToolOptionsContainer.removeAllViews();
            mToolOptionsContainer.addView(mToolOptions);
        }
    }

    public void choose() {
//        Do sth here
        renderToolOptions();
    }

    public View inflateButton() {
        View btnView = mInflater.inflate(R.layout.single_tool_button, null);
        Button btn = (Button) btnView.findViewById(R.id.toolBtn);
        btn.setText(btnText);

        btn.setCompoundDrawablesWithIntrinsicBounds(null, btnIcon, null, null);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose();
            }
        });
        return btnView;
    }
}
