package com.sensu.android.zimaogou;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import com.sensu.android.zimaogou.external.greendao.dao.DaoMaster;
import com.sensu.android.zimaogou.external.greendao.dao.DaoSession;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.auth.NTUserPrincipal;

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

    @Override
    public void onCreate() {
        super.onCreate();

        mBaseApplication = this;

        // /storage/emulated/0/general/
        mUserDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "general" + File.separator;
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
