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
    private static final int[] TAB_TITLES = new int[]{R.string.tab_label_1, R.string.tab_label_2, R.string.tab_label_3};
    private final Context mContext;
    private final List<Image> mImageList;

    public SectionsPagerAdapter(Context context, FragmentManager fm, List<Image> imageList) {
        super(fm);
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = SearchFragment.newInstance();
                break;
            case 2:
                fragment = LibraryFragment.newInstance();
                break;
            default: {
                fragment = PhotosFragment.newInstance(mImageList);
                ((PhotosFragment) fragment).setImageList(mImageList);
                break;
            }
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
        return 3;
    }
}