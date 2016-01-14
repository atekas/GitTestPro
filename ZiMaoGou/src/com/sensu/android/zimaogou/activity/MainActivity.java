package com.sensu.android.zimaogou.activity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.fragment.*;
import com.sensu.android.zimaogou.utils.AppInfoUtils;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import org.apache.http.Header;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    public static final String SELECT_TAB = "select_tab";

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
    private PushAgent mPushAgent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        updateApp();
        mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setPushCheck(true);    //默认不检查集成配置文件
//		mPushAgent.setLocalNotificationIntervalLimit(false);  //默认本地通知间隔最少是10分钟

        //应用程序启动统计
        //参考集成文档的1.5.1.2
        //http://dev.umeng.com/push/android/integration#1_5_1
        mPushAgent.onAppStart();

        //开启推送并设置注册的回调处理
        mPushAgent.enable(mRegisterCallback);
        initViews();
    }
    public Handler handler = new Handler();

    //此处是注册的回调处理
    //参考集成文档的1.7.10
    //http://dev.umeng.com/push/android/integration#1_7_10
    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(String registrationId) {
            // TODO Auto-generated method stub
            handler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    updateStatus();
                }
            });
        }
    };
    private void updateStatus() {
        String pkgName = getApplicationContext().getPackageName();
        String info = String.format("enabled:%s  isRegistered:%s  DeviceToken:%s " +
                        "SdkVersion:%s AppVersionCode:%s AppVersionName:%s",
                mPushAgent.isEnabled(), mPushAgent.isRegistered(),
                mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
                UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));
//        tvStatus.setText("应用包名："+pkgName+"\n"+info);
//
//        btnEnable.setImageResource(mPushAgent.isEnabled()?R.drawable.open_button:R.drawable.close_button);
//        btnEnable.setClickable(true);
//        copyToClipBoard();
//
//        Log.i(TAG, "updateStatus:" + String.format("enabled:%s  isRegistered:%s",
//                mPushAgent.isEnabled(), mPushAgent.isRegistered()));
//        Log.i(TAG, "=============================");
    }
    @Override
    protected void onResume() {
        super.onResume();
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
            case R.id.update_now:
                //todo 升级
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.update_late:
                if (dialog != null) {
                    dialog.dismiss();
                }
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
        mCurrentIndex = getIntent().getIntExtra(SELECT_TAB, HOME_PAGE_FM_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            DisplayUtils.setActivity(this);
        }
    }

    private void updateApp() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", "0");
        requestParams.put("version", AppInfoUtils.getVersionName());

        HttpUtil.get(IConstants.sVersion, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response.optString("ret").equals("-1")) {
                    return;
                }
                String version = response.optJSONObject("data").optString("version_number");
                if (AppInfoUtils.getVersionName().equals(version)) {
                    //版本号一致
                } else {
                    PromptUtils.showToast("有新版本");
                    if (response.optJSONObject("data").optString("is_force_update").equals("1")) {
                        //需要强制升级
                        showUpdateAppInfo(false);
                    } else {
                        //用户选择升级
                        showUpdateAppInfo(true);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private Dialog dialog;

    private void showUpdateAppInfo(final boolean isUpdate) {
        dialog = new Dialog(this, R.style.dialog);
        dialog.setContentView(R.layout.update_app_version);
        dialog.setCancelable(isUpdate);
        dialog.show();
        dialog.findViewById(R.id.update_late).setOnClickListener(this);
        dialog.findViewById(R.id.update_now).setOnClickListener(this);
        if (isUpdate) {
            dialog.findViewById(R.id.update_late).setVisibility(View.VISIBLE);
        } else {
            dialog.findViewById(R.id.update_late).setVisibility(View.GONE);
        }
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!isUpdate) {
                    //程序退出
                }
            }
        });
    }
}
