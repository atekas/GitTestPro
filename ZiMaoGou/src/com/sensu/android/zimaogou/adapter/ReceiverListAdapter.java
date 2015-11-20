package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.ReceiverAddressEditActivity;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverListAdapter extends SimpleBaseAdapter{

    public ReceiverListAdapter(Context context) {
        super(context);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.receiver_address_list_item,null);
            holder.tv_address = (TextView) view.findViewById(R.id.tv_address);
            holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_edit = (TextView) view.findViewById(R.id.tv_edit);
            holder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            holder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ReceiverAddressEditActivity.class));
            }
        });

        return view;
    }


    private class ViewHolder {
        TextView tv_name,tv_phone,tv_address,tv_edit,tv_delete;
        ImageView img_choose;
    }
}
