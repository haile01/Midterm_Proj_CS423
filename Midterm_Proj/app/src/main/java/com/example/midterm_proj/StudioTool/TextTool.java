package com.example.midterm_proj.StudioTool;

import android.graphics.Bitmap;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;

public class TextTool extends StudioTool {

    private TextHandler mTextHandler;

    public interface TextHandler {
        void handleText();
        Bitmap getBitmap();
    }

    public TextTool (StudioToolManager toolManager, TextHandler TextHandler) {
        super(toolManager, "Text", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.text));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.text_tool_options, null);
        mTextHandler = TextHandler;
    }

    public void updateBitmap () {
//        Do sth, then
        mTextHandler.handleText();
        mChangeBitmapHandler.changeBitmap(mTextHandler.getBitmap(), false);
    }
}
