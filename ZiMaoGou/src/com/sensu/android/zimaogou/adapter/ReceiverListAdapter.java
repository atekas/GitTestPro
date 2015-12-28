package com.sensu.android.zimaogou.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.ReceiverAddressMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.mycenter.ReceiverAddressEditActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.HttpUtil;
import com.sensu.android.zimaogou.utils.LogUtils;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/20.
 */
public class ReceiverListAdapter extends SimpleBaseAdapter {
    ArrayList<ReceiverAddressMode> addresses = new ArrayList<ReceiverAddressMode>();
    private boolean mIsEdit;
    UserInfo userInfo;

    public ReceiverListAdapter(Context context, boolean isEdit, ArrayList<ReceiverAddressMode> addresses) {
        super(context);
        mIsEdit = isEdit;
        this.addresses = addresses;
        userInfo = GDUserInfoHelper.getInstance(mContext).getUserInfo();
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.receiver_address_list_item, null);
            holder.tv_address = (TextView) view.findViewById(R.id.tv_address);
            holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_edit = (TextView) view.findViewById(R.id.tv_edit);
            holder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            holder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
            holder.mEditAddress = (RelativeLayout) view.findViewById(R.id.edit_address);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mIsEdit) {
            holder.mEditAddress.setVisibility(View.GONE);
        } else {
            holder.mEditAddress.setVisibility(View.VISIBLE);
        }

        final int position = i;
        if (addresses.get(position).getIs_default().equals("1")) {
            holder.img_choose.setImageResource(R.drawable.d_03);
        } else {
            holder.img_choose.setImageResource(R.drawable.d_06);
        }
        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ReceiverAddressEditActivity.class)
                        .putExtra("title", "编辑收货地址")
                        .putExtra("data", addresses.get(position)));
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
        holder.tv_name.setText(addresses.get(position).getName());
        holder.tv_phone.setText(addresses.get(position).getMobile());
        holder.tv_address.setText(addresses.get(position).getProvince() + addresses.get(position).getCity() + addresses.get(position).getDistrict() + " " + addresses.get(position).getAddress());
        return view;
    }


    private class ViewHolder {
        TextView tv_name, tv_phone, tv_address, tv_edit, tv_delete;
        ImageView img_choose;
        RelativeLayout mEditAddress;
    }

    /**
     * 删除地址
     */
    Dialog mDeleteAddressDialog;

    private void DeleteAddressDialog(final int position) {
        mDeleteAddressDialog = new Dialog(mContext, R.style.dialog);
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


    /**
     * 刷新数据
     */
    private void flush() {
        this.notifyDataSetChanged();
    }

    /**
     * 删除地址
     *
     * @param position
     */
    private void deleteAddress(final int position) {
        String url = IConstants.sAddAddress + "/" + addresses.get(position).getId()+"/delete";
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("id",addresses.get(position).getId());

        HttpUtil.postWithSign(userInfo.getToken(), url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                LogUtils.d("删除地址返回：", response.toString());
                addresses.remove(position);
                flush();
            }
        });

    }

    /**
     * 设为默认地址
     *
     * @param position
     */
    private void setDefaultAddress(final int position) {
        String url = IConstants.sAddAddress + "/" + addresses.get(position).getId();
        RequestParams requestParams = new RequestParams();

        requestParams.put("uid", userInfo.getUid());
        requestParams.put("name", addresses.get(position).getName());
        requestParams.put("mobile", addresses.get(position).getMobile());
        requestParams.put("id_card", addresses.get(position).getId_card());

        requestParams.put("address", addresses.get(position).getAddress());
        requestParams.put("is_default", "1");
        requestParams.put("id", addresses.get(position).getId());
        requestParams.put("province", addresses.get(position).getProvince_id());
        requestParams.put("city", addresses.get(position).getCity_id());
        requestParams.put("district", addresses.get(position).getDistrict_id());


        HttpUtil.postWithSign(userInfo.getToken(), url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                LogUtils.d("设为默认地址：", response.toString());
                for (ReceiverAddressMode t : addresses) {
                    t.setIs_default("0");
                }
                addresses.get(position).setIs_default("1");
                flush();
            }
        });


    }
}
