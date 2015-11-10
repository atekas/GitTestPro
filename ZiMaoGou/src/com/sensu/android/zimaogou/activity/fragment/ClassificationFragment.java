package com.sensu.android.zimaogou.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.PromptUtils;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class ClassificationFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.classification_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PromptUtils.showToast("classification------onActivityCreated");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
