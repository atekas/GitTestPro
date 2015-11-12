package com.sensu.android.zimaogou.external.greendao.helper;

import android.content.Context;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.external.greendao.dao.DaoMaster;
import com.sensu.android.zimaogou.external.greendao.dao.DaoSession;
import com.sensu.android.zimaogou.external.greendao.dao.UserInfoDao;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;

/**
 * Created by zhangwentao on 2015/11/12.
 */
public class GDUserInfoHelper extends GDBaseHelper {

    private static GDUserInfoHelper sGDUserInfoHelper;
    private static Context sContext;
    private DaoSession mDaoSession;
    private UserInfoDao mUserInfoDao;

    public GDUserInfoHelper() { };

    public static GDUserInfoHelper getInstance(Context context) {
        if (sGDUserInfoHelper == null) {
            sGDUserInfoHelper = new GDUserInfoHelper();
            if (sContext == null) {
                sContext = context.getApplicationContext();
            }
            sGDUserInfoHelper.mDaoSession = BaseApplication.getBaseApplication().getDaoSession(context);
            sGDUserInfoHelper.mUserInfoDao = sGDUserInfoHelper.mDaoSession.getUserInfoDao();
        }
        return sGDUserInfoHelper;
    }

    public UserInfo getUserInfo() {
        return mUserInfoDao.loadAll().get(0);
    }

    public void insertUserInfo(UserInfo userInfo) {
        mUserInfoDao.insert(userInfo);
    }
}
