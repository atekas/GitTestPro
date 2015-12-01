package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.adapter.RefundOrCommentAdapter;

/**
 * 申请退款 or 待评论商品页面
 * Created by qi.yang on 2015/12/1.
 */
public class RefundOrCommentActivity extends BaseActivity {

    ListView mGoodsListView;
    TextView mTitleTextView;
    private int type = 0;
    private ImageView mBackImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refundorcomment_activity);
        if(getIntent().getExtras() != null){
            type = getIntent().getExtras().getInt("type");
        }

        initView();
    }

    private void initView(){
        mGoodsListView = (ListView) findViewById(R.id.lv_goods);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mGoodsListView.setDivider(null);
        mGoodsListView.setAdapter(new RefundOrCommentAdapter(this,type));

        switch (type){
            case 0:
                mTitleTextView.setText("申请售后");
                break;
            case 1:
                mTitleTextView.setText("待评价");
                break;
        }
    }
}
