package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.widget.GridView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.LocalPhotoActivity;
import com.sensu.android.zimaogou.activity.tour.TourBuySendAdapter;
import com.sensu.android.zimaogou.photoalbum.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qi.yang on 2015/11/27.
 */
public class ApplySalesAfterActivity extends BaseActivity {

    private GridView mGridView;
    private TourBuySendAdapter mTourBuySendAdapter;

    private List<PhotoInfo> mPhotoList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_after_sales_activity);
        initView();
    }

    private void initView(){
        mPhotoList = (List<PhotoInfo>) getIntent().getSerializableExtra(LocalPhotoActivity.SELECT_PHOTOS);

        if (mPhotoList == null) {
            mPhotoList = new ArrayList<PhotoInfo>();
        }

        mGridView = (GridView) findViewById(R.id.grid_view);
        mTourBuySendAdapter = new TourBuySendAdapter(this);
        mGridView.setAdapter(mTourBuySendAdapter);
        mTourBuySendAdapter.setList(mPhotoList);
    }
}
