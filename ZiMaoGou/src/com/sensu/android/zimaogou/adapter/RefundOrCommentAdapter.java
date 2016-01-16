package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
import com.sensu.android.zimaogou.activity.mycenter.ApplySalesAfterActivity;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * Created by qi.yang on 2015/12/1.
 */
public class RefundOrCommentAdapter extends SimpleBaseAdapter {
    private int type = 0;//跳转哪个页面
    MyOrderMode myOrderMode = new MyOrderMode();
    public RefundOrCommentAdapter(Context context, int type,MyOrderMode myOrderMode) {
        super(context);
        this.type = type;
        this.myOrderMode= myOrderMode;
    }

    @Override
    public int getCount() {
        return myOrderMode.getGoods().size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.refundorcomment_item,null);
            holder.bt_submit = (Button) view.findViewById(R.id.bt_submit);

            holder.img_product = (ImageView) view.findViewById(R.id.img_pro);
            holder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
            holder.tv_productSpc = (TextView) view.findViewById(R.id.tv_spc);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        switch (type){
            case 1:
                holder.bt_submit.setText("评价");
                break;
            case 0:
                holder.bt_submit.setText("申请售后");
        }
        final MyOrderGoodsMode myOrderGoodsMode = myOrderMode.getGoods().get(i);
        holder.bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (type){
                    case 0:
                        mContext.startActivity(new Intent(mContext, ApplySalesAfterActivity.class));
                        break;
                    case 1:
                        mContext.startActivity(new Intent(mContext, ProductCommentActivity.class).putExtra("order",myOrderMode)
                                .putExtra("goods",myOrderGoodsMode));
                        break;
                }
            }
        });
        ImageUtils.displayImage(myOrderMode.getGoods().get(i).getImage(),holder.img_product,ImageUtils.mItemTopOptions);
        holder.tv_productSpc.setText(myOrderMode.getGoods().get(i).getSpec());
        holder.tv_productName.setText(myOrderMode.getGoods().get(i).getName());
        return view;
    }

    private class ViewHolder{
        public Button bt_submit;
        public ImageView img_product;
        public TextView tv_productName;
        public TextView tv_productSpc;
    }
}
