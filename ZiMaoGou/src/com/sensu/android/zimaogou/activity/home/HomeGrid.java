package com.sensu.android.zimaogou.activity.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.HomeGridResponse;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.sensu.android.zimaogou.activity.ProductListActivity;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.ImageUtils;
import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhangwentao on 2015/11/24.
 */
public class HomeGrid extends LinearLayout {

    private ListView mListView;
    private HomeGridAdapter mHomeGridAdapter;

    private HomeGridResponse mHomeGridResponse;

    public HomeGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mListView = (ListView) findViewById(R.id.home_grid);
        getHomeTag6();
    }

    class HomeGridAdapter extends SimpleBaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            count = mHomeGridResponse.data.size() / 3;
            if (mHomeGridResponse.data.size() % 3 != 0) {
                count++;
            }
            return count;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.home_grid_item, null);
            }
            int leftPosition = 3 * i;
            final HomeGridResponse.HomeGridData homeGridDataLeft = mHomeGridResponse.data.get(leftPosition);
            ((TextView) view.findViewById(R.id.name_1)).setText(homeGridDataLeft.name);
            ((TextView) view.findViewById(R.id.type_1)).setText(homeGridDataLeft.alias);
            ImageUtils.displayImage(homeGridDataLeft.image, ((ImageView) view.findViewById(R.id.image_1)));

            view.findViewById(R.id.product_1).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProductListActivity.class);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, homeGridDataLeft.id);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, homeGridDataLeft.name);
                    intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                    getContext().startActivity(intent);
                }
            });

            int centerPosition = 3 * i + 1;
            final HomeGridResponse.HomeGridData homeGridDataCenter = mHomeGridResponse.data.get(centerPosition);
            ((TextView) view.findViewById(R.id.name_2)).setText(homeGridDataCenter.name);
            ((TextView) view.findViewById(R.id.type_2)).setText(homeGridDataCenter.alias);
            ImageUtils.displayImage(homeGridDataCenter.image, ((ImageView) view.findViewById(R.id.image_2)));

            view.findViewById(R.id.product_2).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProductListActivity.class);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, homeGridDataCenter.id);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, homeGridDataCenter.name);
                    intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                    getContext().startActivity(intent);
                }
            });

            int rightPosition = 3 * i + 2;
            final HomeGridResponse.HomeGridData homeGridDataRight = mHomeGridResponse.data.get(rightPosition);
            ((TextView) view.findViewById(R.id.name_3)).setText(homeGridDataRight.name);
            ((TextView) view.findViewById(R.id.type_3)).setText(homeGridDataRight.alias);
            ImageUtils.displayImage(homeGridDataRight.image, ((ImageView) view.findViewById(R.id.image_3)));

            view.findViewById(R.id.product_3).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), ProductListActivity.class);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TAG, homeGridDataRight.id);
                    intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, homeGridDataRight.name);
                    intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                    getContext().startActivity(intent);
                }
            });

            return view;
        }
    }

    private void getHomeTag6() {
        HttpUtil.get(IConstants.sHome_grid, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                HomeGridResponse homeGridResponse = JSON.parseObject(response.toString(), HomeGridResponse.class);
                mHomeGridResponse = homeGridResponse;
                mHomeGridAdapter = new HomeGridAdapter();
                mListView.setAdapter(mHomeGridAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
