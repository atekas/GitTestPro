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
import com.sensu.android.zimaogou.activity.mycenter.ApplySalesAfterActivity;
import com.sensu.android.zimaogou.activity.mycenter.RefundOrCommentActivity;
import com.sensu.android.zimaogou.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/18.
 */
public class OrderListAdapter extends SimpleBaseAdapter {
    ArrayList<OrderMode> mOrders;
    public void flush(ArrayList<OrderMode> orders){
        this.mOrders = orders;
        this.notifyDataSetChanged();
    }
    public OrderListAdapter(Context context,ArrayList<OrderMode> mOrders) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.order_list_item,null);
            viewHolder.tv_amount = (TextView) view.findViewById(R.id.tv_amount);
            viewHolder.tv_freight = (TextView) view.findViewById(R.id.tv_freight);
            viewHolder.tv_orderNo = (TextView) view.findViewById(R.id.tv_orderNo);
            viewHolder.tv_orderType = (TextView) view.findViewById(R.id.tv_type);
            viewHolder.tv_showNum = (TextView) view.findViewById(R.id.tv_showNum);
            viewHolder.lv_products = (ListView) view.findViewById(R.id.lv_products);
            viewHolder.rl_amount = (RelativeLayout) view.findViewById(R.id.rl_amount);
            viewHolder.rl_button = (RelativeLayout) view.findViewById(R.id.rl_button);
            viewHolder.bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
            viewHolder.bt_submit = (Button) view.findViewById(R.id.bt_submit);
            viewHolder.bt_comment = (Button) view.findViewById(R.id.bt_comment);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (mOrders.get(i).getType()){
            case 1:
                viewHolder.rl_button.setVisibility(View.VISIBLE);
                viewHolder.rl_amount.setVisibility(View.VISIBLE);
                viewHolder.bt_cancel.setVisibility(View.GONE);
                viewHolder.bt_comment.setVisibility(View.GONE);
                viewHolder.bt_submit.setText("付款");
                viewHolder.tv_orderType.setText("待付款");
                break;
            case 2:
                viewHolder.rl_button.setVisibility(View.VISIBLE);
                viewHolder.rl_amount.setVisibility(View.VISIBLE);
                viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                viewHolder.bt_comment.setVisibility(View.GONE);
                viewHolder.bt_cancel.setText("查看物流");
                viewHolder.bt_submit.setText("确认收货");
                viewHolder.tv_orderType.setText("待收货");
                break;
            case 3:
                viewHolder.rl_button.setVisibility(View.VISIBLE);
                viewHolder.rl_amount.setVisibility(View.GONE);
                viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                viewHolder.bt_comment.setVisibility(View.GONE);
                viewHolder.bt_cancel.setText("取消订单");
                viewHolder.bt_submit.setText("撤销退款");
                viewHolder.tv_orderType.setText("待退款");
                break;
            case 5:
                viewHolder.rl_button.setVisibility(View.VISIBLE);
                viewHolder.rl_amount.setVisibility(View.VISIBLE);
                viewHolder.bt_cancel.setVisibility(View.VISIBLE);
                viewHolder.bt_comment.setVisibility(View.VISIBLE);
                viewHolder.bt_cancel.setText("查看物流");
                viewHolder.bt_submit.setText("申请售后");
                viewHolder.tv_orderType.setText("交易成功");
                break;
        }
        viewHolder.bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type",0));
            }
        });
        viewHolder.bt_comment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, RefundOrCommentActivity.class).putExtra("type",1));
            }
        });
        viewHolder.tv_showNum.setText("共"+mOrders.size()+"件商品，合计:");
        viewHolder.tv_amount.setText(amountMoney(mOrders.get(i))+"");
        OrderChildListAdapter adapter = new OrderChildListAdapter(mContext,mOrders.get(i).getPros(),mOrders.get(i).getType());
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
