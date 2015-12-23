package com.sensu.android.zimaogou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用ViewPager的Adapter
 *
 * @author cg
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<? extends View> mListViews;

    private int mShowPageCount;

    /**
     * @param viewList mListViews
     */
    public ViewPagerAdapter(List<? extends View> viewList) {
        mListViews = viewList;
    }

    public void setData(List<? extends View> viewList) {
        mListViews = viewList;
       notifyDataSetChanged();
    }

    public List<? extends View> getData() {
        return mListViews;
    }

    public void setShowPageCount(int pageCount) {
        if (pageCount > mListViews.size()) {
            pageCount = mListViews.size();
        }
        mShowPageCount = pageCount;
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public int getCount() {
        return mShowPageCount;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
