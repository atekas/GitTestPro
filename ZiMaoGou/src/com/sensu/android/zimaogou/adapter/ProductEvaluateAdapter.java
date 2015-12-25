package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.ProductCommentMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/11/26.
 *
 * 商品评论的adapter
 */
public class ProductEvaluateAdapter extends SimpleBaseAdapter {

    ArrayList<ProductCommentMode> commentModes = new ArrayList<ProductCommentMode>();
    public ProductEvaluateAdapter(Context context,ArrayList<ProductCommentMode> commentModes) {

        super(context);
        this.commentModes = commentModes;
    }

    @Override
    public int getCount() {
        return commentModes.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.product_evaluate_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mFrameLayout1 = (FrameLayout) view.findViewById(R.id.frame_1);
            viewHolder.mFrameLayout2 = (FrameLayout) view.findViewById(R.id.frame_2);
            viewHolder.mFrameLayout3 = (FrameLayout) view.findViewById(R.id.frame_3);
            viewHolder.mFrameLayout4 = (FrameLayout) view.findViewById(R.id.frame_4);
            viewHolder.mFrameLayout1.setVisibility(View.GONE);
            viewHolder.mFrameLayout2.setVisibility(View.GONE);
            viewHolder.mFrameLayout3.setVisibility(View.GONE);
            viewHolder.mFrameLayout4.setVisibility(View.GONE);
            viewHolder.mFrameLayouts.add(viewHolder.mFrameLayout1);
            viewHolder.mFrameLayouts.add(viewHolder.mFrameLayout2);
            viewHolder.mFrameLayouts.add(viewHolder.mFrameLayout3);
            viewHolder.mFrameLayouts.add(viewHolder.mFrameLayout4);

            viewHolder.mImageView1 = (ImageView) view.findViewById(R.id.photo_1);
            viewHolder.mImageView2 = (ImageView) view.findViewById(R.id.photo_2);
            viewHolder.mImageView3 = (ImageView) view.findViewById(R.id.photo_3);
            viewHolder.mImageView4 = (ImageView) view.findViewById(R.id.photo_4);
            viewHolder.mImageView1.setVisibility(View.GONE);
            viewHolder.mImageView2.setVisibility(View.GONE);
            viewHolder.mImageView3.setVisibility(View.GONE);
            viewHolder.mImageView4.setVisibility(View.GONE);


            viewHolder.commentImages.add(viewHolder.mImageView1);
            viewHolder.commentImages.add(viewHolder.mImageView2);
            viewHolder.commentImages.add(viewHolder.mImageView3);
            viewHolder.commentImages.add(viewHolder.mImageView4);

            viewHolder.mScoreImage1 = (ImageView) view.findViewById(R.id.img_score1);
            viewHolder.mScoreImage2 = (ImageView) view.findViewById(R.id.img_score2);
            viewHolder.mScoreImage3 = (ImageView) view.findViewById(R.id.img_score3);
            viewHolder.mScoreImage4 = (ImageView) view.findViewById(R.id.img_score4);
            viewHolder.mScoreImage5 = (ImageView) view.findViewById(R.id.img_score5);
            viewHolder.mScoreImage1.setVisibility(View.GONE);
            viewHolder.mScoreImage2.setVisibility(View.GONE);
            viewHolder.mScoreImage3.setVisibility(View.GONE);
            viewHolder.mScoreImage4.setVisibility(View.GONE);
            viewHolder.mScoreImage5.setVisibility(View.GONE);
            viewHolder.scoreImages.add(viewHolder.mScoreImage1);
            viewHolder.scoreImages.add(viewHolder.mScoreImage2);
            viewHolder.scoreImages.add(viewHolder.mScoreImage3);
            viewHolder.scoreImages.add(viewHolder.mScoreImage4);
            viewHolder.scoreImages.add(viewHolder.mScoreImage5);

            viewHolder.mLinearLayout = (LinearLayout) view.findViewById(R.id.photos_layout);

            viewHolder.mUserPhoto = (ImageView) view.findViewById(R.id.user_photo);
            viewHolder.mUserName = (TextView) view.findViewById(R.id.user_name);
            viewHolder.mEvaluateContent = (TextView) view.findViewById(R.id.evaluate_content);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ImageUtils.displayImage(commentModes.get(i).getUser_avatar(),viewHolder.mUserPhoto, ImageUtils.mHeadDefaultOptions);
        viewHolder.mUserName.setText(commentModes.get(i).getUser_name());
        viewHolder.mEvaluateContent.setText(commentModes.get(i).getContent());
        if(commentModes.get(i).getImage().size() == 0){
            viewHolder.mLinearLayout.setVisibility(View.GONE);
        }else{
            viewHolder.mLinearLayout.setVisibility(View.VISIBLE);
            for(int j = 0; j < commentModes.get(i).getImage().size(); j++){
                viewHolder.mFrameLayouts.get(j).setVisibility(View.VISIBLE);
                viewHolder.commentImages.get(j).setVisibility(View.VISIBLE);

                ImageUtils.displayImage(commentModes.get(i).getImage().get(j), viewHolder.commentImages.get(j));
            }

        }
        int score = 3;//测试分数，因目前返回分数都为0
        for(int k = 0; k < score; k++){
            viewHolder.scoreImages.get(k).setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class ViewHolder {
        FrameLayout mFrameLayout1;
        FrameLayout mFrameLayout2;
        FrameLayout mFrameLayout3;
        FrameLayout mFrameLayout4;

        ImageView mImageView1;
        ImageView mImageView2;
        ImageView mImageView3;
        ImageView mImageView4;
        ImageView mScoreImage1;
        ImageView mScoreImage2;
        ImageView mScoreImage3;
        ImageView mScoreImage4;
        ImageView mScoreImage5;
        ArrayList<ImageView> commentImages  = new ArrayList<ImageView>();
        ArrayList<ImageView> scoreImages = new ArrayList<ImageView>();
        ArrayList<FrameLayout> mFrameLayouts = new ArrayList<FrameLayout>();
        LinearLayout mLinearLayout;

        ImageView mUserPhoto;

        TextView mUserName;
        TextView mEvaluateContent;

    }
}
