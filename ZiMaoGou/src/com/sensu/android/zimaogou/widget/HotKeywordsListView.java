package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.HotKeywordsResponse;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.sensu.android.zimaogou.activity.ProductListActivity;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/12/30.
 */
public class HotKeywordsListView extends FlowLayout {

    public HotKeywordsListView(Context context) {
        super(context);
    }

    public HotKeywordsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public HotKeywordsListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setHotKeywords(HotKeywordsResponse hotKeywords) {
        removeAllViews();
        if (hotKeywords != null) {
            for (int i = 0; i < hotKeywords.data.size(); i++) {
                inflateView(hotKeywords.data.get(i));
            }
        }
    }

    private void inflateView(final HotKeywordsResponse.HotKeywordsData hotKeywordsData) {
        TextView textView = new TextView(getContext());
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.black_959595));
        textView.setBackground(getContext().getResources().getDrawable(R.drawable.drawable_choose_product_border));
        textView.setPadding(5, 5, 5, 5);
        textView.setText(hotKeywordsData.name);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PromptUtils.showToast("click" + hotKeywordsData.name);
                Intent intent = new Intent(getContext(), ProductListActivity.class);
                intent.putExtra(ProductListActivity.IS_NO_TITLE, true);
                intent.putExtra(ProductListActivity.PRODUCT_LIST_KEYWORD, hotKeywordsData.name);
                intent.putExtra(ProductListActivity.PRODUCT_LIST_TITLE, "搜索");
                getContext().startActivity(intent);
            }
        });

        addView(textView);
    }
}
