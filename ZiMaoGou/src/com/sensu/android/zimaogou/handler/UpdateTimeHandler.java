package com.sensu.android.zimaogou.handler;


import android.os.Message;
import android.widget.TextView;
import com.sensu.android.zimaogou.BaseApplication;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.login.RegisterActivity;

/**
 * Created by zhangwentao on 2015/11/13.
 */
public class UpdateTimeHandler extends SoftReferenceActivityHandler<RegisterActivity> {

    public final static int UPDATE_TIME_CODE = 0;
    public int mMaxTime = 120;

    public UpdateTimeHandler(RegisterActivity weakReference) {
        super(weakReference);
    }

    @Override
    protected void handlerMessage(RegisterActivity softReference, Message msg) {
        switch (msg.what) {
            case UPDATE_TIME_CODE:
                if (mMaxTime >0) {
                    mMaxTime--;
                    ((TextView) softReference.findViewById(R.id.get_auth_code)).setText(mMaxTime + "");
                    sendEmptyMessageDelayed(UPDATE_TIME_CODE, 1000);
                } else {
                    softReference.findViewById(R.id.get_auth_code).setEnabled(true);
                    ((TextView) softReference.findViewById(R.id.get_auth_code)).setText(
                            BaseApplication.getStr(R.string.get_auth_code)
                    );
                }
                break;
        }
    }
}
