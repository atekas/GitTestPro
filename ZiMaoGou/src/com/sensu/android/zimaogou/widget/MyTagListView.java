package com.sensu.android.zimaogou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.TravelTagMode;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qi.yang on 2015/12/23.
 */
public class MyTagListView extends FlowLayout {

    private List<TravelTagMode> mTravelTagModes = new ArrayList<TravelTagMode>();

    public MyTagListView(Context context) {
        super(context);
    }

    public MyTagListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MyTagListView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    public void setTravelTagModes(List<? extends TravelTagMode> lists) {
        setTravelTagModes(lists, false);
    }

    public void setTravelTagModes(List<? extends TravelTagMode> lists, boolean b) {
        removeAllViews();
        mTravelTagModes.clear();
        for (int i = 0; i < lists.size(); i++) {
            addTravelTagMode((TravelTagMode) lists.get(i), b);
        }
    }

    public void addTravelTagMode(TravelTagMode travelTagMode, boolean b) {
        mTravelTagModes.add(travelTagMode);
        inflateTravelTagModeView(travelTagMode, b);
    }

    private void inflateTravelTagModeView(TravelTagMode t, boolean b) {
        View localTravelTagModeView = View.inflate(getContext(),
                R.layout.travel_tag_list_item, null);
        final LinearLayout ll_tag = (LinearLayout) localTravelTagModeView.findViewById(R.id.food_layout);
        final TextView tv_title = (TextView) localTravelTagModeView.findViewById(R.id.food_text);
        final ImageView img_tag = (ImageView) localTravelTagModeView.findViewById(R.id.food_select);
        tv_title.setText(t.getName());
        ll_tag.setTag(t);
        ll_tag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TravelTagMode tagMode = (TravelTagMode) ll_tag.getTag();
                if (tagMode.isCheck()) {
                    ll_tag.setSelected(false);
                    tv_title.setSelected(false);
                    img_tag.setVisibility(View.GONE);
                    tagMode.setCheck(!tagMode.isCheck());
                } else {
                    ll_tag.setSelected(true);
                    tv_title.setSelected(true);
                    img_tag.setVisibility(View.VISIBLE);
                    tagMode.setCheck(!tagMode.isCheck());
                }
            }
        });

        addView(localTravelTagModeView);
    }
}
