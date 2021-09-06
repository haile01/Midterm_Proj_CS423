package com.example.midterm_proj.StudioTool;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioTool.BrushToolHelper.ColorPicker;

public class TextTool extends StudioTool {

    private TextHandler mTextHandler;
    private float currentX, currentY;
    private String mText;
    private Rect bounds = new Rect();
    private int currentColor;
    private int currentSize;
    private String currentFont;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ColorPicker mColorPicker;
    AlertDialog.Builder builder;
    private Typeface tf;

    public interface TextHandler {
        void handleText(String mText, Rect bounds, Paint paint);
        Bitmap getBitmap();
    }

    public TextTool (StudioToolManager toolManager, TextHandler TextHandler) {
        super(toolManager, "Text", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.text));
        mChangeBitmapHandler = toolManager.mChangeBitmapHandler;
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.text_tool_options, null);
        mTextHandler = TextHandler;
        mText = null;
        tf = Typeface.createFromAsset(toolManager.mContext.getAssets(), "fonts/IBMPlexSansArabic-Bold.ttf");
        initializeToolOptionsUI(toolManager.mContext);
    }

    private void initializeToolOptionsUI(Context context) {
        builder = new AlertDialog.Builder(context);

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

        setClickText();
        setClickColor();
        setClickSize();
        setClickFont(context);
    }

    void setClickText(){
        TextView textView = mToolOptions.findViewById(R.id.textAdd);
        textView.setBackgroundColor(Color.BLACK);
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout inflater = (LinearLayout) mInflater.inflate(R.layout.dialog, null);
                builder.setView(inflater)
                        // Add action buttons
                        .setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText editText = inflater.findViewById(R.id.textAdd);
                                mText = editText.getText().toString();
                                updateBitmap();

                            }
                        })
                        .setNegativeButton("cancer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });  //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();
                //commit();
            }
        });
    }

    void setClickColor(){
        Button colorBtn = mToolOptions.findViewById(R.id.colorText);
        colorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commit();
            }
        });
    }

    void setClickSize(){
        Button sizeBtn = mToolOptions.findViewById(R.id.sizeText);

        sizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commit();
            }
        });
    }
    String[] fonts_array = {"Montserrat", "Roboto Mono", "Kaisei Tokumin", "IBM plex sans arabic "};
    void setClickFont(Context context){
        Button fontBtn = mToolOptions.findViewById(R.id.fontText);
        fontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Pick font")
                        .setItems(fonts_array, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0 :
                                        currentFont = fonts_array[0];
                                        //tf = Typeface.createFromAsset(context.getAssets(), "/montserrat_black.ttf");
                                }
                            }
                        });
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.show();
                //commit();
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        if (mText != null){
            mTextHandler.handleText(mText, bounds, paint);
            mChangeBitmapHandler.changeBitmap(mTextHandler.getBitmap(), false);
        }

    }


    @SuppressLint("WrongConstant")
    @Override
    public void choose() {
        super.choose();
        if (mText != null ){
            bounds.offsetTo(mTextHandler.getBitmap().getWidth() / 2, mTextHandler.getBitmap().getHeight() / 2);

            Rect tmpBound = new Rect();
            paint.setTypeface(tf);
            paint.setColor(mColorPicker.getCurrentColor());
            paint.setTextSize(50);
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
//        paint.setTextAlign(Paint.Align.CENTER);
            paint.getTextBounds(mText, 0, mText.length(), tmpBound);

            bounds.offset(- tmpBound.width() / 2, - tmpBound.height() / 2);
            bounds.right = bounds.left + tmpBound.width();
            bounds.top = bounds.bottom - tmpBound.height();
        }
        updateBitmap();
    }

    @Override
    public void drawCanvas(Canvas canvas, Matrix matrix) {
        super.drawCanvas(canvas, matrix);
        if (mText != null){
            float mapped[] = {bounds.left - 10, bounds.top - 10, bounds.right + 10, bounds.bottom + 10};
            matrix.mapPoints(mapped);
            Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            borderPaint.setColor(Color.WHITE);
            borderPaint.setStrokeWidth(2);

            canvas.drawLine(mapped[0], mapped[1], mapped[0], mapped[3], borderPaint);
            canvas.drawLine(mapped[0], mapped[3], mapped[2], mapped[3], borderPaint);
            canvas.drawLine(mapped[2], mapped[3], mapped[2], mapped[1], borderPaint);
            canvas.drawLine(mapped[2], mapped[1], mapped[0], mapped[1], borderPaint);

            borderPaint.setStrokeWidth(3);
            borderPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(mapped[0], mapped[1], 5, borderPaint);
            canvas.drawCircle(mapped[2], mapped[1], 5, borderPaint);
            canvas.drawCircle(mapped[0], mapped[3], 5, borderPaint);
            canvas.drawCircle(mapped[2], mapped[3], 5, borderPaint);
        }
    }

    @Override
    public void beginDrag(float x, float y){
        super.beginDrag(x,y);
        debug(x,y);
        if (check(x, y)) {
            currentX = x;
            currentY = y;
        }
        else {
            currentX = -1;
            currentY = -1;
        }
    }

    @Override
    public void processDrag(float x, float y) {
        super.processDrag(x,y);
        if (currentX == -1 || currentY == -1)
            return;

        float dx = x - currentX;
        float dy = y - currentY;

        currentX = x;
        currentY = y;

        bounds.offset((int)dx, (int)dy);
        updateBitmap();
    }

    @Override
    public void endDrag(float x, float y) {
        super.endDrag(x,y);
        if (currentX == -1 || currentY == -1)
            return;


        float dx = x - currentX;
        float dy = y - currentY;

        currentX = x;
        currentY = y;

        bounds.offset((int)dx, (int)dy);
        updateBitmap();

        currentX = -1;
        currentY = -1;
    }

    private void debug (float x, float y) {
        Log.d("CHECK IN SPRITE ", check(x,y) ? "True" : "False");
    }

    private boolean check(float x, float y){
        int OFFSET = 20;
        return (bounds.left - OFFSET <= x && x <= bounds.right + OFFSET
        && bounds.top - OFFSET <= y && y <= bounds.bottom + OFFSET);
//        return bounds.contains((int)x, (int)y);
//        return (x > bounds.left && x < bounds.left + bounds.width()
//        && y > bounds.top && y < bounds.top + bounds.height());
    }
}