package com.example.midterm_proj.FaceID;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.provider.MediaStore;

import com.example.midterm_proj.BitmapFilter;
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
    private static final int MAX_FACES = 3;
    static ContentResolver mContentResolver;

    static void setContentResolver (ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    static void updateFaceId (FaceIDManager.UpdateFaceAsyncTask task, ArrayList <Image> images) {
        if (mContentResolver == null) {
            return;
        }

//        Face detection
        ArrayList<FaceID> res = new ArrayList<>();
        FaceDetector detector = FaceDetection.getClient();

        for (Image i: images) {
            try {
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(mContentResolver, i.getUri());
                InputImage image = InputImage.fromBitmap(imageBitmap, 0);
                detector.process(image)
                        .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                for(Face face: faces) {
                                    Rect boundingBox = face.getBoundingBox();
                                    Bitmap faceBitmap = Bitmap.createBitmap(
                                            imageBitmap,
                                            boundingBox.left,
                                            boundingBox.top,
                                            boundingBox.right - boundingBox.left,
                                            boundingBox.bottom - boundingBox.top);

                                    task.addFace(i, face, Bitmap.createScaledBitmap(faceBitmap, 112, 112, false));
                                }
                            }
                        });
            }
            catch (IOException e) {
//                Do nothing
            }
        }
    }
}
