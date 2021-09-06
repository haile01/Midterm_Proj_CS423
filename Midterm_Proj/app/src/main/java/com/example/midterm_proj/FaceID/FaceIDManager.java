package com.example.midterm_proj.FaceID;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.ml.FacemobilenetFloat32;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FaceIDManager {
    private static final float THRESHOLD = 0.1f;
    private MutableLiveData<ArrayList<FaceID>> mFaceList;
    private MutableLiveData<ArrayList<FaceCluster>> mClusterList;
    private Context mContext;

    private static FaceIDManager instance;
    private ArrayList<FaceID> currentFaceList;
    private ArrayList<FaceCluster> currentClusterList;

    public static FaceIDManager getInstance (Context context, ContentResolver contentResolver) {
        if (instance == null)
            instance = new FaceIDManager(context, contentResolver);
        return instance;
    }

    public FaceIDManager (Context context, ContentResolver contentResolver) {
//        Read faceIDs & clusters in database
        mContext = context;
        mClusterList = new MutableLiveData<>(new ArrayList<>());
        mFaceList = new MutableLiveData<>(new ArrayList<>());
        FaceCluster.count = 0;
        FaceIDHelper.mContentResolver = contentResolver;
        FaceIDHelper.mContext = context;
        FaceIDHelper.mClusterList = mClusterList;
        FaceIDHelper.mFaceList = mFaceList;
    }

    public void updateFaceId (ArrayList <Image> images) {
        FaceIDHelper.updateFaceId(this, images);
        Log.d("FaceIDManager::updateFaceId", "DONE????");
    }

    public MutableLiveData<ArrayList<FaceID>> getFaceList () {
        return mFaceList;
    }

    public MutableLiveData<ArrayList<FaceCluster>> getClusterList () {
        return mClusterList;
    }

    public static class AddFaceAsyncTask extends AsyncTask<Void, Void, Void> {

        private final Context mContext;
        Image image;
        Face face;
        Bitmap bitmap;
        ArrayList<FaceID> currentFaceList;
        ArrayList<FaceCluster> currentClusterList;
        private MutableLiveData<ArrayList<FaceID>> mFaceList;
        private MutableLiveData<ArrayList<FaceCluster>> mClusterList;

        public AddFaceAsyncTask (Image image, Face face, Bitmap bitmap, Context context, MutableLiveData<ArrayList<FaceID>> mFaceList, MutableLiveData<ArrayList<FaceCluster>> mClusterList) {
            this.mFaceList = mFaceList;
            this.mClusterList = mClusterList;
            currentFaceList = mFaceList.getValue();
            currentClusterList = mClusterList.getValue();
            this.image = image;
            this.face = face;
            this.bitmap = bitmap;
            mContext = context;
        }

        public void addFace () {
            Log.d("FaceIDManager::addFace", "adding face...");
//        Run model to get FaceID
            float [] features = new float[0];
            try {
                FacemobilenetFloat32 model = FacemobilenetFloat32.newInstance(mContext);
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 112, 112, 3}, DataType.FLOAT32);
                TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
                tensorImage.load(bitmap);
                inputFeature0.loadBuffer(tensorImage.getBuffer());

                FacemobilenetFloat32.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                features = outputFeature0.getFloatArray();
            }
            catch (IOException e) {
                Log.e("FaceMobilenet", "Read model error");
            }
//        Writes face to database

            if (features.length == 0)
                return;

            Log.d("FaceIDManager::addFace", features.toString());

//        DATABASE IS NOT AN INSTANCE'S ATTRIBUTE DUMB ASS :)
            currentFaceList.add(new FaceID(image.getUri(), face, FaceIDHelper.rotateBitmap(bitmap, false), features));
            Log.d("FaceIDManager::addFace", "currentFaceList.size() = " + currentFaceList.size());

//        Find the most relevant cluster (or not, create new)
            FaceCluster relevant = null;
            for (FaceCluster c : currentClusterList) {
                if (c.distance(features) <= THRESHOLD) {
                    relevant = c;
                    break;
                }
            }

//       Assign face to that cluster
            if (relevant == null) {
                relevant = new FaceCluster(features);
                currentClusterList.add(relevant);
            }

            relevant.addFace(currentFaceList.size() - 1);
            currentFaceList.get(currentFaceList.size() - 1).clusterID = relevant.getID();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            addFace();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            mFaceList.postValue(currentFaceList);
            mClusterList.postValue(currentClusterList);
        }
    }
}
