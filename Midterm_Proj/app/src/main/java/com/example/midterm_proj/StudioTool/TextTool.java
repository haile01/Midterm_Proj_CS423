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
import android.graphics.fonts.Font;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioTool.BrushToolHelper.ColorPicker;

import java.util.ArrayList;

public class TextTool extends StudioTool {

    private TextHandler mTextHandler;
    private float currentX, currentY;
    private String mText;
    private Rect bounds;
    private Color currentColor;
    private int currentSize;
    private Font currentFont;
    private ColorPicker mColorPicker;

    public interface TextHandler {
        void handleText(String mText);
        Bitmap getBitmap();
        void setImageBitmap(Bitmap imageBitmap);
    }

    public TextTool (StudioToolManager toolManager, TextHandler TextHandler) {
        super(toolManager, "Text", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.text));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.text_tool_options, null);
        mTextHandler = TextHandler;
        mText = "helllloooo";

        initializeToolOptionsUI(toolManager.mContext);
    }

    private void initializeToolOptionsUI(Context context) {
        mColorPicker = new ColorPicker(mToolOptions, context);
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
        TextView textView = mToolOptions.findViewById(R.id.textAdd);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);

        Button colorBtn = mToolOptions.findViewById(R.id.colorText);
        Button sizeBtn = mToolOptions.findViewById(R.id.sizeText);
        Button fontBtn = mToolOptions.findViewById(R.id.fontText);

        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("KKK", "");
                Bitmap bitmap = mTextHandler.getBitmap();
                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(mColorPicker.getCurrentColor());
                paint.setTextSize((int) (14 * 4));
                paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
                paint.setTextAlign(Paint.Align.CENTER);

                bounds = new Rect();
                paint.getTextBounds(mText, 0, mText.length(), bounds);
                canvas.drawText(mText, currentX, currentY  , paint);
                //mTextHandler.setImageBitmap(bitmap);;
                //commit();
            }
        });
        fontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(mColorPicker.getCurrentColor());
//        paint.setTextSize((int) (14 * 4));
//        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
//        paint.setTextAlign(Paint.Align.CENTER);
//
//        bounds = new Rect();
//        paint.getTextBounds(mText, 0, mText.length(), bounds);
//        canvas.drawText(mText, currentX, currentY  , paint);
    }

    @Override
    public void beginDrag(float x, float y){
        super.beginDrag(x,y);
        //debug(x,y);
        currentX = x;
        currentY = y;
    }

    @Override
    public void processDrag(float x, float y) {
        super.processDrag(x,y);
        float dx = x - currentX;
        float dy = y - currentY;

        currentX = x;
        currentY = y;

        bounds.left += dx;
        bounds.top += dy;

    }

    @Override
    public void endDrag(float x, float y) {
        super.endDrag(x,y);
    }

    private void debug (float x, float y) {
        Log.d("CHECK IN SPRITE ", check(x,y) ? "True" : "False");
    }

    private boolean check(float x, float y){
        return (x > bounds.left && x < bounds.left + bounds.width()
        && y > bounds.top && y < bounds.top + bounds.height());
    }
}