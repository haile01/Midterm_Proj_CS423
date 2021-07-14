package com.example.midterm_proj.ui.main;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class SizeConfig {
    static int height;
    static int width;
    static int numOfImagesRow = 3;
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int getNumOfImagesRow(){
        return numOfImagesRow;
    }
    public static void init(int newHeight, int newWidth){
        height = newHeight;
        width = newWidth;
    }



}
