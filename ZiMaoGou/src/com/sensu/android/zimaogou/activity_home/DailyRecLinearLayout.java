package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/12.
 */
public class DailyRecLinearLayout extends LinearLayout{
    HorizontalListView mhListView;
    HorizontalListViewAdapter mhListViewAdapter;
    TextView mTitle;
    int mSize = 3; //一屏显示多少个
    int mType = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
    ArrayList<ProductMode> pros = new ArrayList<ProductMode>();
    public DailyRecLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mhListView = (HorizontalListView) findViewById(R.id.lv_recommend);
        mTitle = (TextView) findViewById(R.id.tv_title);
        int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mhListView.getLayoutParams();
        linearParams.height = width/3 + DisplayUtils.dp2px(28);
        mhListView.setLayoutParams(linearParams);


    }

    public void setData(int Size,int Type){
        if(Type == 1){
            mTitle.setText(getResources().getText(R.string.daily_recommend));
        }
        ProductMode mode1 = new ProductMode();
        mode1.setTestImg(R.drawable.product1);
        mode1.setTestTitle("懒人支架，给你最好的体验");
        pros.add(mode1);
        ProductMode mode2 = new ProductMode();
        mode2.setTestImg(R.drawable.product2);
        mode2.setTestTitle("抹茶手工皂精油");
        pros.add(mode2);
        ProductMode mode3 = new ProductMode();
        mode3.setTestImg(R.drawable.product3);
        mode3.setTestTitle("YEASALAND——your best partner");
        pros.add(mode3);
        ProductMode mode4 = new ProductMode();
        mode4.setTestImg(R.drawable.product1);
        mode4.setTestTitle("可伤过的痛过的我，向谁述说");
        pros.add(mode4);
        ProductMode mode5 = new ProductMode();
        mode5.setTestImg(R.drawable.product2);
        mode5.setTestTitle("还是放开了说好不分的手");
        pros.add(mode5);
        mhListViewAdapter = new HorizontalListViewAdapter(getContext(),pros,Size,Type);
        mhListView.setAdapter(mhListViewAdapter);
    }
}
