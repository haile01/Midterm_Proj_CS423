package com.example.midterm_proj;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository mImgRepo;
    private LiveData<List<Image>> mAllImages;

    public ImageViewModel (Application application) {
        super(application);
        mImgRepo = new ImageRepository(application);
        mAllImages = mImgRepo.getAllImages();
    }

    LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    //public voi delete() {mImgRepo.delete();}
}
