package com.example.midterm_proj.StudioTool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;

public class TextTool extends StudioTool {

    private TextHandler mTextHandler;
    private float dx, dy;
    private String mText;
    private Rect bounds;
    private final float boundLength = 100;

    public interface TextHandler {
        void handleText(String mText);
        Bitmap getBitmap();
    }

    public TextTool (StudioToolManager toolManager, TextHandler TextHandler) {
        super(toolManager, "Text", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.text));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.text_tool_options, null);
        mTextHandler = TextHandler;
        mText = "helllloooo";


        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        ImageButton cancelBtn = mToolOptions.findViewById(R.id.textCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.textCommit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateBitmap();
                commit();
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mTextHandler.handleText(mText);
        mChangeBitmapHandler.changeBitmap(mTextHandler.getBitmap(), false);
    }

    @Override
    public void drawCanvas(Canvas canvas, Matrix matrix) {
        super.drawCanvas(canvas, matrix);

        dx = mTextHandler.getBitmap().getWidth() / 2;
        dy = mTextHandler.getBitmap().getHeight() / 2;

        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels

        paint.setTextSize((int) (14 * 4));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        bounds = new Rect();
        paint.getTextBounds(mText, 0, mText.length(), bounds);
        float x = (int) (dx - bounds.width()/2);
        float y = (int) (dy + bounds.height()/2);
        Log.d("oooooooooo", x + " " + y );
        canvas.drawText(mText, x, y, paint); // x, y is center of bound

    }

    @Override
    public void beginDrag(float x, float y){
        super.beginDrag(x,y);
        debug(x,y);
       // dx = x;
       // dy = y;

    }

    @Override
    public void processDrag(float x, float y) {
        super.processDrag(x,y);
    }

    @Override
    public void endDrag(float x, float y) {
        super.endDrag(x,y);
    }
    private void debug (float x, float y) {
        if (checkInBound(x,y)){
            Log.d("YESSS", x + " " + y );
        }
        else {
            Log.d("1111", x + " " + y);
        }
    }

    private boolean checkInBound(float x, float y){

        return (x>= dx - bounds.width()/2 && x < dx+ bounds.width()/2 && y>=dy - bounds.height()/2 && y <dy+bounds.height()/2);
    }
}
