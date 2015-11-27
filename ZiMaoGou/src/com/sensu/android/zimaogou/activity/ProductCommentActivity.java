package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.tour.TourBuySendAdapter;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/11/27.
 */
public class ProductCommentActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSubmit;
    private GridView mGridView;
    private TourBuySendAdapter mTourBuySendAdapter;

    private boolean mIsShowName;

    private ImageView mImageStar1;
    private ImageView mImageStar2;
    private ImageView mImageStar3;
    private ImageView mImageStar4;
    private ImageView mImageStar5;

    private ImageView mImageShowName;

    private EditText mCommentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_comment_activity);

        initViews();
    }

    private void initViews() {
        mSubmit = (TextView) findViewById(R.id.submit);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mSubmit.setEnabled(false);

        mImageStar1 = (ImageView) findViewById(R.id.star_1);
        mImageStar2 = (ImageView) findViewById(R.id.star_2);
        mImageStar3 = (ImageView) findViewById(R.id.star_3);
        mImageStar4 = (ImageView) findViewById(R.id.star_4);
        mImageStar5 = (ImageView) findViewById(R.id.star_5);
        mImageShowName = (ImageView) findViewById(R.id.is_show_name);
        mCommentEdit = (EditText) findViewById(R.id.edit_comment);

        mCommentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //todo 判断是否有星星    如果没有星星不能提交   有星星有文字才可以点击
                if (TextUtils.isEmpty(charSequence.toString())) {
                    mSubmit.setEnabled(false);
                } else {
                    mSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mImageStar1.setOnClickListener(this);
        mImageStar2.setOnClickListener(this);
        mImageStar3.setOnClickListener(this);
        mImageStar4.setOnClickListener(this);
        mImageStar5.setOnClickListener(this);
        mImageShowName.setOnClickListener(this);

        mTourBuySendAdapter = new TourBuySendAdapter(this);
        mGridView.setAdapter(mTourBuySendAdapter);
        mTourBuySendAdapter.setList(new ArrayList());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.star_1:
                starBlack();
                mImageStar1.setSelected(true);
                break;
            case R.id.star_2:
                starBlack();
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                break;
            case R.id.star_3:
                starBlack();
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                break;
            case R.id.star_4:
                starBlack();
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                mImageStar4.setSelected(true);
                break;
            case R.id.star_5:
                starBlack();
                mImageStar1.setSelected(true);
                mImageStar2.setSelected(true);
                mImageStar3.setSelected(true);
                mImageStar4.setSelected(true);
                mImageStar5.setSelected(true);
                break;
            case R.id.is_show_name:
                if (mIsShowName) {
                    mIsShowName = false;
                } else {
                    mIsShowName = true;
                }
                mImageShowName.setSelected(mIsShowName);
                break;
        }
    }

    private void starBlack() {
        mImageStar1.setSelected(false);
        mImageStar2.setSelected(false);
        mImageStar3.setSelected(false);
        mImageStar4.setSelected(false);
        mImageStar5.setSelected(false);
    }
}
