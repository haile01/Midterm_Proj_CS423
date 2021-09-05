package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.ui.main.ChangeBitmapHandler;

public class TextTool extends StudioTool {

    private TextHandler mTextHander;

    public interface TextHandler {
        void handleText();
        Bitmap getBitmap();
    }

    public TextTool (StudioToolManager toolManager, TextHandler TextHandler) {
        super(toolManager, "Text", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.text));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.text_tool_options, null);
        mTextHander = TextHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mTextHander.handleText();
        mChangeBitmapHandler.changeBitmap(mTextHander.getBitmap(), false);
    }
}
