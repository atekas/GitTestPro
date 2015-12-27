package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductTypeModel;
import com.sensu.android.zimaogou.R;

import java.util.List;

/**
 * Created by zhangwentao on 2015/12/27.
 */
public class ProductTypeListView extends FlowLayout {

    private List<ProductTypeModel> mProductTypeModelList;

    public ProductTypeListView(Context context) {
        super(context);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTypeData(List<ProductTypeModel> productTypeModelList) {
        removeAllViews();
        mProductTypeModelList = productTypeModelList;
        for (int i = 0; i < productTypeModelList.size(); i++) {
            inflateTypeView(productTypeModelList.get(i));
        }
    }

    private void inflateTypeView(final ProductTypeModel productTypeModel) {
        View productTypeView = View.inflate(getContext(), R.layout.product_type_view, null);
        final TextView type = (TextView) productTypeView.findViewById(R.id.type_name);
        type.setText(productTypeModel.getTypeName());

//        if (productTypeModel.getIsSelect()) {
//            type.setSelected(true);
//        } else {
//            type.setSelected(false);
//        }

//        type.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                productTypeModel.setIsSelect(true);
//            }
//        });

        addView(productTypeView);
    }
}
