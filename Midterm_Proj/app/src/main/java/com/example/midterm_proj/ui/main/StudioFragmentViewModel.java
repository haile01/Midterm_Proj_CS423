package com.example.midterm_proj.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.lifecycle.ViewModel;

import com.example.midterm_proj.BitmapFilter;
import com.example.midterm_proj.StudioTool.BrightTool;
import com.example.midterm_proj.StudioTool.BrushTool;
import com.example.midterm_proj.StudioTool.BrushToolHelper.Brush;
import com.example.midterm_proj.StudioTool.ContrastTool;
import com.example.midterm_proj.StudioTool.CropTool;
import com.example.midterm_proj.StudioTool.ExposureTool;
import com.example.midterm_proj.StudioTool.HueTool;
import com.example.midterm_proj.StudioTool.SaturationTool;
import com.example.midterm_proj.StudioTool.SharpenTool;
import com.example.midterm_proj.StudioTool.TextTool;

import java.util.LinkedList;

public class StudioFragmentViewModel extends ViewModel
implements BrightTool.BrightHandler,
        BrushTool.BrushHandler,
        ContrastTool.ContrastHandler,
        CropTool.CropHandler,
        ExposureTool.ExposureHandler,
        SaturationTool.SaturationHandler,
        SharpenTool.SharpenHandler,
        TextTool.TextHandler,
        HueTool.HueHandler
{
    // TODO: Implement the ViewModel
    private Bitmap bitmapToProcess = null;
    private Bitmap processedBitmap = null;

    public void exposureFilter(int value) {
        processedBitmap = BitmapFilter.exposure(bitmapToProcess, value);
    }

    public void contrastFilter(int value) {
        processedBitmap = BitmapFilter.contrast(bitmapToProcess, value);
    }

    public void handleText(String mText) {
        processedBitmap = BitmapFilter.text(bitmapToProcess, mText);
    }

    public void handleCrop(RectF rect) {
        processedBitmap = BitmapFilter.crop(bitmapToProcess, rect);
    }

    public void handleBrush(LinkedList<Brush> brushes) {
        Bitmap tmp = bitmapToProcess.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(tmp);
        for (Brush b: brushes) {
            b.draw(canvas);
        }
        processedBitmap = Bitmap.createBitmap(tmp);
        tmp.recycle();
    }

    public void sharpenFilter(int value) {
        processedBitmap = BitmapFilter.sharpen(bitmapToProcess, value);
    }

    public void saturationFilter(int value) {
        processedBitmap = BitmapFilter.saturation(bitmapToProcess, value);
    }

    public void brightFilter(int value) {
        processedBitmap = BitmapFilter.brightness(bitmapToProcess, value);
    }

    public void hueFilter(int value){
        processedBitmap = BitmapFilter.hue(bitmapToProcess, value);
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        bitmapToProcess = Bitmap.createBitmap(imageBitmap);
        processedBitmap = Bitmap.createBitmap(imageBitmap);
    }

    public Bitmap getBitmap() {
        return processedBitmap;
    }

    public void commit() {
        bitmapToProcess = Bitmap.createBitmap(processedBitmap);
    }
}