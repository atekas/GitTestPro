package com.sensu.android.zimaogou.activity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class HomePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_page_fragment, container, false);
    }
}
