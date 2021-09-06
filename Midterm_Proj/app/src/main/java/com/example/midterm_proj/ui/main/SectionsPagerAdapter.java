package com.example.midterm_proj.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.midterm_proj.ChangeTabHandler;
import com.example.midterm_proj.Image;
import com.example.midterm_proj.ImageViewModel;
import com.example.midterm_proj.MainActivity;
import com.example.midterm_proj.OpenPopupHandler;
import com.example.midterm_proj.R;
import com.example.midterm_proj.StudioImageManager;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_label_1, R.string.tab_label_2, R.string.tab_label_3};
    private final Context mContext;
    private List<Image> mImageList;

    private Fragment mPhotosFragment;
    private Fragment mStudioFragment;
    private Fragment mFaceAlbumFragment;

    private OpenPopupHandler mOpenPopupHandler;
    private ChangeTabHandler mChangeTabHandler;
    private StudioImageManager mStudioImageManager;

    public SectionsPagerAdapter(Context context, FragmentManager fm, List<Image> imageList) {
        super(fm);
        mContext = context;
        mImageList = imageList;

        mPhotosFragment = PhotosFragment.newInstance();
        ((PhotosFragment) mPhotosFragment).preInit(mImageList);

        mStudioFragment = StudioFragment.newInstance();

        mFaceAlbumFragment = FaceAlbumFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = mPhotosFragment;
                break;
            case 1:
                fragment = mFaceAlbumFragment;
                break;
            case 2:
                fragment = mStudioFragment;
                break;
            default:
                fragment = mPhotosFragment;
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    public void setImageList(List<Image> imageList) {
        mImageList = imageList;
        ((PhotosFragment) mPhotosFragment).preInit(mImageList);
        ((FaceAlbumFragment) mFaceAlbumFragment).setImageList(mImageList);
    }

    public void setOpenPopupHandler(OpenPopupHandler handler) {
        mOpenPopupHandler = handler;
        ((PhotosFragment) mPhotosFragment).setOpenPopupHandler(handler);
    }

    public void setChangeTabHandler(ChangeTabHandler handler) {
        mChangeTabHandler = handler;
        ((PhotosFragment) mPhotosFragment).setChangeTabHandler(mChangeTabHandler);
        ((StudioFragment) mStudioFragment).setChangeTabHandler(mChangeTabHandler);
    }

    public void setStudioImageManager(StudioImageManager manager) {
        ((StudioFragment) mStudioFragment).setStudioImageManager(manager);
    }
}