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
        mOnProductChangedListener = onProductChangedListener;
        mProductDetailsResponse = productDetailsResponse;
        int size = productDetailsResponse.data.spec_attr.size();
        if (size == 1) {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(getContext(), R.layout.product_type_item, null);
                ((TextView) view.findViewById(R.id.type_name)).setText(productDetailsResponse.data.spec_attr.get(i).cn);
                view.setId(i);
                if (productDetailsResponse.data.spec_attr.get(i).en.equals("color")) {
                    setColor(view, size);
                } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("size")) {
                    setSize(view, size);
                } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("capacity")) {
                    setCapacity(view, size);
                }
                addView(view);
            }
        } else {
            for (int i = 0; i < size; i++) {
                View view = View.inflate(getContext(), R.layout.product_type_item, null);
                ((TextView) view.findViewById(R.id.type_name)).setText(productDetailsResponse.data.spec_attr.get(i).cn);
                view.setId(i);
                if (productDetailsResponse.data.spec_attr.get(i).en.equals("color")) {
                    setColor(view, size);
                } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("size")) {
                    setSize(view, size);
                } else if (productDetailsResponse.data.spec_attr.get(i).en.equals("capacity")) {
                    setCapacity(view, size);
                }
                addView(view);
            }
        }

        mOnProductChangedListener.getProductColor();
    }

    private void setColor(View view, int size) {
        List<ProductTypeModel> colorProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (colorProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (size == 1) {
                    if (Integer.parseInt(spec.num) > 0) {
                        productTypeModel1.setEnable(true);
                    } else {
                        productTypeModel1.setEnable(false);
                    }
                } else {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("color");
                productTypeModel1.setTypeName(spec.color);
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

                    if (size == 1) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel1.setEnable(true);
                        } else {
                            productTypeModel1.setEnable(false);
                        }
                    } else {
                        productTypeModel1.setEnable(true);
                    }

                    productTypeModel1.setType("color");
                    productTypeModel1.setTypeName(spec.color);
                    colorProductTypeList.add(productTypeModel1);
                }
            }
        }
        mColorProductTypeList = colorProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(colorProductTypeList, this);
    }

    private void setSize(View view, int size) {
        List<ProductTypeModel> sizeProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
            if (sizeProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (size == 1) {
                    if (Integer.parseInt(spec.num) > 0) {
                        productTypeModel1.setEnable(true);
                    } else {
                        productTypeModel1.setEnable(false);
                    }
                } else {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("size");
                productTypeModel1.setTypeName(spec.size);
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

                    if (size == 1) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel1.setEnable(true);
                        } else {
                            productTypeModel1.setEnable(false);
                        }
                    } else {
                        productTypeModel1.setEnable(true);
                    }

                    productTypeModel1.setType("size");
                    productTypeModel1.setTypeName(spec.size);
                    sizeProductTypeList.add(productTypeModel1);
                }
            }
        }
        mSizeProductTypeList = sizeProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(sizeProductTypeList, this);
    }

    private void setCapacity(View view, int size) {
        List<ProductTypeModel> capacityProductTypeList = new ArrayList<ProductTypeModel>();
        boolean canAdd = false;
        for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {

            if (capacityProductTypeList.size() == 0) {
                ProductTypeModel productTypeModel1 = new ProductTypeModel();
                productTypeModel1.setIsSelect(false);

                if (size == 1) {
                    if (Integer.parseInt(spec.num) > 0) {
                        productTypeModel1.setEnable(true);
                    } else {
                        productTypeModel1.setEnable(false);
                    }
                } else {
                    productTypeModel1.setEnable(true);
                }

                productTypeModel1.setType("capacity");
                productTypeModel1.setTypeName(spec.capacity);
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

                    if (size == 1) {
                        if (Integer.parseInt(spec.num) > 0) {
                            productTypeModel1.setEnable(true);
                        } else {
                            productTypeModel1.setEnable(false);
                        }
                    } else {
                        productTypeModel1.setEnable(false);
                    }

                    productTypeModel1.setType("capacity");
                    productTypeModel1.setTypeName(spec.capacity);
                    capacityProductTypeList.add(productTypeModel1);
                }
            }
        }
        mCapacityProductTypeList = capacityProductTypeList;
        ((ProductTypeListView) view.findViewById(R.id.product_type_list)).setTypeData(capacityProductTypeList, this);
    }

    @Override
    public void onTypeClick(String type, String value, boolean isCancel) {
        removeAllViews();
        int size = mProductDetailsResponse.data.spec_attr.size();
        for (int i = 0; i < size; i++) {
            View view = View.inflate(getContext(), R.layout.product_type_item, null);
            ((TextView) view.findViewById(R.id.type_name)).setText(mProductDetailsResponse.data.spec_attr.get(i).cn);
            view.setId(i);

            String rule = mProductDetailsResponse.data.spec_attr.get(i).en;

            if (type.equals("color")) {
                setColorAgain(type, value, isCancel);
            } else if (type.equals("size")) {
                setSizeAgain(type, value, isCancel);
            } else if (type.equals("capacity")) {
                setCapacityAgain(type, value, isCancel);
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

    private void setColorAgain(String type, String value, boolean isCancel) {
        if (isCancel) {
            if (mCapacityProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    productTypeModel.setEnable(false);
                }
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.color.equals(value)) {
                            if (spec.capacity.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }

            if (mSizeProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    productTypeModel.setEnable(false);
                }

                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.color.equals(value)) {
                            if (spec.size.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }
        } else {
            if (mColorProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    productTypeModel.setIsSelect(false);
                }
            }

            if (mSizeProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    productTypeModel.setEnable(true);
                }
            }

            if (mCapacityProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    productTypeModel.setEnable(true);
                }
            }
        }
    }

    private void setSizeAgain(String type, String value, boolean isCancel) {
        if (isCancel) {
            if (mCapacityProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    productTypeModel.setEnable(false);
                }
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.size.equals(value)) {
                            if (spec.capacity.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }

            if (mColorProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    productTypeModel.setEnable(false);
                }

                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.size.equals(value)) {
                            if (spec.color.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }
        } else {
            if (mSizeProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    productTypeModel.setIsSelect(false);
                }
            }

            if (mColorProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    productTypeModel.setEnable(true);
                }
            }

            if (mCapacityProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    productTypeModel.setEnable(true);
                }
            }
        }
    }

    private void setCapacityAgain(String type, String value, boolean isCancel) {
        if (isCancel) {
            if (mColorProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    productTypeModel.setEnable(false);
                }
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.size.equals(value)) {
                            if (spec.color.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }

            if (mSizeProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mSizeProductTypeList ){
                    productTypeModel.setEnable(false);
                }

                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    for (ProductDetailsResponse.Spec spec : mProductDetailsResponse.data.spec) {
                        if (spec.color.equals(value)) {
                            if (spec.size.equals(productTypeModel.getTypeName()) && Integer.parseInt(spec.num) > 0) {
                                productTypeModel.setEnable(true);
                            }
                        }
                    }
                }
            }
        } else {
            if (mCapacityProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mCapacityProductTypeList) {
                    productTypeModel.setIsSelect(false);
                }
            }

            if (mSizeProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mSizeProductTypeList) {
                    productTypeModel.setEnable(true);
                }
            }

            if (mColorProductTypeList != null) {
                for (ProductTypeModel productTypeModel : mColorProductTypeList) {
                    productTypeModel.setEnable(true);
                }
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
