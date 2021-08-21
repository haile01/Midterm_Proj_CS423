package com.example.midterm_proj.ui.main;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.midterm_proj.BitmapFilter;
import com.example.midterm_proj.StudioTool.BrightTool;
import com.example.midterm_proj.StudioTool.BrushTool;
import com.example.midterm_proj.StudioTool.ContrastTool;
import com.example.midterm_proj.StudioTool.CropTool;
import com.example.midterm_proj.StudioTool.ExposureTool;
import com.example.midterm_proj.StudioTool.SaturationTool;
import com.example.midterm_proj.StudioTool.SharpenTool;
import com.example.midterm_proj.StudioTool.TextTool;

public class StudioFragmentViewModel extends ViewModel
implements BrightTool.BrightHandler,
        BrushTool.BrushHandler,
        ContrastTool.ContrastHandler,
        CropTool.CropHandler,
        ExposureTool.ExposureHandler,
        SaturationTool.SaturationHandler,
        SharpenTool.SharpenHandler,
        TextTool.TextHandler {
    // TODO: Implement the ViewModel
    private Bitmap bitmapToProcess = null;
    private Bitmap processedBitmap = null;

    public void exposureFilter(double value) {
        processedBitmap = BitmapFilter.exposure(bitmapToProcess, value);
    }

    public void contrastFilter(double value) {
        processedBitmap = BitmapFilter.contrast(bitmapToProcess, value);
    }

    public void handleText() {
    }

    public void handleCrop() {
    }

    public void handleBrush() {
    }

    public void sharpenFilter(int i) {
        processedBitmap = BitmapFilter.sharpen(bitmapToProcess);
    }

    public void saturationFilter(int value) {
        processedBitmap = BitmapFilter.saturation(bitmapToProcess, value);
    }

    public void brightFilter(int value) {
        processedBitmap = BitmapFilter.brightness(bitmapToProcess, value);
    }


    public void setImageBitmap(Bitmap mImageBitmap) {
//        if (bitmapToProcess != null) {
//            bitmapToProcess.recycle();
//            bitmapToProcess = null;
//        }
        bitmapToProcess = Bitmap.createBitmap(mImageBitmap);
        processedBitmap = Bitmap.createBitmap(mImageBitmap);
    }

    public Bitmap getBitmap() {
        return processedBitmap;
    }

//    public void drawTextToBitmap(String mText) {
//        try {
//            Bitmap bitmap = bitmapToProcess;
//            float scale = 4;
//            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
//            // set default bitmap config if none
//            if(bitmapConfig == null) {
//                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
//            }
//            // resource bitmaps are imutable,
//            // so we need to convert it to mutable one
//            bitmap = bitmap.copy(bitmapConfig, true);
//
//            Canvas canvas = new Canvas(bitmap);
//            // new antialised Paint
//            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            // text color - #3D3D3D
//            paint.setColor(Color.rgb(110,110, 110));
//            // text size in pixels
//            paint.setTextSize((int) (16 * scale));
//            // text shadow
//            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
//
//            // draw text to the Canvas center
//            Rect bounds = new Rect();
//            paint.getTextBounds(mText, 0, mText.length(), bounds);
//            int x = (bitmap.getWidth() - bounds.width())/ bounds.width();
//            int y = (bitmap.getHeight() - bounds.height())/ bounds.height();
//
//            canvas.drawText(mText, x , y , paint);
//
//            bitmapToProcess = bitmap;
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//
//    }
}