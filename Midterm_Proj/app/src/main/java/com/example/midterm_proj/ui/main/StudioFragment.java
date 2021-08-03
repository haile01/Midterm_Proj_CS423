package com.example.midterm_proj.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.midterm_proj.BitmapFilter;
import com.example.midterm_proj.ChangeTabHandler;
import com.example.midterm_proj.GetImageHandler;
import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioCanvasView;
import com.example.midterm_proj.StudioImageManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class StudioFragment extends Fragment implements StudioImageManager.OnChangeBitmapHandler, GetImageHandler {

    private ViewGroup mContainer;
    private ChangeTabHandler mChangeTabHander;
    private Bitmap mBitmap;
    private LayoutInflater mInflater;
    private ViewGroup mRootView;
    private StudioImageManager mStudioImageManager;
    private View mEmptyBitmapView;
    private LinearLayout mContentView;
    private StudioCanvasView mBitmapCanvasView;
    private Fragment mStudioFragment;
    private int PICK_IMAGE_CODE  = 1000;


    private File photoFile = null;
    private View mView;
    private Bitmap mImageBitmap;

    private Button textButton;
    private Button cropButton;
    private Button drawButton;
    private Button exposureButton;
    private Button contrastButton;
    private Button sharpenButton;
    private Button saturationButton;
    private Button brightButton;
    private Button hueButton;
    private Button blurButton;
    private Button tintButton;

    private BitmapFilter bitmapFilter;

    public StudioFragment () {
//        Empty constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_studio, container, false);
        mInflater = inflater;
        mContainer = container;
        mRootView = (ViewGroup) root;
        mContentView = mRootView.findViewById(R.id.studioContentContainer);
        initialize();
        return root;
    }

    private void initialize() {
        mEmptyBitmapView = mInflater.inflate(R.layout.empty_bitmap, mRootView, false);
        mBitmapCanvasView = new StudioCanvasView(mRootView.getContext());
        renderEmptyBitmap();
        attachCancelButton();
        attachGalleryButton();
        attachCameraButton();
        bitmapFilter = new BitmapFilter();
    }

    private void attachCancelButton () {
        Button cancelBtn = (Button) mRootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancel();
            }
        });
    }

    private void attachGalleryButton(){
        Button pickGallery = (Button) mRootView.findViewById(R.id.gallery_button);
        pickGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromGallery();
            }
        });
    }

    private void attachCameraButton(){
        Button pickCamera = (Button) mRootView.findViewById(R.id.camera_button);
        pickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromCamera();
            }
        });
    }

    private void testImage(){
        ImageView pickCamera = (ImageView) mRootView.findViewById(R.id.test_image);
        mImageBitmap = bitmapFilter.invert(mImageBitmap);
        pickCamera.setImageBitmap(mImageBitmap);
    }

    public void handleCancel () {
        mBitmapCanvasView.cancel();
        renderEmptyBitmap();
        mChangeTabHander.setTab(0);
    }

    private void renderEmptyBitmap () {
        mContentView.removeAllViews();
        mContentView.addView(mEmptyBitmapView);
    }
    
    private void renderBitmap () {
        mContentView.removeAllViews();
        mContentView.addView(mBitmapCanvasView);
    }

    public static StudioFragment newInstance() {
        StudioFragment fragment = new StudioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setChangeTabHandler(ChangeTabHandler handler) {
        mChangeTabHander = handler;
    }

    public void setStudioImageManager(StudioImageManager manager) {
        mStudioImageManager = manager;
        manager.setOnChangeBitmapHandler(this);
    }

    @Override
    public void changeBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mBitmapCanvasView.setBitmap(bitmap);
        renderBitmap();
    }

    private void takeImageFromCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(new String[] {
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            });
        }
        try {
            photoFile = createImageFile();
            Log.i("MidtermProj",photoFile.getAbsolutePath());
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.midterm_proj.captureimage.fileprovider",
                        photoFile);
                startCameraActivity.launch(photoURI);
            }
        } catch (Exception ex) {
            displayMessage(getContext(), ex.getMessage());
        }
    }

    private final ActivityResultLauncher<String[]> requestPermission = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
//                Do sth here
            }
    );

    private final ActivityResultLauncher<Uri> startCameraActivity = registerForActivityResult(
            new ActivityResultContract<Uri, Bitmap>() {
                @NonNull
                @Override
                public Intent createIntent(@NonNull Context context, Uri input) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, input);
                    return takePictureIntent;
                }
                @Override
                public Bitmap parseResult(int resultCode, @Nullable Intent intent) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        return bitmap;
                    }
                    catch (Exception ex) {
                        displayMessage(getContext(),"Request cancelled or something went wrong.");
                        return null;
                    }
                }
            },
            new ActivityResultCallback<Bitmap>() {
                @Override
                public void onActivityResult(Bitmap result) {
                    mImageBitmap = result;
                    testImage();
                }
            }
    );

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return image;
    }

    private void displayMessage(Context context, String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    private void takeImageFromGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent, PICK_IMAGE_CODE);
        startGalleryActivity.launch(null);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_CODE){
//            Uri uri = data.getData();
//            Bitmap bitmap = null;
//            try {
//                if (uri != null){
//                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
//                    mImageBitmap = bitmap;
//                    testImage();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private final ActivityResultLauncher<Uri> startGalleryActivity = registerForActivityResult(
            new ActivityResultContract<Uri, Bitmap>() {
                @NonNull
                @Override
                public Intent createIntent(@NonNull Context context, Uri input) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    return galleryIntent;
                }
                @Override
                public Bitmap parseResult(int resultCode, @Nullable Intent intent) {
                    Uri uri = intent.getData();
                    Bitmap bitmap = null;
                    try {
                        if (uri != null){
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                        }
                        return bitmap;
                    }
                    catch (Exception ex) {
                        displayMessage(getContext(),"Request cancelled or something went wrong.");
                        return null;
                    }
                }
            },
            new ActivityResultCallback<Bitmap>() {
                @Override
                public void onActivityResult(Bitmap result) {
                    mImageBitmap = result;
                    //testImage();
                }
            }
    );

    @Override
    public void imageFromCamera() {
        takeImageFromCamera();
    }

    @Override
    public void imageFromGallery() {
        takeImageFromGallery();
    }
}
