package com.example.midterm_proj.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.midterm_proj.Image;
import com.example.midterm_proj.ImageViewModel;
import com.example.midterm_proj.R;

import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_label_1};
    private final Context mContext;
    private List<Image> mImageList;

    private Fragment mPhotosFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm, List<Image> imageList) {
        super(fm);
        mContext = context;
        mImageList = imageList;

        mPhotosFragment = PhotosFragment.newInstance(mImageList, mContext);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position) {
//            case 1: {
//                fragment = SearchFragment.newInstance();
//                break;
//            }
//            case 2:
//                fragment = LibraryFragment.newInstance();
//                break;
            default:
                ((PhotosFragment) mPhotosFragment).preInit(mImageList, mContext);
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
        // Show 2 total pages.
        return 1;
    }

    public void setImageList(List<Image> imageList) {
        mImageList = imageList;
        ((PhotosFragment) mPhotosFragment).preInit(mImageList, mContext);
    }
}