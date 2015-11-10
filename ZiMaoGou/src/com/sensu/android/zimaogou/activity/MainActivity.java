package com.sensu.android.zimaogou.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFragmentManager = getFragmentManager();

        mHomePageFm = new HomePageFragment();
        mClassificationFm = new ClassificationFragment();
        mTourBuyFm = new TourBuyFragment();
        mShoppingBagFm = new ShoppingBagFragment();
        mMeFm = new MeFragment();

//        showFragment(0);

        findViewById(R.id.bottom_home_page).setOnClickListener(this);
        findViewById(R.id.bottom_classification).setOnClickListener(this);
        findViewById(R.id.bottom_tour_buy).setOnClickListener(this);
        findViewById(R.id.bottom_shopping_bag).setOnClickListener(this);
        findViewById(R.id.bottom_me).setOnClickListener(this);
    }

//    private void showFragment(int index) {
//        FragmentTransaction ft = mFragmentManager.beginTransaction();
//        hideFragment(ft);
//        switch (index) {
//            case 0:
//                if (mHomePageFm != null) {
//                    ft.show(mHomePageFm);
//                }
//                ft.add(R.id.home_page_fm, mHomePageFm).commit();
//                break;
//            case 1:
//                if (mClassificationFm != null) {
//                    ft.show(mClassificationFm);
//                }
//                ft.add(R.id.classification_fm, mClassificationFm);
//                break;
//            case 2:
//                if (mTourBuyFm != null) {
//                    ft.show(mTourBuyFm);
//                }
//                ft.add(R.id.tour_buy_fm, mTourBuyFm).commit();
//                break;
//            case 3:
//                if (mShoppingBagFm != null) {
//                    ft.show(mShoppingBagFm);
//                }
//                ft.add(R.id.shopping_bag_fm, mShoppingBagFm).commit();
//                break;
//            case 4:
//                if (mMeFm != null) {
//                    ft.show(mMeFm);
//                }
//                ft.add(R.id.me_fm, mMeFm).commit();
//                break;
//        }
//    }

//    private void hideFragment(FragmentTransaction ft) {
//        if (mHomePageFm != null) {
//            ft.hide(mHomePageFm).commit();
//        }
//        if (mClassificationFm != null) {
//            ft.hide(mClassificationFm).commit();
//        }
//        if (mTourBuyFm != null) {
//            ft.hide(mTourBuyFm).commit();
//        }
//        if (mShoppingBagFm != null) {
//            ft.hide(mShoppingBagFm).commit();
//        }
//        if (mMeFm != null) {
//            ft.hide(mMeFm).commit();
//        }
//    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bottom_home_page:
//                showFragment(0);
//                break;
//            case R.id.bottom_classification:
//                showFragment(1);
//                break;
//            case R.id.bottom_tour_buy:
//                showFragment(2);
//                break;
//            case R.id.bottom_shopping_bag:
//                showFragment(3);
//                break;
//            case R.id.bottom_me:
//                showFragment(4);
//                break;
//        }
    }
}
