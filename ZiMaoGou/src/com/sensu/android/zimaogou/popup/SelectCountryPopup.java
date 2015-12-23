package com.sensu.android.zimaogou.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.LandMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.adapter.CountryListAdapter;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/12/1.
 */
public class SelectCountryPopup extends BasePopupWindow implements View.OnClickListener {

    private ListView mCountryListView;
    private CountryListAdapter mCountryListAdapter;
    private OnViewClickListener mOnViewClickListener;
    ArrayList<LandMode> landModes ;
    public SelectCountryPopup(Context context,TextView tv_country,ArrayList<LandMode> landModes) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_country_pop, null);
        this.landModes = landModes;
        mCountryListView = (ListView) view.findViewById(R.id.country_list);
        mCountryListAdapter = new CountryListAdapter(context,new CountryListAdapter.OnCountrySelect() {
            @Override
            public void onCountrySelect() {
                dismiss();
            }
        }, tv_country,landModes);
        mCountryListView.setAdapter(mCountryListAdapter);
        view.findViewById(R.id.finish).setOnClickListener(this);

        setContentView(view);

        setWidth(DisplayUtils.getDisplayWidth());
        setHeight(DisplayUtils.getDisplayHeight());
        setTouchable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        mOnViewClickListener = onViewClickListener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                dismiss();
                break;
        }
    }

    public interface OnViewClickListener {

    }

}
