package com.example.midterm_proj.FaceID;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.midterm_proj.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaceIDHelper {
    static ContentResolver mContentResolver;
    static Context mContext;
    private static Float ROTATE_ANGLE = 90f;
    static MutableLiveData<ArrayList<FaceID>> mFaceList;
    static MutableLiveData<ArrayList<FaceCluster>> mClusterList;

    public static void updateFaceId (FaceIDManager manager, ArrayList<Image> images) {
        if (mContentResolver == null) {
            return;
        }

//        Face detection
        ArrayList<FaceID> res = new ArrayList<>();
        FaceDetector detector = FaceDetection.getClient();

        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            try {
                Bitmap imageBitmap = rotateBitmap(MediaStore.Images.Media.getBitmap(mContentResolver, image.getUri()), true);
                InputImage inputImage = InputImage.fromBitmap(imageBitmap, 0);
                int finalI = i;
//                task.addFace(image, null, Bitmap.createScaledBitmap(imageBitmap, 112, 112, false));
                detector.process(inputImage)
                        .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                Log.d("FaceIDHelper::updateFaceId", "done " + (finalI + 1) + "/" + images.size());
                                for(Face face: faces) {
                                    Rect boundingBox = face.getBoundingBox();
                                    Bitmap faceBitmap = Bitmap.createBitmap(
                                            imageBitmap,
                                            boundingBox.left,
                                            boundingBox.top,
                                            boundingBox.right - boundingBox.left,
                                            boundingBox.bottom - boundingBox.top);

                                    new FaceIDManager.AddFaceAsyncTask(
                                            image,
                                            face,
                                            Bitmap.createScaledBitmap(faceBitmap, 112, 112, false),
                                            mContext,
                                            mFaceList,
                                            mClusterList
                                    ).execute();
                                }
                            }
                        });
            }
            catch (IOException e) {
//                Do nothing
            }
        }

        Log.d("FaceIDHelper::updateFaceId", "WHY CANT THE FUCKING DETECTOR BE SYNCHRONOUS????");
    }

    public static Bitmap rotateBitmap(Bitmap source, boolean forward)
    {
        Float angle = forward ? ROTATE_ANGLE : -ROTATE_ANGLE;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

}
