package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
import com.sensu.android.zimaogou.activity.mycenter.ApplySalesAfterActivity;

/**
 * Created by qi.yang on 2015/12/1.
 */
public class RefundOrCommentAdapter extends SimpleBaseAdapter {
    private int type = 0;//跳转哪个页面

    public RefundOrCommentAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.refundorcomment_item,null);
            holder.bt_submit = (Button) view.findViewById(R.id.bt_submit);
            holder.bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (type){
                        case 0:
                            mContext.startActivity(new Intent(mContext, ApplySalesAfterActivity.class));
                            break;
                        case 1:

                            mContext.startActivity(new Intent(mContext, ProductCommentActivity.class));
                            break;
                    }
                }
            });
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

        return view;
    }

    private class ViewHolder{
        public Button bt_submit;
    }
}
