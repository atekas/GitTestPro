package com.sensu.android.zimaogou;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.sensu.android.zimaogou.Mode.ProvinceMode;
import com.sensu.android.zimaogou.external.greendao.dao.DaoMaster;
import com.sensu.android.zimaogou.external.greendao.dao.DaoSession;
import com.sensu.android.zimaogou.handler.CustomNotificationHandler;
import com.sensu.android.zimaogou.utils.*;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;


import java.io.File;

/**
 * Created by winter on 2015/9/22.
 *
 * @author winter
 */
public class BaseApplication extends Application {

    public static String DB_NAME = "zimaogou_db";

    private String mUserDir;
    private String mCacheDir;
    private String mExternalCacheDir;

    private String mSavePicPath;

    private static BaseApplication mBaseApplication;

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private static final String TAG = BaseApplication.class.getName();
    private PushAgent mPushAgent;
    public static ProvinceMode mChooseAddress ;
    @Override
    public void onCreate() {
        super.onCreate();
        mChooseAddress = new ProvinceMode();
        mBaseApplication = this;
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //新浪微博
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        // /storage/emulated/0/general/
        mUserDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "zimaogou" + File.separator;
        // /storage/emulated/0/general/image/
        mSavePicPath = mUserDir + "image" + File.separator;
        // /data/data/com.personal.android.general/cache/
        mCacheDir = getCacheDir().getAbsolutePath() + File.separator;
        File file = getExternalCacheDir();

        if (file != null) {
            mExternalCacheDir = file.getAbsolutePath() + File.separator;
        } else {
            // /storage/emulated/0/Android/data/com.personal.android.general/cache/
            mExternalCacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android"
                    + File.separator + "data" + File.separator + getPackageName() + File.separator + "cache" + File.separator;
        }

        DisplayUtils.init(this);
        PromptUtils.init(this);
//        TelephoneInfoUtils.init(this);
//        WiFiInfoUtils.init(this);
//        NetworkTypeUtils.init(this);
//        LogUtils.setIsLogEnable(true);
        LogUtils.setIsWriteDisk(true);
//        AppInfoUtils.init(this);
        ImageUtils.init(this);




        //友盟推送
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);

        UmengMessageHandler messageHandler = new UmengMessageHandler(){
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if(isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             *
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title)
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
		/*UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
			}
		};*/
        CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

    }
    public static void setChooseProvince(ProvinceMode provinceMode){
        mChooseAddress.data = provinceMode.data;
        mChooseAddress.setName(provinceMode.getName());
        mChooseAddress.setId(provinceMode.getId());
        mChooseAddress = provinceMode;
    }
    public static ProvinceMode getChooseProvince(){

        return mChooseAddress;
    }
    public static String getStr(int resId) {
        return mBaseApplication.getString(resId);
    }

    public static String getStr(int resId, Object... formatArgs) {
        return mBaseApplication.getString(resId, formatArgs);
    }

    public static BaseApplication getBaseApplication() {
        return mBaseApplication;
    }

    public String getUserDir() {
        return mUserDir;
    }

    public String getSavePicPath() {
        return mSavePicPath;
    }

    public String getBaseExternalCacheDir() {
        return mExternalCacheDir;
    }

    public String getBaseUserDir() {
        return mUserDir;
    }

    public String getBaseCacheDir() {
        return mCacheDir;
    }

    public void exit() {
        ActivityManager.getInstance().finishActivities();
    }

    /**
     * 获取DaoMaster
     * @return
     */
    public DaoMaster getDaoMaster(Context context) {
        if (mDaoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
            mDaoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return mDaoMaster;
    }

    /**
     * 获取DaoSession
     * @param context
     * @return
     */
    public DaoSession getDaoSession(Context context) {
        if (mDaoSession == null) {
            if (mDaoMaster == null) {
                mDaoMaster = getDaoMaster(context);
            }
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
