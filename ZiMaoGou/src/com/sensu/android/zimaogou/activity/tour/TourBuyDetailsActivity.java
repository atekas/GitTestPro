package com.sensu.android.zimaogou.activity.tour;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.CommentMode;
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.widget.FixedAspectRatioFrameLayout;
import com.sensu.android.zimaogou.widget.RoundImageView;
import com.sensu.android.zimaogou.widget.VideoLinearLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 游购详情
 * Created by zhangwentao on 2015/11/16.
 */
public class TourBuyDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ListView mTourDetailsListView;
    private TourBuyDetailsAdapter mTourBuyDetailsAdapter;
    private LinearLayout mLikeUsersLinearLayout,mImageLinearLayout;
    private View mHeaderView;
    private RelativeLayout mBottomRelativeLayout;
    private Button mCommentSureButton, mCloseButton;
    private TravelMode travelMode = new TravelMode();
    private TextView mLikeNumTextView, mCommentNum, mLikeTextView,mFavoriteTextView,mUserNameTextView,mSendTimeTextView,mCityTextView,mBrowsersTextView,mContentTextView;
    private EditText mCommentEditText;
    private RoundImageView mUserHeadPicImageView;
    ArrayList<UserInfo> likeUsers = new ArrayList<UserInfo>();
    ArrayList<CommentMode> commentModes = new ArrayList<CommentMode>();
    private FixedAspectRatioFrameLayout mVideoFrameLayout;
    private VideoLinearLayout mVideoLinearLayout;
    UserInfo userInfo;
    boolean isLike = false;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tour_buy_details_activity);
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if (getIntent().getExtras() != null) {
            travelMode = (TravelMode) getIntent().getExtras().get("travel");
        }
        initViews();
    }

    private void initViews() {
        mTourDetailsListView = (ListView) findViewById(R.id.review_details);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.rl_bottom);
        mCommentEditText = (EditText) findViewById(R.id.et_comment);
        mLikeTextView = (TextView) findViewById(R.id.tv_like);
        mFavoriteTextView = (TextView) findViewById(R.id.tv_favorite);



        mCommentSureButton = (Button) findViewById(R.id.bt_sure);
        mCloseButton = (Button) findViewById(R.id.bt_close);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tour_details_header, null);
        mLikeUsersLinearLayout = (LinearLayout) mHeaderView.findViewById(R.id.ll_likeUser);
        mLikeNumTextView = (TextView) mHeaderView.findViewById(R.id.tv_likeNum);
        mCommentNum = (TextView) mHeaderView.findViewById(R.id.tv_commentNum);
        mUserNameTextView = (TextView) mHeaderView.findViewById(R.id.user_name);
        mSendTimeTextView = (TextView) mHeaderView.findViewById(R.id.send_time);
        mCityTextView = (TextView) mHeaderView.findViewById(R.id.tv_city);
        mBrowsersTextView = (TextView) mHeaderView.findViewById(R.id.tv_browsers);
        mContentTextView = (TextView) mHeaderView.findViewById(R.id.content_text);
        mUserHeadPicImageView = (RoundImageView) mHeaderView.findViewById(R.id.user_head_pic);

        mVideoFrameLayout = (FixedAspectRatioFrameLayout) mHeaderView.findViewById(R.id.video_frameLayout);
        mImageLinearLayout = (LinearLayout) mHeaderView.findViewById(R.id.ll_img);
        mVideoLinearLayout = (VideoLinearLayout) mHeaderView.findViewById(R.id.ll_video);
        mVideoFrameLayout.setVisibility(View.GONE);
        mImageLinearLayout.setVisibility(View.GONE);


        mTourDetailsListView.addHeaderView(mHeaderView);
        mTourBuyDetailsAdapter = new TourBuyDetailsAdapter(this);

        mTourDetailsListView.setAdapter(mTourBuyDetailsAdapter);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);

        mCommentSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentSubmit();
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomRelativeLayout.setVisibility(View.GONE);
            }
        });
        initHeader();
        getDataForLike();
        getDataForComment();
        showLoading();
    }

    /**
     * 更新点赞人用户头像UI
     */
    private void setLikeUsers() {
//        if(TextUtils.isEmpty(travelMode.getLike_num())||travelMode.getLike_num().equals("0")){
//            mLikeNumTextView.setVisibility(View.GONE);
//            return;
//        }else{
//            mLikeNumTextView.setText(travelMode.getLike_num());
//        }
        mLikeNumTextView.setText(travelMode.getLike_num());
        mLikeUsersLinearLayout.removeAllViews();
        for (int i = 0; i < likeUsers.size(); i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.roundimage_layout, null);
            RoundImageView roundImageView = (RoundImageView) v.findViewById(R.id.head_pic);
            ImageUtils.displayImage(likeUsers.get(i).getAvatar(), roundImageView,ImageUtils.mHeadDefaultOptions);
            mLikeUsersLinearLayout.addView(v);
        }
    }

    /**
     * 获取点赞人数据
     */
    private void getDataForLike() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", travelMode.getId());

        HttpUtil.get(IConstants.sGetTravelDetail + travelMode.getId() + "/likeravatars", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("游购点赞返回：", response.toString());
                JSONArray data = response.optJSONArray("data");
                if (data == null || data.length() == 0) {
                    return;
                }
                JSONObject item = null;
                likeUsers.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        item = (JSONObject) data.get(i);
                        UserInfo userInfo = new UserInfo();
                        userInfo = JSON.parseObject(item.toString(), UserInfo.class);
                        likeUsers.add(userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                setLikeUsers();
            }
        });
    }

    /**
     * 获取评论数据
     */
    private void getDataForComment() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", travelMode.getId());

        HttpUtil.get(IConstants.sGetTravelDetail + travelMode.getId() + "/comments", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("游购评论返回：", response.toString());
                commentModes.clear();
                cancelLoading();
                JSONArray data = response.optJSONArray("data");

                if (data == null || data.length() == 0) {

                    return;
                }
                JSONObject item = null;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        item = (JSONObject) data.get(i);
                        CommentMode commentMode = new CommentMode();
                        commentMode = JSON.parseObject(item.toString(), CommentMode.class);
                        commentModes.add(commentMode);
                        mTourBuyDetailsAdapter.flush(commentModes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mCommentNum.setText(commentModes.size() + "");
            }
        });
    }

    /**
     * 评论
     *
     * @param v
     */
    public void CommentClick(View v) {

        if (userInfo == null) {
            PromptUtils.showToast("请先登录");
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        mBottomRelativeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 提交评论
     */
    private void CommentSubmit() {
        if (TextUtils.isEmpty(mCommentEditText.getText().toString())) {
            PromptUtils.showToast("评论内容不能为空");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("tid", travelMode.getId());
        requestParams.put("content", mCommentEditText.getText().toString());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sTravelComment, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mBottomRelativeLayout.setVisibility(View.GONE);
                PromptUtils.showToast("评论成功");
                InputMethodManager imm;
                imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mCommentEditText.getWindowToken(),0);
                getDataForComment();//刷新评论
            }
        });

    }

    /**
     * 点赞
     *
     * @param v
     */
    public void LikeClick(View v) {
        if(checkLogin()){
            if(isLike == true){
                return;
            }
            likeSubmit();
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }


    }

    /**
     * 提交点赞
     */
    private void likeSubmit() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("tid", travelMode.getId());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sTravelLike, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("提交点赞返回：", response.toString());
                isLike = true;
                mLikeTextView.setSelected(true);
                travelMode.setLike_num((Integer.parseInt(travelMode.getLike_num())+1)+"");
                getDataForLike();
            }
        });

    }

    /**
     * 收藏
     * @param v
     */
    public void CollectClick(View v){
        if(checkLogin()){
            CollectSubmit();
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    /**
     * 提交收藏
     */
    private void CollectSubmit() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid", userInfo.getUid());
        requestParams.put("tid", travelMode.getId());
        HttpUtil.postWithSign(userInfo.getToken(), IConstants.sFavorite, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("提交收藏返回：", response.toString());
                isFavorite = !isFavorite;
                mFavoriteTextView.setSelected(isFavorite);
            }
        });

    }
    /**
     * 初始化详情界面
     */
    private void initHeader() {
        getTravelDetail();

    }

    /**
     * 获取游购详情
     */
    private void getTravelDetail() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", travelMode.getId());
        requestParams.put("userid", userInfo == null ? "0" : userInfo.getUid());
        HttpUtil.get(IConstants.sGetTravelDetail + travelMode.getId() , requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("游购详情返回：", response.toString());
                JSONArray data = response.optJSONArray("data");
                if (data == null || data.length() == 0) {

                    return;
                }
                JSONObject item = null;

                try {
                    item = (JSONObject) data.get(0);
                    travelMode = JSON.parseObject(item.toString(), TravelMode.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLike = travelMode.getIs_like().equals("1")?true:false;
                isFavorite = travelMode.getIs_favorite().equals("1")?true:false;
                mLikeTextView.setSelected(isLike);
                mFavoriteTextView.setSelected(isFavorite);
                ImageUtils.displayImage(travelMode.getAvatar(), mUserHeadPicImageView, ImageUtils.mHeadDefaultOptions);

                mUserNameTextView.setText(travelMode.getName());
                mSendTimeTextView.setText(DateUtils.getTimeAgo(travelMode.getCreated_at()));
                mCityTextView.setText(travelMode.getLocation());
                mBrowsersTextView.setText(travelMode.getBrowser_num()+"人看过");
                mContentTextView.setText(travelMode.getContent());

                if(travelMode.getCategory().equals("2")){
                    mVideoFrameLayout.setVisibility(View.VISIBLE);
                    mImageLinearLayout.setVisibility(View.GONE);
                    mVideoLinearLayout.setURL(travelMode.getMedia().cover,travelMode.getMedia().video);
                }else{
                    mVideoFrameLayout.setVisibility(View.GONE);
                    mImageLinearLayout.setVisibility(View.VISIBLE);
                    for(int i = 0; i < travelMode.getMedia().image.size(); i++){
                        View v = LayoutInflater.from(TourBuyDetailsActivity.this ).inflate(R.layout.tour_details_header_img_item,null);
                        final ImageView imageView = (ImageView) v.findViewById(R.id.content_pic);
                        ImageLoader.getInstance().displayImage(travelMode.getMedia().image.get(i), imageView, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            // TODO Auto-generated method stub

                                DisplayMetrics metric = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metric);
                                int pxWidth = metric.widthPixels;
                                float ratio = (float)pxWidth/(float)loadedImage.getWidth();
                                float imageHeight = loadedImage.getHeight()*ratio;

                                ViewGroup.LayoutParams lp = imageView.getLayoutParams();
                                lp.width = pxWidth;
                                lp.height = (int)imageHeight;
                                imageView.setLayoutParams(lp);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });
                        mImageLinearLayout.addView(v);
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                String imageUrl = "";
                if(travelMode.getCategory().equals("2")){
                    imageUrl = travelMode.getMedia().cover;
                }else{
                    imageUrl = travelMode.getMedia().image.get(0);
                }

                ShareDialog("足迹",travelMode.getContent(),imageUrl,"http://m.ftzgo365.com"+"/share/travel/"+travelMode.getId());
                break;
        }
    }

    private boolean checkLogin(){
        if(userInfo == null){
            PromptUtils.showToast("请先登录");
            return false;
        }
        return true;
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(TourBuyDetailsActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TourBuyDetailsActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TourBuyDetailsActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(TourBuyDetailsActivity.this).onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
    }

    /**
     * 分享
     */
    Dialog mShareDialog;

    private void ShareDialog(final String title, final String content,String imageUrl, final String targetUrl) {
        mShareDialog = new Dialog(this, R.style.dialog);
        mShareDialog.setCancelable(true);
        mShareDialog.setContentView(R.layout.share_dialog);
        LinearLayout ll_wx = (LinearLayout) mShareDialog.findViewById(R.id.ll_wx);
        LinearLayout ll_friends = (LinearLayout) mShareDialog.findViewById(R.id.ll_friends);
        LinearLayout ll_sina = (LinearLayout) mShareDialog.findViewById(R.id.ll_sina);
        final UMImage image = new UMImage(TourBuyDetailsActivity.this, imageUrl);

        ll_sina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new ShareAction(TourBuyDetailsActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText(content)
                        .withTitle(title)
                        .withTargetUrl(targetUrl)
                        .withMedia(image)
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(TourBuyDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText(content)
                        .withTitle(title)
                        .withTargetUrl(targetUrl)
                        .withMedia(image)
                        .share();
                mShareDialog.dismiss();
            }
        });
        ll_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShareAction(TourBuyDetailsActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withText(content)
                        .withTitle(title)
                        .withTargetUrl(targetUrl)
                        .withMedia(image)
                        .share();
                mShareDialog.dismiss();
            }
        });

        mShareDialog.show();
    }
}
