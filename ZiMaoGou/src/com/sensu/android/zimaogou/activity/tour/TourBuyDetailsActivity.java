package com.sensu.android.zimaogou.activity.tour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.external.umeng.share.UmengShare;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zhangwentao on 2015/11/16.
 */
public class TourBuyDetailsActivity extends BaseActivity implements View.OnClickListener {

    private ListView mTourDetailsListView;
    private TourBuyDetailsAdapter mTourBuyDetailsAdapter;
    private UmengShare mUmengShare;

    private View mHeaderView;
    private RelativeLayout mBottomRelativeLayout;
    private Button mCommentSureButton,mCloseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tour_buy_details_activity);

        initViews();
    }

    private void initViews() {
        mUmengShare = UmengShare.getInstance(this);
        mTourDetailsListView = (ListView) findViewById(R.id.review_details);
        mBottomRelativeLayout = (RelativeLayout) findViewById(R.id.rl_bottom);
        mCommentSureButton = (Button) findViewById(R.id.bt_sure);
        mCloseButton = (Button) findViewById(R.id.bt_close);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tour_details_header, null);
        mTourDetailsListView.addHeaderView(mHeaderView);
        mTourBuyDetailsAdapter = new TourBuyDetailsAdapter(this);

        mTourDetailsListView.setAdapter(mTourBuyDetailsAdapter);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);

        mCommentSureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomRelativeLayout.setVisibility(View.GONE);
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomRelativeLayout.setVisibility(View.GONE);
            }
        });
        initHeader();
    }

    /**
     * 评论
     * @param v
     */
    public void CommentClick(View v){
        mBottomRelativeLayout.setVisibility(View.VISIBLE);
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
