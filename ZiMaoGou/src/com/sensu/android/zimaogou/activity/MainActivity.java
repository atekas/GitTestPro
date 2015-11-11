package com.sensu.android.zimaogou.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.fragment.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    /**
     * Called when the activity is first created.
     */
    private HomePageFragment mHomePageFm;
    private ClassificationFragment mClassificationFm;
    private TourBuyFragment mTourBuyFm;
    private ShoppingBagFragment mShoppingBagFm;
    private MeFragment mMeFm;

    private FragmentManager mFragmentManager;

    //底层按钮
    private TextView mHomePageBottomView;
    private TextView mClassificationBottomView;
    private TextView mTourBuyBottomView;
    private TextView mShoppingBagBottomView;
    private TextView mMeBottomView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initViews();

    }

    private void initViews() {
        mHomePageBottomView = (TextView) findViewById(R.id.bottom_home_page);
        mClassificationBottomView = (TextView) findViewById(R.id.bottom_classification);
        mTourBuyBottomView = (TextView) findViewById(R.id.bottom_tour_buy);
        mShoppingBagBottomView = (TextView) findViewById(R.id.bottom_shopping_bag);
        mMeBottomView = (TextView) findViewById(R.id.bottom_me);

        mHomePageBottomView.setOnClickListener(this);
        mClassificationBottomView.setOnClickListener(this);
        mTourBuyBottomView.setOnClickListener(this);
        mShoppingBagBottomView.setOnClickListener(this);
        mMeBottomView.setOnClickListener(this);

        mFragmentManager = getFragmentManager();

        mHomePageBottomView.setSelected(true);
        showFragment(0);
    }

    private void showFragment(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                if (mHomePageFm == null) {
                    mHomePageFm = new HomePageFragment();
                    ft.add(R.id.content, mHomePageFm);
                } else {
                    ft.show(mHomePageFm);
                }
                break;
            case 1:
                if (mClassificationFm == null) {
                    mClassificationFm = new ClassificationFragment();
                    ft.add(R.id.content, mClassificationFm);
                } else {
                    ft.show(mClassificationFm);
                }
                break;
            case 2:
                if (mTourBuyFm == null) {
                    mTourBuyFm = new TourBuyFragment();
                    ft.add(R.id.content, mTourBuyFm);
                } else {
                    ft.show(mTourBuyFm);
                }
                break;
            case 3:
                if (mShoppingBagFm == null) {
                    mShoppingBagFm = new ShoppingBagFragment();
                    ft.add(R.id.content, mShoppingBagFm);
                } else {
                    ft.show(mShoppingBagFm);
                }
                break;
            case 4:
                if (mMeFm == null) {
                    mMeFm = new MeFragment();
                    ft.add(R.id.content, mMeFm);
                } else {
                    ft.show(mMeFm);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mHomePageFm != null) {
            ft.hide(mHomePageFm);
        }
        if (mClassificationFm != null) {
            ft.hide(mClassificationFm);
        }
        if (mTourBuyFm != null) {
            ft.hide(mTourBuyFm);
        }
        if (mShoppingBagFm != null) {
            ft.hide(mShoppingBagFm);
        }
        if (mMeFm != null) {
            ft.hide(mMeFm);
        }
    }

    @Override
    public void onClick(View view) {
        clearBottomStatus();
        switch (view.getId()) {
            case R.id.bottom_home_page:
                showFragment(0);
                mHomePageBottomView.setSelected(true);
                break;
            case R.id.bottom_classification:
                showFragment(1);
                mClassificationBottomView.setSelected(true);
                break;
            case R.id.bottom_tour_buy:
                showFragment(2);
                mTourBuyBottomView.setSelected(true);
                break;
            case R.id.bottom_shopping_bag:
                showFragment(3);
                mShoppingBagBottomView.setSelected(true);
                break;
            case R.id.bottom_me:
                showFragment(4);
                mMeBottomView.setSelected(true);
                break;
        }
    }

    private void clearBottomStatus() {
        mHomePageBottomView.setSelected(false);
        mClassificationBottomView.setSelected(false);
        mTourBuyBottomView.setSelected(false);
        mShoppingBagBottomView.setSelected(false);
        mMeBottomView.setSelected(false);
    }
}
