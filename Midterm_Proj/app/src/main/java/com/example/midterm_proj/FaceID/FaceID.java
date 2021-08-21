package com.example.midterm_proj.FaceID;

import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

public class FaceID {
    private String imageName;
    private Face mFace;
    private float[] mVector;

    public FaceID (Face face, float[] vector) {
        mFace = face;
        mVector = vector;
    }

    public FaceID (Face face, TensorBuffer buffer) {
        mFace = face;
        mVector = buffer.getFloatArray();
    }
}
