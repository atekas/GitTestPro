package com.sensu.android.zimaogou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductTypeModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.ProductDetailsResponse;
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

    private OnProductChangedListener mOnProductChangedListener;

    public ProductTypeLinearLayout(Context context) {
        super(context);
    }

    public ProductTypeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductTypeLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setProductDetailsResponse(ProductDetailsResponse productDetailsResponse, OnProductChangedListener onProductChangedListener) {
        getDefaultType(productDetailsResponse);
        mOnProductChangedListener = onProductChangedListener;
        mProductDetailsResponse = productDetailsResponse;
        int size = productDetailsResponse.data.spec_attr.size();
        for (int i = 0; i < size; i++) {
            View view = View.inflate(getContext(), R.layout.product_type_item, null);
            ((TextView) view.findViewById(R.id.type_name)).setText(productDetailsResponse.data.spec_attr.get(i).cn);
            view.setId(i);
            if (productDetailsResponse.data.spec_attr.get(i).en.equals("color")) {
                setColor(view);
            } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("size")) {
                setSize(view);
            } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("capacity")) {
                setCapacity(view);
            }
            addView(view);
        }
        mOnProductChangedListener.getProductColor();
    }

    private void setColor(View view) {
        List<ProductTypeModel> colorProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (colorProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("color");
                productTypeModel1.setTypeName(spec.color);
                if (mColor != null) {
                    if (mColor.equals(spec.color)) {
                        productTypeModel1.setIsSelect(true);
                    }
                }
                colorProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : colorProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.color)) {
                        canAdd = false;
                        break;
                    } else {
                        canAdd = true;
                    }
                }

                if (canAdd) {
                    ProductTypeModel productTypeModel1 = new ProductTypeModel();
                    productTypeModel1.setIsSelect(false);

                    if (Integer.parseInt(spec.num) > 1) {
                        productTypeModel1.setEnable(true);
                    }

                    productTypeModel1.setType("color");
                    productTypeModel1.setTypeName(spec.color);

                    if (mColor != null) {
                        if (mColor.equals(spec.color)) {
                            productTypeModel1.setIsSelect(true);
                        }
                    }
                    colorProductTypeList.add(productTypeModel1);
                }
            }
        }
        mColorProductTypeList = colorProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(colorProductTypeList, this);
    }

    private void setSize(View view) {
        List<ProductTypeModel> sizeProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
            if (sizeProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("size");
                productTypeModel1.setTypeName(spec.size);
                if (mSize != null) {
                    if (mSize.equals(spec.size)) {
                        productTypeModel1.setIsSelect(true);
                    }
                }
                sizeProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : sizeProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.size)) {
                        canAdd = false;
                        break;
                    } else {
                        canAdd = true;
                    }
                }

                if (canAdd) {
                    ProductTypeModel productTypeModel1 = new ProductTypeModel();
                    productTypeModel1.setIsSelect(false);

                    if (Integer.parseInt(spec.num) > 1) {
                        productTypeModel1.setEnable(true);
                    }

                    productTypeModel1.setType("size");
                    productTypeModel1.setTypeName(spec.size);
                    if (mSize != null) {
                        if (mSize.equals(spec.size)) {
                            productTypeModel1.setIsSelect(true);
                        }
                    }
                    sizeProductTypeList.add(productTypeModel1);
                }
            }
        }
        mSizeProductTypeList = sizeProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(sizeProductTypeList, this);
    }

    private void setCapacity(View view) {
        List<ProductTypeModel> capacityProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (capacityProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (Integer.parseInt(spec.num) > 1) {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("capacity");
                productTypeModel1.setTypeName(spec.capacity);
                if (mCapacity != null) {
                    if (mCapacity.equals(spec.capacity)) {
                        productTypeModel1.setIsSelect(true);
                    }
                }
                capacityProductTypeList.add(productTypeModel1);
            } else {
                for (ProductTypeModel productTypeModel : capacityProductTypeList) {
                    if (productTypeModel.getTypeName().equals(spec.capacity)) {
                        canAdd = false;
                        break;
                    } else {
                        canAdd = true;
                    }
                }

                if (canAdd) {
                    ProductTypeModel productTypeModel1 = new ProductTypeModel();
                    productTypeModel1.setIsSelect(false);

                    if (Integer.parseInt(spec.num) > 1) {
                        productTypeModel1.setEnable(true);
                    }

                    productTypeModel1.setType("capacity");
                    productTypeModel1.setTypeName(spec.capacity);
                    if (mCapacity != null) {
                        if (mCapacity.equals(spec.capacity)) {
                            productTypeModel1.setIsSelect(true);
                        }
                    }
                    capacityProductTypeList.add(productTypeModel1);
                }
            }
        }
        mCapacityProductTypeList = capacityProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(capacityProductTypeList, this);
    }

    @Override
    public void onTypeClick(String type, String value) {
        removeAllViews();
        int size = mProductDetailsResponse.data.spec_attr.size();
        for (int i = 0; i < size; i++) {
            View view = View.inflate(getContext(), R.layout.product_type_item, null);
            ((TextView) view.findViewById(R.id.type_name)).setText(mProductDetailsResponse.data.spec_attr.get(i).cn);
            view.setId(i);

            String rule = mProductDetailsResponse.data.spec_attr.get(i).en;

            if (type.equals("color")) {
                setColorAgain(type, value);
            } else if (type.equals("size")) {
                setSizeAgain(type, value);
            } else if (type.equals("capacity")) {
                setCapacityAgain(type, value);
            }

            if (rule.equals("color")) {
                ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(mColorProductTypeList, this);
            } else if (rule.equals("size")) {
                ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(mSizeProductTypeList, this);
            } else if (rule.equals("capacity")) {
                ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(mCapacityProductTypeList, this);
            }
            addView(view);
        }

        if (mOnProductChangedListener != null) {
            mOnProductChangedListener.getProductColor();
        }
    }

    private void setColorAgain(String type, String value) {
        if (mColorProductTypeList == null) {
            return;
        }
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
            if (type.equals("size")) {
                if (spec.size.equals(value)) {
                    for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            } else if (type.equals("capacity")) {
                if (spec.capacity.equals(value)) {
                    for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            }
        }
    }

    private void setSizeAgain(String type, String value) {
        if (mSizeProductTypeList == null) {
            return;
        }
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
            if (type.equals("color")) {
                if (spec.color.equals(value)) {
                    for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            } else if (type.equals("capacity")) {
                if (spec.capacity.equals(value)) {
                    for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            }
        }
    }

    private void setCapacityAgain(String type, String value) {
        if (mCapacityProductTypeList == null) {
            return;
        }
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
            if (type.equals("color")) {
                if (spec.color.equals(value)) {
                    for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            } else if (type.equals("size")) {
                if (spec.size.equals(value)) {
                    for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel.setEnable(true);
                        } else {
                            productTypeModel.setEnable(false);
                        }
                    }
                }
            }
        }
    }

    private String mColor;
    private String mSize;
    private String mCapacity;

    private void getDefaultType(ProductDetailsResponse productDetailsResponse) {
        for (ProductDetailsResponse.Spec spec : productDetailsResponse.data.spec) {
            if (Integer.parseInt(spec.num) > 0) {
                for (ProductDetailsResponse.SpecAttr specAttr : productDetailsResponse.data.spec_attr) {
                    if (specAttr.en.equals("color")) {
                        mColor = spec.color;
                    } else if (specAttr.en.equals("size")) {
                        mSize = spec.size;
                    } else if (specAttr.en.equals("capacity")) {
                        mCapacity = spec.capacity;
                    }
                }
                break;
            }
        }

    }

    public List<ProductTypeModel> getColor() {
        return mColorProductTypeList;
    }

    public List<ProductTypeModel> getSize() {
        return mSizeProductTypeList;
    }

    public List<ProductTypeModel> getCapacity() {
        return mCapacityProductTypeList;
    }

    public interface OnProductChangedListener {
        public void getProductColor();
    }
}
