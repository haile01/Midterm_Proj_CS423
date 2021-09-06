package com.example.midterm_proj.FaceID;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.midterm_proj.ml.FacemobilenetFloat32;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class FaceIDManager {
    private ArrayList<FaceID> mFaceList;
    private float[][] clusterList;
    private Context mContext;

    public FaceIDManager (Context context) {
//        Read faceIDs & clusters in database
        mContext = context;
    }

    public void addFace (Face face, Bitmap bitmap) {
//        Run model to get FaceID
        float [] features = null;
        try {
            FacemobilenetFloat32 model = FacemobilenetFloat32.newInstance(mContext);
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 112, 112, 3}, DataType.FLOAT32);
            TensorImage tensorImage = new TensorImage(DataType.UINT8);
            tensorImage.load(bitmap);
            inputFeature0.loadBuffer(tensorImage.getBuffer());

            FacemobilenetFloat32.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            features = outputFeature0.getFloatArray();
        }
        catch (IOException e) {
            Log.e("FaceMobilenet", "Read model error");
            return;
        }
//        Writes face to database

        if (features == null)
            return;

//        DATABASE IS NOT AN INSTANCE'S ATTRIBUTE DUMB ASS :)


//        Find the most relevant cluster (or not, create new)
//        Assign face to that cluster
    }
}
