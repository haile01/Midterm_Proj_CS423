package com.example.midterm_proj;

import android.app.Application;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository mImgRepo;
    private LiveData<List<Image>> mAllImages;

    public ImageViewModel (Application application) {
        super(application);
        mImgRepo = new ImageRepository(application.getApplicationContext().getContentResolver());
        mAllImages = mImgRepo.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    public void deleteImage(Uri imageUri) {
        mImgRepo.deleteImage(imageUri);
    }
    public void saveImage(Bitmap image) {mImgRepo.saveImage(image);}
}
