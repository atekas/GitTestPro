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
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuyDetailsActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwentao on 2015/11/12.
 */
public class TourBuyAdapter extends SimpleBaseAdapter {
    ArrayList<TravelMode> travelModes = new ArrayList<TravelMode>();

    boolean isFromMy = false;
    public TourBuyAdapter(Context context) {
        super(context);
    }
    public TourBuyAdapter(Context context,boolean isFromMy){
        super(context);
        this.isFromMy = isFromMy;
    }

    public void flush(ArrayList<TravelMode> travelModes){
        if (travelModes != null) {
            this.travelModes.addAll(travelModes);
        }
        notifyDataSetChanged();
    }
    public void reFlush(ArrayList<TravelMode> travelModes){
        if (travelModes != null) {
            this.travelModes = travelModes;
        }else{
            this.travelModes.clear();
        }
        notifyDataSetChanged();
    }
    public void clearData() {
        this.travelModes.clear();
    }

    public List<TravelMode> getData() {
        return travelModes;
    }

    @Override
    public int getCount() {
        return travelModes.size();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        final int position = i;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.tour_buy_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img_contentPic = (ImageView) view.findViewById(R.id.content_pic);
            viewHolder.img_userHead = (ImageView) view.findViewById(R.id.user_head_pic);
            viewHolder.img_videoPic = (ImageView) view.findViewById(R.id.video_icon);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.content_text);
            viewHolder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            viewHolder.tv_marks = (TextView) view.findViewById(R.id.marks);
            viewHolder.tv_praiseCount = (TextView) view.findViewById(R.id.praise_count);
            viewHolder.tv_reviewCount = (TextView) view.findViewById(R.id.review_count);
            viewHolder.tv_sendTime = (TextView) view.findViewById(R.id.send_time);
            viewHolder.tv_userName = (TextView) view.findViewById(R.id.user_name);
            viewHolder.img_delete = (ImageView) view.findViewById(R.id.delete);
            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteTravelDialog(position);
                }
            });
            viewHolder.rl_praise_layout = (RelativeLayout) view.findViewById(R.id.praise_layout);
            viewHolder.rl_review_layout = (RelativeLayout) view.findViewById(R.id.review_layout);
            if(isFromMy){
                viewHolder.img_delete.setVisibility(View.VISIBLE);
            }else{
                viewHolder.img_delete.setVisibility(View.GONE);
            }
            //TODO 查找控件
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.rl_praise_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LikeClick(travelModes.get(position), travelModes.get(position).getIs_like().equals("1"));
            }
        });
        viewHolder.rl_review_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()) {
                    mContext.startActivity(new Intent(mContext, TourBuyDetailsActivity.class).putExtra("travel", travelModes.get(i))
                            .putExtra("is_comment", true));
                }else{
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
        ImageUtils.displayImage(travelModes.get(i).getAvatar(),viewHolder.img_userHead,ImageUtils.mHeadDefaultOptions);
        viewHolder.tv_userName.setText(travelModes.get(i).getName());
        viewHolder.tv_sendTime.setText(DateUtils.getTimeAgo(travelModes.get(i).getCreated_at()));
        String tag = "";
        for(int j = 0; j <travelModes.get(i).getTag().size(); j++){
            tag += " "+travelModes.get(i).getTag().get(j);
        }
        if(travelModes.get(i).getCategory().equals("1")){
            viewHolder.img_videoPic.setVisibility(View.GONE);
            if(travelModes.get(i).getMedia().image != null&&travelModes.get(i).getMedia().image.size()>0) {
                ImageUtils.displayImage(travelModes.get(i).getMedia().image.get(0), viewHolder.img_contentPic,ImageUtils.mItemTopOptions);
            }
        }else{
            viewHolder.img_videoPic.setVisibility(View.VISIBLE);
            ImageUtils.displayImage(travelModes.get(i).getMedia().cover,viewHolder.img_contentPic,ImageUtils.mItemTopOptions);
        }
        viewHolder.tv_marks.setText(travelModes.get(i).getCountry()+tag);
        if(travelModes.get(i).getIs_like().equals("1")){
            viewHolder.tv_praiseCount.setSelected(true);
        }else{
            viewHolder.tv_praiseCount.setSelected(false);
        }
        if(Integer.parseInt(travelModes.get(i).getLike_num()) == 0){
            viewHolder.tv_praiseCount.setText("点赞");
        }else {
            viewHolder.tv_praiseCount.setText(travelModes.get(i).getLike_num());
        }
        if(Integer.parseInt(travelModes.get(i).getComment_num()) == 0){
            viewHolder.tv_reviewCount.setText("评论");
        }else {
            viewHolder.tv_reviewCount.setText(travelModes.get(i).getComment_num());
        }
        viewHolder.tv_location.setText(travelModes.get(i).getLocation());
        viewHolder.tv_content.setText(travelModes.get(i).getContent());
        return view;
    }

    private static class ViewHolder {
        ImageView img_userHead,img_contentPic,img_videoPic,img_delete;
        TextView tv_userName,tv_sendTime,tv_location,tv_content,tv_marks,tv_reviewCount,tv_praiseCount;
        RelativeLayout rl_praise_layout,rl_review_layout;
    }

    /**
     * 删除游购
     */
    Dialog mDeleteAddressDialog;

    private void DeleteTravelDialog(final int position) {
        mDeleteAddressDialog = new Dialog(mContext, R.style.dialog);
        mDeleteAddressDialog.setCancelable(true);
        mDeleteAddressDialog.setContentView(R.layout.delete_address_dialog);

        Button bt_sure = (Button) mDeleteAddressDialog.findViewById(R.id.bt_sure);
        Button bt_cancel = (Button) mDeleteAddressDialog.findViewById(R.id.bt_cancel);
        TextView tv_tip = (TextView) mDeleteAddressDialog.findViewById(R.id.tv_tip);
        tv_tip.setText("删除该条足迹");
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteTravel(position);
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

    private void deleteTravel(final int position){
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", GDUserInfoHelper.getInstance(mContext).getUserInfo().getUid());
        requestParams.put("travel_id",travelModes.get(position).getId());
        HttpUtil.postWithSign(GDUserInfoHelper.getInstance(mContext).getUserInfo().getToken(), IConstants.sDeleteTravel,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("删除我的足迹返回：",response.toString());
                if(response.optInt("ret")>=0){
                    PromptUtils.showToast("删除成功");
                    travelModes.remove(position);
                    flush(travelModes);
                }else{
                    PromptUtils.showToast(response.optString("msg"));
                }
            }
        });
    }

    /**
     * 点赞
     *
     * @param
     */
    public void LikeClick(TravelMode travelMode,boolean isLike) {
        if(checkLogin()){
            if(isLike == true){
                return;
            }
            likeSubmit(travelMode);
        }else{
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }


    }

    /**
     * 提交点赞
     * @param travelMode
     */
    private void likeSubmit(final TravelMode travelMode) {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", GDUserInfoHelper.getInstance(mContext).getUserInfo().getUid());
        requestParams.put("tid", travelMode.getId());
        HttpUtil.postWithSign(GDUserInfoHelper.getInstance(mContext).getUserInfo().getToken(), IConstants.sTravelLike, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("提交点赞返回：", response.toString());
                if(response.optInt("ret")>=0) {
                    travelMode.setLike_num((Integer.parseInt(travelMode.getLike_num()) + 1) + "");
                    travelMode.setIs_like("1");
                    flush();
                }

            }
        });

    }
    private boolean checkLogin(){
        if(GDUserInfoHelper.getInstance(mContext).getUserInfo() == null){
            PromptUtils.showToast("请先登录");
            return false;
        }
        return true;
    }
    /**
     * 刷新
     */
    private void flush(){
        this.notifyDataSetChanged();
    }
}
