package com.nsk.cky.ckylibrary.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description:
 * Company    :
 * Author     : gene
 * Date       : 2018/8/26
 */
public class CkyPagerAdapter extends PagerAdapter {
    private List<View> mViewList;
private int childrenCount = 0;

    public CkyPagerAdapter(List<View> mViewList) {
        super();
        this.mViewList = mViewList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mViewList == null) ? 0 : mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        notifyDataSetChanged();
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(childrenCount>0){
            childrenCount--;
            return POSITION_UNCHANGED;
        }
       return  super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        childrenCount = getCount();
        super.notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        notifyDataSetChanged();
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }





}