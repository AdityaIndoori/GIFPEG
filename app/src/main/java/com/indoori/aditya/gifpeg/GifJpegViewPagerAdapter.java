package com.indoori.aditya.gifpeg;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by aditya on 03-06-2017.
 */

public class GifJpegViewPagerAdapter extends FragmentStatePagerAdapter implements D_JpegFragmentViewPager.OnFragmentInteractionListener,E_GifFragmentViewPager.OnFragmentInteractionListener{
    private int mNumOfTabs;
    String Category;
    GifJpegViewPagerAdapter(FragmentManager fm, int mNumOfTabs, String Category) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
        this.Category = Category;
        Log.v("Category","" + Category);
    }

    @Override
    public Fragment getItem(int position) {
        return onFragmentInteraction(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
            return "JPEGs";
        else if (position==1)
            return "GIFs";
        return null;
    }

    @Override
    public Fragment onFragmentInteraction(int pageNumber) {
        switch (pageNumber){
            case 0:
                D_JpegFragmentViewPager jpegFragmentViewPage = new D_JpegFragmentViewPager().newInstance("JPG", Category);
                return jpegFragmentViewPage;
            case 1:
                E_GifFragmentViewPager EGifFragmentViewPager = new E_GifFragmentViewPager().newInstance("GIF", Category);
                return EGifFragmentViewPager;
            default:
                return null;
        }
    }

}
