package com.sensu.android.zimaogou.activity.video;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CameraActivity extends BaseActivity implements View.OnClickListener {

    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;

    private List<String> mVideoPathList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);

        findViewById(R.id.finish).setOnClickListener(this);

        if (mVideoPathList == null) {
            mVideoPathList = new ArrayList<String>();
        }

        /**
         * 逻辑   触摸按钮时开始录制   松开放弃录制并删除上次录制时存留下的路径
         *
         * 再次触摸按钮继续重复操作
         */


        mShootBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                            @Override
                            public void onRecordFinish() {
                                handler.sendEmptyMessage(1);
                            }
                        });
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mRecorderView.getTimeCount() > 1)
                            handler.sendEmptyMessage(1);
                        else {
                            if (mRecorderView.getVecordFile() != null) {
                                mRecorderView.getVecordFile().delete();
                            }
                            Toast.makeText(CameraActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mRecorderView.stop();
                    mVideoPathList.add(mRecorderView.getVecordFile().getAbsolutePath());
                    Toast.makeText(CameraActivity.this, "视频录制完成，地址" + mRecorderView.getVecordFile(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
        }
    }

    /**
     * 录制完成回调
     *
     * @author liuyinjun
     * @date 2015-2-9
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);

        public void OnShootFailure();
    }
}

