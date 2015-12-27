package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/12/27.
 */
public class ProductTypeListView extends FlowLayout {

    public ProductTypeListView(Context context) {
        super(context);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTypeData() {

    }

    private void inflateTypeView() {
        View productTypeView = View.inflate(getContext(), R.layout.product_type_view, null);

        final TextView type = (TextView) productTypeView.findViewById(R.id.type);

        addView(type);
    }
}
