package com.example.midterm_proj.StudioTool;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.midterm_proj.R;

public class CropTool extends StudioTool {

    private final CropHandler mCropHandler;
    private RectF mRect;
    private final float boundLength = 100;

    public interface CropHandler {
        void handleCrop(RectF mRect);
        Bitmap getBitmap();
    }

    public CropTool (StudioToolManager toolManager, CropHandler cropHandler) {
        super(toolManager, "Crop", AppCompatResources.getDrawable(toolManager.mContext, R.mipmap.crop));
        mToolOptions = (LinearLayout) mInflater.inflate(R.layout.crop_tool_options, null);
        mCropHandler = cropHandler;
        initializeToolOptionsUI();
    }

    private void initializeToolOptionsUI() {
        ImageButton cancelBtn = mToolOptions.findViewById(R.id.cropCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        ImageButton commitBtn = mToolOptions.findViewById(R.id.cropCommit);
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBitmap();
                commit();
            }
        });
    }

    public void updateBitmap () {
//        Do sth, then
        mCropHandler.handleCrop(mRect);
        mChangeBitmapHandler.changeBitmap(mCropHandler.getBitmap(), false);
    }

    @Override
    public void choose() {
        super.choose();
//        Get bitmap size
        mRect = new RectF(0, 0, mCropHandler.getBitmap().getWidth(), mCropHandler.getBitmap().getHeight());
    }

    @Override
    public void drawCanvas(Canvas canvas, Matrix matrix) {
        super.drawCanvas(canvas, matrix);
        Paint paint = new Paint();
        paint.setARGB(180, 255, 255, 255);
        paint.setStrokeWidth(0);
        RectF newRect = new RectF();
        matrix.mapRect(newRect, mRect);
//        bounding rect with grid
        for(int i = 0; i <= 3; i++) {
            float tmpY = newRect.top + newRect.height() / 3 * i;
            canvas.drawLine(newRect.left, tmpY, newRect.right, tmpY, paint);
        }
        for(int i = 0; i <= 3; i++) {
            float tmpX = newRect.left + newRect.width() / 3 * i;
            canvas.drawLine(tmpX, newRect.top, tmpX, newRect.bottom, paint);
        }
//        upper-left corner
        float strokeWidth = 10;
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine(newRect.left - strokeWidth / 2, newRect.top, newRect.left + boundLength, newRect.top, paint);
        canvas.drawLine(newRect.left, newRect.top - strokeWidth / 2, newRect.left, newRect.top + boundLength, paint);
//        upper-right corner
        canvas.drawLine(newRect.right + strokeWidth / 2, newRect.top, newRect.right - boundLength, newRect.top, paint);
        canvas.drawLine(newRect.right, newRect.top - strokeWidth / 2, newRect.right, newRect.top + boundLength, paint);
//        lower-left corner
        canvas.drawLine(newRect.left - strokeWidth / 2, newRect.bottom , newRect.left + boundLength, newRect.bottom, paint);
        canvas.drawLine(newRect.left, newRect.bottom + strokeWidth / 2, newRect.left, newRect.bottom - boundLength, paint);
//        lower-right corner
        canvas.drawLine(newRect.right + strokeWidth / 2, newRect.bottom, newRect.right - boundLength, newRect.bottom, paint);
        canvas.drawLine(newRect.right, newRect.bottom + strokeWidth / 2, newRect.right, newRect.bottom - boundLength, paint);
        /*
//        top-mid
        canvas.drawLine(newRect.left + newRect.width() / 2 - boundLength / 2, newRect.top, newRect.left + newRect.width() / 2 + boundLength / 2, newRect.top, paint);
//        bottom-mid
        canvas.drawLine(newRect.left + newRect.width() / 2 - boundLength / 2, newRect.bottom, newRect.left + newRect.width() / 2 + boundLength / 2, newRect.bottom, paint);
//        left-mid
        canvas.drawLine(newRect.left, newRect.top + newRect.height() / 2 - boundLength / 2, newRect.left, newRect.top + newRect.height() / 2 + boundLength / 2, paint);
//        right-mid
        canvas.drawLine(newRect.right, newRect.top + newRect.height() / 2 - boundLength / 2, newRect.right, newRect.top + newRect.height() / 2 + boundLength / 2, paint);
         */
    }

    private int cornerInd = -1;
    private float curX;
    private float curY;
    /*
    0---1
    |   |
    2---3
     */

    @Override
    public void beginDrag(float x, float y) {
        super.beginDrag(x, y);
//        Check which corner does it belong to
        if (y <= mRect.top + boundLength) {
            if (x <= mRect.left + boundLength)
                cornerInd = 0;
            if (x >= mRect.right - boundLength)
                cornerInd = 1;
        }
        if (y >= mRect.bottom - boundLength) {
            if (x <= mRect.left + boundLength)
                cornerInd = 2;
            if (x >= mRect.right - boundLength)
                cornerInd = 3;
        }
        curX = x;
        curY = y;
        debug();
    }

    @Override
    public void processDrag(float x, float y) {
        super.processDrag(x, y);
        float dX = x - curX;
        float dY = y - curY;
        curX = x;
        curY = y;
        switch (cornerInd) {
            case 0: {
                mRect.left += dX;
                mRect.top += dY;
                break;
            }
            case 1: {
                mRect.right += dX;
                mRect.top += dY;
                break;
            }
            case 2: {
                mRect.left += dX;
                mRect.bottom += dY;
                break;
            }
            case 3: {
                mRect.right += dX;
                mRect.bottom += dY;
                break;
            }
            default: {
//                Do nothing
            }
        }
        debug();
    }

    @Override
    public void endDrag(float x, float y) {
        super.endDrag(x, y);
        cornerInd = -1;
    }

    private void debug () {
        Log.d("cornerInd", "" + cornerInd);
        Log.d("Rect", mRect.left + " " + mRect.right + ", " + mRect.top + " " + mRect.bottom);
    }
}
