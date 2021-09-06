package com.example.midterm_proj.FaceID;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class FaceID {
    public Uri mImageUri;
    public Face mFace;
    public int clusterID;
    public float[] mVector;
    public Bitmap mBitmap;

    public FaceID (Uri imageUri, Face face, Bitmap bitmap, float[] vector) {
        mImageUri = imageUri;
        mFace = face;
        mVector = vector;
        mBitmap = bitmap;
    }
}
