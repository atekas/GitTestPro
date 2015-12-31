package com.sensu.android.zimaogou.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.sensu.android.zimaogou.R;

/**
 * Created by qi.yang on 2015/11/13.
 */
public class UiUtils {
    /**
     *
     * 重新计算listview高度 用于ScrollView嵌套ListView
     *
     */
    public static void setListViewHeightBasedOnChilds(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        int totalDividerHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(desiredWidth, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalDividerHeight = listView.getDividerHeight()
                * (listAdapter.getCount() - 1);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + totalDividerHeight;
        listView.setLayoutParams(params);
    }




}
