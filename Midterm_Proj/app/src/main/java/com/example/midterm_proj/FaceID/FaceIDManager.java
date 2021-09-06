package com.example.midterm_proj.FaceID;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.ml.FacemobilenetFloat32;
import com.google.mlkit.vision.face.Face;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FaceIDManager {
    private MutableLiveData<ArrayList<FaceID>> mFaceList;
    private MutableLiveData<ArrayList<FaceCluster>> mClusterList;
    private Context mContext;

    private static FaceIDManager instance;
    public static FaceIDManager getInstance (Context context, ContentResolver contentResolver) {
        if (instance == null)
            instance = new FaceIDManager(context, contentResolver);
        return instance;
    }

    public FaceIDManager (Context context, ContentResolver contentResolver) {
//        Read faceIDs & clusters in database
        mContext = context;
        mClusterList = new MutableLiveData<>();
        mFaceList = new MutableLiveData<>();
        FaceCluster.count = 0;
        FaceIDHelper.setContentResolver(contentResolver);
    }

    public void updateFaceId (ArrayList <Image> images) {
        new UpdateFaceAsyncTask(mContext, images).execute();
    }

    public MutableLiveData<ArrayList<FaceID>> getFaceList () {
        return mFaceList;
    }

    public MutableLiveData<ArrayList<FaceCluster>> getClusterList () {
        return mClusterList;
    }

    public static class UpdateFaceAsyncTask extends AsyncTask <Void, Void, Void> {
        private ArrayList<FaceID> currentFaceList;
        private ArrayList<FaceCluster> currentClusterList;
        @SuppressLint("StaticFieldLeak")
        private final Context mContext;
        private final ArrayList<Image> mImageList;
        private float THRESHOLD = 0.1f;

        public UpdateFaceAsyncTask (Context context, ArrayList<Image> imageList) {
            super();
            currentClusterList.clear();
            currentFaceList.clear();
            mImageList = imageList;
            mContext = context;
        }

        public void addFace (Image image, Face face, Bitmap bitmap) {
//        Run model to get FaceID
            float [] features;
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
            currentFaceList.add(new FaceID(image.getUri(), face, bitmap, features));

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
            FaceIDHelper.updateFaceId(this, mImageList);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            instance.mFaceList.postValue(currentFaceList);
            instance.mClusterList.postValue(currentClusterList);
        }
    }
}
