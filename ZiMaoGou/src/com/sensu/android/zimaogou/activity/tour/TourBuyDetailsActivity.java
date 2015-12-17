package com.sensu.android.zimaogou.activity.tour;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.Mode.CommentMode;
import com.sensu.android.zimaogou.Mode.TravelMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.login.LoginActivity;
import com.sensu.android.zimaogou.external.greendao.helper.GDUserInfoHelper;
import com.sensu.android.zimaogou.external.greendao.model.UserInfo;
import com.sensu.android.zimaogou.external.umeng.share.UmengShare;
import com.sensu.android.zimaogou.utils.*;
import com.sensu.android.zimaogou.widget.RoundImageView;
import com.umeng.socialize.bean.SHARE_MEDIA;
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
    private UmengShare mUmengShare;
    private LinearLayout mLikeUsersLinearLayout;
    private View mHeaderView;
    private RelativeLayout mBottomRelativeLayout;
    private Button mCommentSureButton,mCloseButton;
    private TravelMode travelMode;
    private TextView mLikeNumTextView,mCommentNum,mLikeTextView;
    private EditText mCommentEditText;
    ArrayList<UserInfo> likeUsers = new ArrayList<UserInfo>();
    ArrayList<CommentMode> commentModes = new ArrayList<CommentMode>();
    UserInfo userInfo ;
    boolean isLike = false;
    boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tour_buy_details_activity);
        userInfo = GDUserInfoHelper.getInstance(this).getUserInfo();
        if(getIntent().getExtras() != null){
            travelMode = (TravelMode) getIntent().getExtras().get("travel");
        }
        travelMode.setId("13");
        initViews();
    }

    private void initViews() {
        mUmengShare = UmengShare.getInstance(this);
        mTourDetailsListView = (ListView) findViewById(R.id.review_details);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.rl_bottom);
        mCommentEditText = (EditText) findViewById(R.id.et_comment);
        mLikeTextView = (TextView) findViewById(R.id.tv_like);


        mCommentSureButton = (Button) findViewById(R.id.bt_sure);
        mCloseButton = (Button) findViewById(R.id.bt_close);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tour_details_header, null);
        mLikeUsersLinearLayout = (LinearLayout) mHeaderView.findViewById(R.id.ll_likeUser);
        mLikeNumTextView = (TextView) mHeaderView.findViewById(R.id.tv_likeNum);
        mCommentNum = (TextView) mHeaderView.findViewById(R.id.tv_commentNum);

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
    }

    /**
     * 更新点赞人用户头像UI
     */
    private void setLikeUsers(){
//        if(TextUtils.isEmpty(travelMode.getLike_num())||travelMode.getLike_num().equals("0")){
//            mLikeNumTextView.setVisibility(View.GONE);
//            return;
//        }else{
//            mLikeNumTextView.setText(travelMode.getLike_num());
//        }
        mLikeNumTextView.setText(travelMode.getLike_num());
        for(int i = 0; i < likeUsers.size(); i++){
            View v = LayoutInflater.from(this).inflate(R.layout.roundimage_layout,null);
            RoundImageView roundImageView = (RoundImageView) v.findViewById(R.id.head_pic);
            ImageUtils.displayImage(likeUsers.get(i).getAvatar(),roundImageView);
            mLikeUsersLinearLayout.addView(v);
        }
    }
    /**
     * 获取点赞人数据
     */
    private void getDataForLike(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",travelMode.getId());

        HttpUtil.get(IConstants.sGetTravelDetail+travelMode.getId()+"/likeravatars",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("游购点赞返回：",response.toString());
                JSONArray data = response.optJSONArray("data");
                if(data == null || data.length() == 0){
                    return;
                }
                JSONObject item = null;
                for(int i = 0; i < data.length(); i++){
                    try {
                        item = (JSONObject) data.get(i);
                        UserInfo userInfo = new UserInfo();
                        userInfo = JSON.parseObject(item.toString(),UserInfo.class);
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
    private void getDataForComment(){
        RequestParams requestParams = new RequestParams();
        requestParams.put("id",travelMode.getId());

        HttpUtil.get(IConstants.sGetTravelDetail+travelMode.getId()+"/comments",requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("游购评论返回：",response.toString());
                commentModes.clear();
                JSONArray data = response.optJSONArray("data");
                if(data == null || data.length() == 0){

                    return;
                }
                JSONObject item = null;
                for(int i = 0; i < data.length(); i++){
                    try {
                        item = (JSONObject) data.get(i);
                        CommentMode commentMode = new CommentMode();
                        commentMode = JSON.parseObject(item.toString(),CommentMode.class);
                        commentModes.add(commentMode);
                        mTourBuyDetailsAdapter.flush(commentModes);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mCommentNum.setText(commentModes.size()+"");
            }
        });
    }
    /**
     * 评论
     * @param v
     */
    public void CommentClick(View v){
        if(userInfo == null){
            PromptUtils.showToast("请先登录");
            startActivity(new Intent(this, LoginActivity.class));
        }
        mBottomRelativeLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 提交评论
     */
    private void CommentSubmit(){
        if(TextUtils.isEmpty(mCommentEditText.getText().toString())){
            PromptUtils.showToast("评论内容不能为空");
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("tid",travelMode.getId());
        requestParams.put("content",mCommentEditText.getText().toString());
        HttpUtil.postWithSign(userInfo.getToken(),IConstants.sTravelComment,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                mBottomRelativeLayout.setVisibility(View.GONE);
                getDataForComment();//刷新评论
            }
        });

    }

    /**
     * 点赞
     * @param v
     */
    public void LikeClick(View v){
        likeSubmit();
    }
    /**
     * 提交点赞
     */
    private void likeSubmit(){

        RequestParams requestParams = new RequestParams();
        requestParams.put("uid",userInfo.getUid());
        requestParams.put("tid",travelMode.getId());
        HttpUtil.postWithSign(userInfo.getToken(),IConstants.sTravelLike,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.d("提交点赞返回：",response.toString());
                mLikeTextView.setSelected(true);
            }
        });

    }
    private void initHeader() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                mUmengShare.configPlatforms();
                mUmengShare.setShareContent();
                mUmengShare.mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA);
                mUmengShare.mController.openShare(this, false);
                break;
        }
    }
}
