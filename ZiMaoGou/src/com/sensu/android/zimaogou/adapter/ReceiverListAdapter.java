package com.sensu.android.zimaogou.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.ReceiverAddressEditActivity;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverListAdapter extends SimpleBaseAdapter{
    ArrayList<TestAddress> addresses = new ArrayList<TestAddress>();
    public ReceiverListAdapter(Context context) {
        super(context);
        setData();
    }

    @Override
    public int getCount() {
        return addresses.size();
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
        final int position = i;
        if(addresses.get(position).isChoose) {
            holder.img_choose.setImageResource(R.drawable.d_03);
        }else{
            holder.img_choose.setImageResource(R.drawable.d_06);
        }
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ReceiverAddressEditActivity.class).putExtra("title","编辑收货地址"));
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAddressDialog(position);
            }
        });
        holder.img_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultAddress(position);
            }
        });
        return view;
    }


    private class ViewHolder {
        TextView tv_name,tv_phone,tv_address,tv_edit,tv_delete;
        ImageView img_choose;
    }

    /**
     *
     * 删除地址
     *
     */
    Dialog mDeleteAddressDialog;
    private void DeleteAddressDialog(final int position){
        mDeleteAddressDialog = new Dialog(mContext,R.style.dialog);
        mDeleteAddressDialog.setCancelable(true);
        mDeleteAddressDialog.setContentView(R.layout.delete_address_dialog);

        Button bt_sure = (Button) mDeleteAddressDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mDeleteAddressDialog.findViewById(R.id.bt_cancel);

        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteAddress(position);
                mDeleteAddressDialog.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteAddressDialog.dismiss();
            }
        });
        mDeleteAddressDialog.show();
    }

    private class TestAddress{
        public boolean isChoose = false;
    }

    /**
     * 填充测试数据
     */
    private void setData(){
        TestAddress t1 = new TestAddress();
        t1.isChoose = true;
        TestAddress t2 = new TestAddress();
        t2.isChoose = false;
        TestAddress t3 = new TestAddress();
        t3.isChoose = false ;
        addresses.add(t1);
        addresses.add(t2);
        addresses.add(t3);
    }

    /**
     * 刷新数据
     */
    private void flush(){
        this.notifyDataSetChanged();
    }

    /**
     * 删除地址
     * @param position
     */
    private void deleteAddress(int position){
        addresses.remove(position);
        flush();
    }
    /**
     * 设为默认地址
     * @param position
     */
    private void setDefaultAddress(int position){
        for(TestAddress t: addresses){
            t.isChoose = false;
        }
        addresses.get(position).isChoose = true;
        flush();
    }
}
