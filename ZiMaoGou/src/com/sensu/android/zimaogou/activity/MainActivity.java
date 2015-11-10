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

    private FrameLayout mFrameLayout;

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

        mFrameLayout = (FrameLayout) findViewById(R.id.content);

        mFragmentManager = getFragmentManager();

        mHomePageFm = (HomePageFragment) mFragmentManager.findFragmentById(R.id.home_page_fm);
        mClassificationFm = (ClassificationFragment) mFragmentManager.findFragmentById(R.id.classification_fm);
        mTourBuyFm = (TourBuyFragment) mFragmentManager.findFragmentById(R.id.tour_buy_fm);
        mShoppingBagFm = (ShoppingBagFragment) mFragmentManager.findFragmentById(R.id.shopping_bag_fm);
        mMeFm = (MeFragment) mFragmentManager.findFragmentById(R.id.me_fm);

        mHomePageBottomView.setSelected(true);
        showFragment(0);
    }

    private void showFragment(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                if (mHomePageFm != null) {
                    ft.show(mHomePageFm).commit();
                }
                break;
            case 1:
                if (mClassificationFm != null) {
                    ft.show(mClassificationFm).commit();
                }
                break;
            case 2:
                if (mTourBuyFm != null) {
                    ft.show(mTourBuyFm).commit();
                }
                break;
            case 3:
                if (mShoppingBagFm != null) {
                    ft.show(mShoppingBagFm).commit();
                }
                break;
            case 4:
                if (mMeFm != null) {
                    ft.show(mMeFm).commit();
                }
                break;
        }
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
