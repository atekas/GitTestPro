package com.sensu.android.zimaogou.external.greendao.helper;

import android.content.Context;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.external.greendao.dao.DaoSession;
import com.sensu.android.zimaogou.external.greendao.dao.SearchKeywordDao;
import com.sensu.android.zimaogou.external.greendao.model.SearchKeyword;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/16.
 */
public class GDSearchKeywordHelper extends GDBaseHelper {

    private static GDSearchKeywordHelper sGDSearchKeywordHelper;
    private DaoSession mDaoSession;
    private SearchKeywordDao mSearchKeywordDao;

    private GDSearchKeywordHelper() {}

    public static GDSearchKeywordHelper getInstance(Context context) {
        if (sGDSearchKeywordHelper == null) {
            sGDSearchKeywordHelper = new GDSearchKeywordHelper();
        }
        sGDSearchKeywordHelper.mDaoSession = BaseApplication.getBaseApplication().getDaoSession(context);
        sGDSearchKeywordHelper.mSearchKeywordDao = sGDSearchKeywordHelper.mDaoSession.getSearchKeywordDao();

        return sGDSearchKeywordHelper;
    }

    public List<SearchKeyword> getSearchKeyword() {
        return mSearchKeywordDao.loadAll();
    }

    public void insertKeyword(SearchKeyword searchKeyword) {
        mSearchKeywordDao.insert(searchKeyword);
    }

    public void deleteKeyword() {
        mSearchKeywordDao.deleteAll();
    }
}
