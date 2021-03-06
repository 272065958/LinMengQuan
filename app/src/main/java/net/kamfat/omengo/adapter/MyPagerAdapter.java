package net.kamfat.omengo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cjx on 2016/7/12.
 */
public class MyPagerAdapter extends PagerAdapter {
    View[] views;
    String[] titles;
    public MyPagerAdapter(View[] views, String[] titles) {
        this.views = views;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return views.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views[position];
        if(view.getParent() != null){
            container.removeView(view);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}