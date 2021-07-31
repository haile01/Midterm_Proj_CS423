package com.example.midterm_proj;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.util.concurrent.Future;

public interface GetImageHandler {
    void imageFromCamera();

    void imageFromGallery();
}
