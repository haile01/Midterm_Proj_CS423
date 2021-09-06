package com.example.midterm_proj.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.midterm_proj.ChangeTabHandler;
import com.example.midterm_proj.GetImageHandler;
import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioCanvasView;
import com.example.midterm_proj.StudioImageManager;
import com.example.midterm_proj.StudioTool.StudioToolManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudioFragment extends Fragment implements GetImageHandler, ChangeBitmapHandler {

    private ViewGroup mContainer;
    private ChangeTabHandler mChangeTabHandler;
    private Bitmap mBitmap;
    private LayoutInflater mInflater;
    private ViewGroup mRootView;
    private StudioImageManager mStudioImageManager;
    private View mEmptyBitmapView;
    private LinearLayout mContentView;
    private StudioCanvasView mBitmapCanvasView;
    private int PICK_IMAGE_CODE = 1000;
    private StudioToolManager mStudioToolManager;


    private File photoFile = null;
    private View mView;

    private StudioFragmentViewModel mViewModel;

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
        mViewModel = new ViewModelProvider(this).get(StudioFragmentViewModel.class);
        mStudioToolManager = new StudioToolManager(mInflater,
                mRootView.findViewById(R.id.studioContentContainer),
                mRootView.findViewById(R.id.studioToolbarContainer),
                mRootView.findViewById(R.id.studioToolOptionsContainer),
                getContext(),
                this,
                mViewModel
        );
        mBitmapCanvasView.setStudioToolManager(mStudioToolManager);
        renderEmptyBitmap();
        attachCancelButton();
        attachGalleryButton();
        attachCameraButton();
    }

    private void attachCancelButton () {
        Button cancelBtn = mRootView.findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancel();
            }
        });
    }

    private void attachGalleryButton(){
        Button pickGallery = mRootView.findViewById(R.id.gallery_button);
        pickGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromGallery();
            }
        });
    }

    private void attachCameraButton(){
        Button pickCamera = mRootView.findViewById(R.id.camera_button);
        pickCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFromCamera();
            }
        });
    }

    public void handleCancel () {
        mBitmapCanvasView.cancel();
        renderEmptyBitmap();
        mStudioToolManager.cancel();
        mChangeTabHandler.setTab(0);
    }

    private void renderEmptyBitmap () {
        mStudioToolManager.hideTools();
        mContentView.removeAllViews();
        mContentView.addView(mEmptyBitmapView);
    }
    
    private void renderBitmap () {
        mStudioToolManager.showTools();
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
        mChangeTabHandler = handler;
    }

    public void setStudioImageManager(StudioImageManager manager) {
        mStudioImageManager = manager;
        manager.setChangeBitmapHandler(this);
    }

    public void changeBitmap(Bitmap bitmap, boolean reset) {
        mBitmapCanvasView.updateBitmap(bitmap);
        if (reset) {
            mBitmap = bitmap;
            mViewModel.setImageBitmap(bitmap);
            renderBitmap();
        }
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
                    if (result != null) {
                        changeBitmap(result, true);
                    }
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
        startGalleryActivity.launch(null);
    }

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
                    if (result != null) {
                        changeBitmap(result, true);
                    }
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
