package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.external.greendao.model.SearchKeyword;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SearchListAdapter extends SimpleBaseAdapter {

    private List<SearchKeyword> mSearchKeywordList;

    public SearchListAdapter(Context context) {
        super(context);
    }

    public void setSearchKeywordList(List<SearchKeyword> searchKeywordList) {
        mSearchKeywordList = searchKeywordList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSearchKeywordList == null ? 0 : mSearchKeywordList.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.search_listview_item, null);
        }

        SearchKeyword searchKeyword = mSearchKeywordList.get(i);

        ((TextView) view.findViewById(R.id.search_keyword)).setText(searchKeyword.getKeyword());


        return view;
    }
}
