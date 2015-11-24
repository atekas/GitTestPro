package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.LivelyMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/13.
 */
public class HomeVerticalLinearLayout extends LinearLayout{
    TextView mTitle;
    ListView mListView;
    ArrayList<LivelyMode> lives = new ArrayList<LivelyMode>();
    public HomeVerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitle = (TextView) findViewById(R.id.tv_title);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setDivider(null);
    }

    public void setData(int Type){
        LivelyMode mode1 = new LivelyMode();
        mode1.setTestImage(R.drawable.special_1);
        mode1.setLowPrice(128);
        mode1.setTitle("日本FANCL营养特卖");
        lives.add(mode1);
        LivelyMode mode2 = new LivelyMode();
        mode2.setTestImage(R.drawable.special_2);
        mode2.setLowPrice(25);
        mode2.setTitle("日本资生堂洗护特卖");
        lives.add(mode2);
        LivelyMode mode3 = new LivelyMode();
        mode3.setTestImage(R.drawable.special_3);
        mode3.setLowPrice(79);
        mode3.setTitle("韩国凯多彩妆特卖");
        lives.add(mode3);
        switch (Type){
            case 4:
                mTitle.setText(getResources().getText(R.string.lively));
                mListView.setAdapter(new LivelyVerticalListViewAdapter(getContext(),lives));
                break;
            case 5:
                mTitle.setText("推荐单品");
                mListView.setAdapter(new RecommendItemAdapter(getContext()));
                break;
        }
        UiUtils.setListViewHeightBasedOnChilds(mListView);
    }
}
