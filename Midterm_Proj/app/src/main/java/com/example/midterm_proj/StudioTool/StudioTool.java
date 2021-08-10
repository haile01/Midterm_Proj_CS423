package com.example.midterm_proj.StudioTool;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.midterm_proj.R;

public class StudioTool {
    private LinearLayout mToolOptionsContainer;
    private LayoutInflater mInflater;
    public LinearLayout mToolOptions;
    public Drawable btnIcon;
    public String btnText;

    public StudioTool (LayoutInflater inflater, LinearLayout toolOptionsContainer) {
        mInflater = inflater;
        mToolOptionsContainer = toolOptionsContainer;
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
        return btnView;
    }
}
