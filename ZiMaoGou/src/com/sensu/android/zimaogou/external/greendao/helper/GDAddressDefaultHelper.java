package com.sensu.android.zimaogou.external.greendao.helper;

import android.content.Context;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.external.greendao.dao.AddressDefaultDao;
import com.sensu.android.zimaogou.external.greendao.dao.DaoSession;
import com.sensu.android.zimaogou.external.greendao.model.AddressDefault;

/**
 * Created by zhangwentao on 2015/12/29.
 */
public class GDAddressDefaultHelper extends GDBaseHelper {
    private static GDAddressDefaultHelper sGDAddressDefaultHelper;
    private DaoSession mDaoSession;
    private AddressDefaultDao mAddressDefaultDao;

    private GDAddressDefaultHelper() {}

    public static GDAddressDefaultHelper getInstance(Context context) {
        if (sGDAddressDefaultHelper == null) {
            sGDAddressDefaultHelper = new GDAddressDefaultHelper();
        }
        sGDAddressDefaultHelper.mDaoSession = BaseApplication.getBaseApplication().getDaoSession(context);
        sGDAddressDefaultHelper.mAddressDefaultDao = sGDAddressDefaultHelper.mDaoSession.getAddressDefaultDao();

        return sGDAddressDefaultHelper;
    }

    public AddressDefault getAddressDefault() {
        if (mAddressDefaultDao.loadAll().size() == 0) {
            return null;
        }
        return mAddressDefaultDao.loadAll().get(0);
    }

    public void insertAddress(AddressDefault addressDefault) {
        mAddressDefaultDao.insert(addressDefault);
    }

    public void deleteAddress() {
        mAddressDefaultDao.deleteAll();
    }
}
