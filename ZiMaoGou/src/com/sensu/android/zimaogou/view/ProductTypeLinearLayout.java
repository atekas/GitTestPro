package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductTypeModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.widget.ProductTypeListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/12/28.
 */
public class ProductTypeLinearLayout extends LinearLayout implements ProductTypeListView.OnTypeClickListener {

    private List<ProductTypeModel> mColorProductTypeList;
    private List<ProductTypeModel> mSizeProductTypeList;
    private List<ProductTypeModel> mCapacityProductTypeList;

    private ProductDetailsResponse mProductDetailsResponse;

    private View[] mViews;

    public ProductTypeLinearLayout(Context context) {
        super(context);
    }

    public ProductTypeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductTypeLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse) {
        mProductDetailsResponse = productDetailsResponse;
        int size = productDetailsResponse.data.spec_attr.size();
        mViews = new View[size];
        for (int i = 0; i < size; i++) {
            View view = View.inflate(getContext(), R.layout.product_type_item, null);
            ((TextView) view.findViewById(R.id.type_name)).setText(productDetailsResponse.data.spec_attr.get(i).cn);
            view.setId(i);
            if (productDetailsResponse.data.spec_attr.get(i).en.equals("color")) {
                setColor(view, i);
            } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("size")) {
                setSize(view, i);
            } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("capacity")) {
                setCapacity(view, i);
            }
            mViews[i] = view;
            addView(view);
        }
    }

    private void setColor(View view, int i) {
        List<ProductTypeModel> colorProductTypeList = new ArrayList<ProductTypeModel>();
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (colorProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("color");
                productTypeModel1.setTypeName(spec.color);
                colorProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : colorProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.color)) {
                        break;
                    } else {
                        ProductTypeModel productTypeModel1 = new ProductTypeModel();
                        productTypeModel1.setIsSelect(false);

                        if (Integer.parseInt(spec.num) > 1) {
                            productTypeModel1.setEnable(true);
                        }

                        productTypeModel1.setType("color");
                        productTypeModel1.setTypeName(spec.color);
                        colorProductTypeList.add(productTypeModel1);
                    }
                }
            }
        }
        mColorProductTypeList = colorProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(colorProductTypeList, i, this);
    }

    private void setSize(View view, int i) {
        List<ProductTypeModel> sizeProductTypeList = new ArrayList<ProductTypeModel>();
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (sizeProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("size");
                productTypeModel1.setTypeName(spec.size);
                sizeProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : sizeProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.size)) {
                        break;
                    } else {
                        ProductTypeModel productTypeModel1 = new ProductTypeModel();
                        productTypeModel1.setIsSelect(false);

                        if (Integer.parseInt(spec.num) > 1) {
                            productTypeModel1.setEnable(true);
                        }

                        productTypeModel1.setType("size");
                        productTypeModel1.setTypeName(spec.size);
                        sizeProductTypeList.add(productTypeModel1);
                    }
                }
            }
        }
        mSizeProductTypeList = sizeProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(sizeProductTypeList, i, this);
    }

    private void setCapacity(View view, int i) {
        List<ProductTypeModel> capacityProductTypeList = new ArrayList<ProductTypeModel>();
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (capacityProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("capacity");
                productTypeModel1.setTypeName(spec.capacity);
                capacityProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : capacityProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.capacity)) {
                        break;
                    } else {
                        ProductTypeModel productTypeModel1 = new ProductTypeModel();
                        productTypeModel1.setIsSelect(false);

                        if (Integer.parseInt(spec.num) > 1) {
                            productTypeModel1.setEnable(true);
                        }

                        productTypeModel1.setType("capacity");
                        productTypeModel1.setTypeName(spec.capacity);
                        capacityProductTypeList.add(productTypeModel1);
                    }
                }
            }
        }
        mCapacityProductTypeList = capacityProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(capacityProductTypeList, i, this);
    }

    @Override
    public void onTypeClick(String type, int position) {
        PromptUtils.showToast(type);
//        int size = mProductDetailsResponse.data.spec_attr.size();
//        for (int i = 0; i < size; i++) {
//            if (!mProductDetailsResponse.data.spec_attr.get(i).en.equals(type)) {
//                if (type.equals("color")) {
//                    setCapacity(mViews[position], position);
//                } else if (type.equals("size")) {
//                    setSize(mViews[position], position);
//                } else if (type.equals("capacity")) {
//                    setCapacity(mViews[position], position);
//                }
//            }
//        }
    }

    private void setColorAgain(View view, int position) {
        if (mColorProductTypeList != null) {
            for (ProductTypeModel productTypeModel : mColorProductTypeList) {

            }
        }
    }

    private void setSizeAgain() {

    }

    private void setCapacityAgain() {

    }
}
