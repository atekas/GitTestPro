package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.SelectProductModel;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by zhangwentao on 2015/11/19.
 */
public class VerifyOrderAdapter extends SimpleBaseAdapter {

    private SelectProductModel mSelectProductModel;

    public VerifyOrderAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return mSelectProductModel == null ? 0 : mSelectProductModel.getGoodsInfo().size();
    }

    public void setSelectProductModel(SelectProductModel selectProductModel) {
        mSelectProductModel = selectProductModel;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.verify_order_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mProductImage = (ImageView) view.findViewById(R.id.product_pic);
            viewHolder.mProductName = (TextView) view.findViewById(R.id.product_name);
            viewHolder.mProductSize = (TextView) view.findViewById(R.id.product_size);
            viewHolder.mNum = (TextView) view.findViewById(R.id.product_count);
            viewHolder.mPrice = (TextView) view.findViewById(R.id.product_price);
            viewHolder.mBottomLine = view.findViewById(R.id.bottom_line);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (i == (getCount() - 1)) {
            viewHolder.mBottomLine.setVisibility(View.GONE);
        } else {
            viewHolder.mBottomLine.setVisibility(View.VISIBLE);
        }

        SelectProductModel.GoodsInfo goodsInfo = mSelectProductModel.getGoodsInfo().get(i);
        viewHolder.mProductName.setText(goodsInfo.getName());
        viewHolder.mPrice.setText(goodsInfo.getPrice());
        viewHolder.mNum.setText("×" + goodsInfo.getNum());
        viewHolder.mProductSize.setText(goodsInfo.getSpec());
        ImageUtils.displayImage(goodsInfo.getImage(), viewHolder.mProductImage);

        return view;
    }

    private class ViewHolder {
        ImageView mProductImage;
        TextView mProductName;
        TextView mProductSize;
        TextView mNum;
        TextView mPrice;
        View mBottomLine;
    }
}
