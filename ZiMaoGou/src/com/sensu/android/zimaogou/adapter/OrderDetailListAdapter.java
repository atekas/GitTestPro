package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderDetailListAdapter extends SimpleBaseAdapter {
    ArrayList<MyOrderMode> mOrders;
    int state;
    public void flush(ArrayList<MyOrderMode> orders,int state){
        this.mOrders = orders;
        this.notifyDataSetChanged();
        this.state = state;
    }
    public OrderDetailListAdapter(Context context, ArrayList<MyOrderMode> mOrders,int state) {
        super(context);
        this.mOrders = mOrders;
        this.state = state;
    }

    @Override
    public int getCount() {
        return mOrders.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_list_item,null);
            viewHolder.tv_warehouseName = (TextView) view.findViewById(R.id.tv_warehouseName);
            viewHolder.tv_amount_coupon = (TextView) view.findViewById(R.id.tv_amount_coupon);
            viewHolder.tv_amount_tax = (TextView) view.findViewById(R.id.tv_amount_tax);
            viewHolder.tv_amount_express = (TextView) view.findViewById(R.id.tv_amount_express);
            viewHolder.tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            viewHolder.tv_orderTime = (TextView) view.findViewById(R.id.tv_orderTime);
            viewHolder.lv_products = (ListView) view.findViewById(R.id.product_child);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_warehouseName.setText(mOrders.get(i).getDeliver_address());
        viewHolder.tv_amount_coupon.setText("￥"+mOrders.get(i).getAmount_coupon());
        viewHolder.tv_amount_tax.setText("￥"+mOrders.get(i).getAmount_tax());
        viewHolder.tv_amount_express.setText("￥"+mOrders.get(i).getAmount_express());
        viewHolder.tv_amount.setText("￥"+mOrders.get(i).getAmount_real());
        viewHolder.tv_orderTime.setText("下单时间："+mOrders.get(i).getCreated_at());
        OrderDetailChildListAdapter adapter = new OrderDetailChildListAdapter(mContext,mOrders.get(i),state);
        viewHolder.lv_products.setDivider(null);
        viewHolder.lv_products.setAdapter(adapter);
        UiUtils.setListViewHeightBasedOnChilds(viewHolder.lv_products);
        return view;
    }

    public static class  ViewHolder{
        public TextView tv_warehouseName,tv_amount_coupon,tv_amount_tax,tv_amount_express,tv_amount,tv_orderTime;
        public ListView lv_products;
    }

    private double amountMoney(OrderMode order){
        double sum = 0;
        for(ProductMode pro:order.getPros()){
            sum += pro.getPrice()*pro.getNum();
        }
        return sum;
    }
}
