package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

/**
 * Created by zhangwentao on 2015/11/18.
 */
public class SearchGirdAdapter extends SimpleBaseAdapter {


    public SearchGirdAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder  = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.search_girdview_item, null);
            holder.img_right = (ImageView) view.findViewById(R.id.img_right);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if((i+1) % 3 == 0){
            holder.img_right.setVisibility(View.GONE);
        }else{
            holder.img_right.setVisibility(View.VISIBLE);
        }
        //todo 塞数据

        return view;
    }
    class ViewHolder{
        ImageView img_right;
    }

}
