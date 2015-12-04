package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.OrderMode;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.RefundOrCommentActivity;
import com.sensu.android.zimaogou.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderDetailListAdapter extends SimpleBaseAdapter {
    ArrayList<OrderMode> mOrders;
    public void flush(ArrayList<OrderMode> orders){
        this.mOrders = orders;
        this.notifyDataSetChanged();
    }
    public OrderDetailListAdapter(Context context, ArrayList<OrderMode> mOrders) {
        super(context);
        this.mOrders = mOrders;
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

            viewHolder.lv_products = (ListView) view.findViewById(R.id.lv_products);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }


        OrderDetailChildListAdapter adapter = new OrderDetailChildListAdapter(mContext,mOrders.get(i).getPros(),mOrders.get(i).getType());
        viewHolder.lv_products.setDivider(null);
        viewHolder.lv_products.setAdapter(adapter);
        adapter.flush(mOrders.get(i).getPros());
        UiUtils.setListViewHeightBasedOnChilds(viewHolder.lv_products);
        return view;
    }

    public static class  ViewHolder{
        public TextView tv_orderNo,tv_orderType,tv_showNum,tv_amount,tv_freight;
        public ListView lv_products;
        public RelativeLayout rl_amount,rl_button;
        public Button bt_cancel,bt_submit,bt_comment;
    }

    private double amountMoney(OrderMode order){
        double sum = 0;
        for(ProductMode pro:order.getPros()){
            sum += pro.getPrice()*pro.getNum();
        }
        return sum;
    }
}
