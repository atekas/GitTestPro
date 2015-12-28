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
    private OnTypeClickListener mOnTypeClickListener;

    private int mPosition;

    public ProductTypeListView(Context context) {
        super(context);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ProductTypeListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTypeData(List<ProductTypeModel> productTypeModelList, int position, OnTypeClickListener onTypeClickListener) {
        mOnTypeClickListener = onTypeClickListener;
        mPosition = position;
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

        if (productTypeModel.getIsSelect()) {
            type.setSelected(true);
        } else {
            type.setSelected(false);
        }

        if (productTypeModel.getEnable()) {
            type.setEnabled(true);
        } else {
            type.setEnabled(false);
        }

        type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initProductTypeModelList();
                productTypeModel.setIsSelect(true);
                if (mOnTypeClickListener != null) {
                    mOnTypeClickListener.onTypeClick(productTypeModel.getType(), mPosition);
                }
                setTypeData(mProductTypeModelList, mPosition, new OnTypeClickListener() {
                    @Override
                    public void onTypeClick(String type, int position) {

                    }
                });
            }
        });

        addView(productTypeView);
    }

    private void initProductTypeModelList() {
        for (ProductTypeModel productTypeModel : mProductTypeModelList) {
            productTypeModel.setIsSelect(false);
        }
    }

    public interface OnTypeClickListener {
        public void onTypeClick(String type, int position);
    }
}
