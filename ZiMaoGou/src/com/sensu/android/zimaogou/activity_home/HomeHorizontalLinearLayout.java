package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.StoreMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.GoodShopActivity;
import com.sensu.android.zimaogou.activity.SpellOrderActivity;
import com.sensu.android.zimaogou.activity.SpellOrderDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsAdapter;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/12.
 */
public class HomeHorizontalLinearLayout extends LinearLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    HorizontalListView mhListView;
    HorizontalListViewAdapter mhListViewAdapter;
    StoreHorizontalListViewAdapter mStoreListAdapter;
    TextView mTitle;
    int mType = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
    ArrayList<ProductMode> pros = new ArrayList<ProductMode>();//产品数组
    ArrayList<StoreMode> stors = new ArrayList<StoreMode>();//店铺数组
    public HomeHorizontalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mhListView = (HorizontalListView) findViewById(R.id.lv_recommend);
        mTitle = (TextView) findViewById(R.id.tv_title);

        findViewById(R.id.more).setOnClickListener(this);

        mhListView.setOnItemClickListener(this);
    }

    public void setData(int Type){
        mType = Type;

        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) mhListView.getLayoutParams();
        int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度

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

        StoreMode store1 = new StoreMode();
        store1.setStoreName("百年印花老店");
        store1.setTestStoreImage(R.drawable.store1);
        store1.setTestCountryImage(R.drawable.index_14);
        store1.setTestCountryName("法国巴黎");
        stors.add(store1);
        StoreMode store2 = new StoreMode();
        store2.setStoreName("百年印花老店");
        store2.setTestStoreImage(R.drawable.store2);
        store2.setTestCountryImage(R.drawable.index_16);
        store2.setTestCountryName("日本北海道");
        stors.add(store2);
        StoreMode store3 = new StoreMode();
        store3.setStoreName("百年印花老店");
        store3.setTestStoreImage(R.drawable.store3);
        store3.setTestCountryImage(R.drawable.index_16);
        store3.setTestCountryName("韩国明洞");
        stors.add(store3);
        switch (Type){
            case 1://每日推荐
                mTitle.setText(getResources().getText(R.string.daily_recommend));
                findViewById(R.id.title_layout).setVisibility(GONE);
                //设置水平listview的高度
                linearParams.height = width/3 + DisplayUtils.dp2px(28);
                mhListView.setLayoutParams(linearParams);
                mhListView.setPadding(0,0,DisplayUtils.dp2px(6),0);
                mhListViewAdapter = new HorizontalListViewAdapter(getContext(),pros,Type);
                mhListView.setAdapter(mhListViewAdapter);
                break;
            case 2://拼单特价
                mTitle.setText(getResources().getText(R.string.group_special));
                linearParams.height = DisplayUtils.dp2px(245);
                mhListViewAdapter = new HorizontalListViewAdapter(getContext(),pros,Type);
                mhListView.setAdapter(mhListViewAdapter);
                break;
            case 3://发现好店铺
                mTitle.setText(getResources().getText(R.string.find_shop));
                linearParams.height = width/3 + DisplayUtils.dp2px(28);
                mhListView.setLayoutParams(linearParams);
                mhListView.setPadding(0,0,DisplayUtils.dp2px(6),0);
                mStoreListAdapter = new StoreHorizontalListViewAdapter(getContext(),stors);
                mhListView.setAdapter(mStoreListAdapter);
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (mType) {
            case 1:
                PromptUtils.showToast("游购详情");
                getContext().startActivity(new Intent(getContext(), TourBuyDetailsActivity.class));
                break;
            case 2:
                PromptUtils.showToast("拼单详情");
                getContext().startActivity(new Intent(getContext(), SpellOrderDetailsActivity.class));
                break;
            case 3:
                PromptUtils.showToast("好店铺详情");
                getContext().startActivity(new Intent(getContext(), TourBuyDetailsActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more:
                if (mType == 1) {
                    //没有更多按钮
                } else if (mType == 2) {
                    getContext().startActivity(new Intent(getContext(), SpellOrderActivity.class));
                } else if (mType ==3) {
                    getContext().startActivity(new Intent(getContext(), GoodShopActivity.class));
                }
                break;
        }
    }
}
