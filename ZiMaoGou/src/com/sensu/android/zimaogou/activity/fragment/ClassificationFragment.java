package com.sensu.android.zimaogou.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductClassificationResponse;
import com.sensu.android.zimaogou.activity.*;
import com.sensu.android.zimaogou.adapter.ClassificationGridAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;

import java.util.List;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class ClassificationFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

//    String[] toolsList = new String[]{"常用分类", "潮流女装", "品牌男装", "内衣配饰", "家用电器"
//            , "手机数码", "电脑办公", "个护化妆", "母婴频道", "食物生鲜", "酒水饮料", "家居家纺", "整车车品"
//            , "鞋靴箱包", "运动户外", "图书", "玩具乐器", "钟表", "居家生活", "珠宝饰品", "音像制品", "家具建材"
//            , "计生情趣", "营养保健", "奢侈礼品", "生活服务", "旅游出行"};

    public static final String PRODUCT_ID = "product_id";
    private ProductClassificationResponse mProductClassificationResponse;
    private ProductClassificationResponse.ProductCategory mCurrentCategory;

    private View[] mViews;
    private ScrollView mScrollView;

    private GridView mGridView;
    private ClassificationGridAdapter mClassificationGridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classification_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView() {
        getClassification();

        mScrollView = (ScrollView) mParentActivity.findViewById(R.id.scroll_view);
        mParentActivity.findViewById(R.id.search).setOnClickListener(this);

        mGridView = (GridView) mParentActivity.findViewById(R.id.small_classification_grid);
        mClassificationGridAdapter = new ClassificationGridAdapter(mParentActivity);
        mGridView.setAdapter(mClassificationGridAdapter);

        mGridView.setOnItemClickListener(this);
    }

    private void layoutView(List<ProductClassificationResponse.ProductCategory> productCategoryList) {
        LinearLayout containerLayout = (LinearLayout) mParentActivity.findViewById(R.id.classification_list);
        mViews = new View[productCategoryList.size()];
        for (int i = 0; i < productCategoryList.size(); i++) {
            View view = LayoutInflater.from(mParentActivity).inflate(R.layout.classification_list_item, null);
            view.setId(i);
            view.setOnClickListener(mOnClickListener);
            TextView textView = (TextView) view.findViewById(R.id.classification_name);
            textView.setText(productCategoryList.get(i).name);
            containerLayout.addView(view);
            mViews[i] = view;
        }
        changColor(0);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changColor(view.getId());
            changTextLocation(view.getId());
            //todo 调用gridView的adapter进行数据刷新
        }
    };

    private void changColor(int id) {
        for (int i = 0; i < mProductClassificationResponse.data.size(); i++) {
            if (i != id) {
                mViews[i].findViewById(R.id.classification_name).setSelected(false);
                mViews[i].findViewById(R.id.line).setSelected(false);
            }
        }
        mViews[id].findViewById(R.id.classification_name).setSelected(true);
        mViews[id].findViewById(R.id.line).setSelected(true);
        mClassificationGridAdapter.setCategoryList(mProductClassificationResponse.data.get(id));
        mCurrentCategory = mProductClassificationResponse.data.get(id);
    }

    private void changTextLocation(int position) {
        int x = mViews[position].getTop();
        mScrollView.smoothScrollTo(0, x);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(mParentActivity, ProductListActivity.class);
        intent.putExtra(PRODUCT_ID, mCurrentCategory.sub.get(i).id);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                startActivity(new Intent(mParentActivity, SearchActivity.class));
                break;
        }
    }

    public void getClassification() {
        HttpUtil.get(IConstants.sProduct_classification, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                ProductClassificationResponse productClassificationResponse = JSON.parseObject(content, ProductClassificationResponse.class);
                mProductClassificationResponse = productClassificationResponse;
                layoutView(productClassificationResponse.data);
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }
}
