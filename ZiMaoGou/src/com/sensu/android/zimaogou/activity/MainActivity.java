package com.sensu.android.zimaogou.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.fragment.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final int HOME_PAGE_FM_CODE = 0;
    public static final int CLASSIFICATION_FM_CODE = HOME_PAGE_FM_CODE + 1;
    public static final int TOUR_BUT_FM_CODE = CLASSIFICATION_FM_CODE + 1;
    public static final int SHOPPING_BAG_FM_CODE = TOUR_BUT_FM_CODE + 1;
    public static final int ME_FM_CODE = SHOPPING_BAG_FM_CODE + 1;

    private int mCurrentIndex;

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

        mCurrentIndex = HOME_PAGE_FM_CODE;
        viewPerformClick();
    }

    private void showFragment(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case HOME_PAGE_FM_CODE:
                if (mHomePageFm == null) {
                    mHomePageFm = new HomePageFragment();
                    ft.add(R.id.content, mHomePageFm);
                } else {
                    ft.show(mHomePageFm);
                }
                mCurrentIndex = HOME_PAGE_FM_CODE;
                break;
            case CLASSIFICATION_FM_CODE:
                if (mClassificationFm == null) {
                    mClassificationFm = new ClassificationFragment();
                    ft.add(R.id.content, mClassificationFm);
                } else {
                    ft.show(mClassificationFm);
                }
                mCurrentIndex = CLASSIFICATION_FM_CODE;
                break;
            case TOUR_BUT_FM_CODE:
                if (mTourBuyFm == null) {
                    mTourBuyFm = new TourBuyFragment();
                    ft.add(R.id.content, mTourBuyFm);
                } else {
                    ft.show(mTourBuyFm);
                }
                mCurrentIndex = TOUR_BUT_FM_CODE;
                break;
            case SHOPPING_BAG_FM_CODE:
                if (mShoppingBagFm == null) {
                    mShoppingBagFm = new ShoppingBagFragment();
                    ft.add(R.id.content, mShoppingBagFm);
                } else {
                    ft.show(mShoppingBagFm);
                }
                mCurrentIndex = SHOPPING_BAG_FM_CODE;
                break;
            case ME_FM_CODE:
                if (mMeFm == null) {
                    mMeFm = new MeFragment();
                    ft.add(R.id.content, mMeFm);
                } else {
                    ft.show(mMeFm);
                }
                mCurrentIndex = ME_FM_CODE;
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
                showFragment(HOME_PAGE_FM_CODE);
                mHomePageBottomView.setSelected(true);
                break;
            case R.id.bottom_classification:
                showFragment(CLASSIFICATION_FM_CODE);
                mClassificationBottomView.setSelected(true);
                break;
            case R.id.bottom_tour_buy:
                showFragment(TOUR_BUT_FM_CODE);
                mTourBuyBottomView.setSelected(true);
                break;
            case R.id.bottom_shopping_bag:
                showFragment(SHOPPING_BAG_FM_CODE);
                mShoppingBagBottomView.setSelected(true);
                break;
            case R.id.bottom_me:
                showFragment(ME_FM_CODE);
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

    private void viewPerformClick() {
        switch (mCurrentIndex) {
            case HOME_PAGE_FM_CODE:
                mHomePageBottomView.performClick();
                break;
            case CLASSIFICATION_FM_CODE:
                mClassificationBottomView.performClick();
                break;
            case TOUR_BUT_FM_CODE:
                mTourBuyBottomView.performClick();
                break;
            case SHOPPING_BAG_FM_CODE:
                mShoppingBagBottomView.performClick();
                break;
            case ME_FM_CODE:
                mMeBottomView.performClick();
                break;
        }
    }
}
