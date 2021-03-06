package com.sensu.android.zimaogou.activity;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.fragment.*;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.mycenter.MessageActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.utils.AppInfoUtils;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private ImageView mTourBuyBottomView;
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
        //暂时关闭推送
//        mPushAgent.disable();

        BaseApplication.activityContext = this;
        initViews();
        showMessageDialog();
    }

    private void showMessageDialog() {
        if (!BaseApplication.isGetPush) {
            return;
        }
        final Dialog dialog = new Dialog(this, R.style.dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_address_dialog);
        TextView tv_tip = (TextView) dialog.findViewById(R.id.tv_tip);
        Button bt_sure = (Button) dialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
        tv_tip.setText("您有一条新的消息");
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.isGetPush = false;
                if (GDUserInfoHelper.getInstance(MainActivity.this).getUserInfo() == null) {
                    PromptUtils.showToast("请先登录");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseApplication.isGetPush = false;
                dialog.dismiss();
            }
        });
        dialog.show();
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
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initViews() {
        mHomePageBottomView = (TextView) findViewById(R.id.bottom_home_page);
        mClassificationBottomView = (TextView) findViewById(R.id.bottom_classification);
        mTourBuyBottomView = (ImageView) findViewById(R.id.bottom_tour_buy);
        mShoppingBagBottomView = (TextView) findViewById(R.id.bottom_shopping_bag);
        mMeBottomView = (TextView) findViewById(R.id.bottom_me);

        mHomePageBottomView.setOnClickListener(this);
        mClassificationBottomView.setOnClickListener(this);
        mTourBuyBottomView.setOnClickListener(this);
        mShoppingBagBottomView.setOnClickListener(this);
        mMeBottomView.setOnClickListener(this);

        mFragmentManager = getFragmentManager();

        viewPerformClick();

        final SharedPreferences preferences = getSharedPreferences("zimaogou_preferences", Activity.MODE_PRIVATE);
        String isShow = preferences.getString("isShow", "true");
        if (isShow.equals("true")) {
            findViewById(R.id.page_image).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.page_image).setVisibility(View.GONE);
        }
        findViewById(R.id.page_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.page_image).setVisibility(View.GONE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("isShow", "false");
                editor.commit();
            }
        });
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

    public void viewPerformClick() {
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
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
//        if(ssoHandler != null){
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
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
                String url = response.optJSONObject("data").optString("version_url");

                String[] getVersion = new String[3];
                String[] localVersion = new String[3];

                getVersion = version.split("\\.");
                localVersion = AppInfoUtils.getVersionName().split("\\.");

                if (Integer.parseInt(getVersion[0]) > Integer.parseInt(localVersion[0])
                        || Integer.parseInt(getVersion[1]) > Integer.parseInt(localVersion[1])
                        || Integer.parseInt(getVersion[2]) > Integer.parseInt(localVersion[2])) {

                    if (response.optJSONObject("data").optString("is_force_update").equals("1")) {
                        //需要强制升级
                        showUpdateAppInfo(false, version, url);
                    } else {
                        //用户选择升级
                        showUpdateAppInfo(true, version, url);
                    }
                } else {
                    //版本号一致
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private Dialog dialog;

    private void showUpdateAppInfo(final boolean isUpdate, final String version, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本升级");
        builder.setCancelable(isUpdate);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!TextUtils.isEmpty(url)) {
                    downFile(url, version);
                }
            }
        });

        if (isUpdate) {
            builder.setNegativeButton("暂不升级", null);
        }

        dialog = builder.show();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!isUpdate) {
                    //程序退出
                    finish();
                }
            }
        });
    }

    String filePath = BaseApplication.getBaseApplication().getBaseExternalCacheDir()
            + File.separator + "temp/download/";

    private void downFile(final String url, final String version) {
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpGet get = new HttpGet(url);
                HttpResponse response;
                try {
                    response = client.execute(get);
                    HttpEntity entity = response.getEntity();
                    long length = entity.getContentLength();
                    InputStream is = entity.getContent();
                    FileOutputStream fileOutputStream = null;
                    if (is != null) {
                        File file = new File(filePath + File.separator + "自贸购" + version + ".apk");
                        if (file.exists()) {
                            file.delete();
                        }

                        if (!new File(filePath).exists()) {
                            new File(filePath).mkdirs();
                        }

                        file.createNewFile();
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[1024];
                        int ch = -1;
                        int count = 0;
                        while ((ch = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, ch);
                            count += ch;
                            if (length > 0) {
                            }
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    down(version);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void down(final String version) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(filePath + File.separator + "自贸购" + version + ".apk")),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }
        });
    }
}
