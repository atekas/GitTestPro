package com.sensu.android.zimaogou.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2016/1/25.
 */
public class GuidePageActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View> mViewList = new ArrayList<View>();

    private View[] mViews;

    int[] drawables = {
            R.drawable.welcome_01,
            R.drawable.welcome_02,
            R.drawable.welcome_03,
            R.drawable.welcome_04,
    };

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guide_page_activity);

        mSharedPreferences = getSharedPreferences("zimaogou_preferences", Activity.MODE_PRIVATE);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOnPageChangeListener(this);

        mViews = new View[drawables.length];

        for (int i = 0; i < mViews.length; i++) {
            mViews[i] = ((LinearLayout) findViewById(R.id.point_layout)).getChildAt(i);
        }

        initPager();
    }

    private void initPager() {
        for (int i =0; i < drawables.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.guide_page_view, null);
            view.findViewById(R.id.image).setBackground(getResources().getDrawable(drawables[i]));
            if (i == drawables.length - 1) {
                view.findViewById(R.id.into_app).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.into_app).setVisibility(View.GONE);
            }

            view.findViewById(R.id.into_app).setOnClickListener(this);

            mViewList.add(view);
        }
        mViewPagerAdapter = new ViewPagerAdapter(mViewList);
        mViewPagerAdapter.setShowPageCount(mViewList.size());
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.setCurrentItem(0);
        mViews[0].setSelected(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.into_app:
                if (mSharedPreferences != null) {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("isFirst", "false");
                    editor.commit();
                }
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        for (View view : mViews) {
            view.setSelected(false);
        }

        mViews[i].setSelected(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
