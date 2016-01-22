package com.sensu.android.zimaogou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sensu.android.zimaogou.Mode.MessageMode;
import com.sensu.android.zimaogou.R;

import java.util.ArrayList;

/**
 * Created by qi.yang on 2015/11/19.
 */
public class MessageAdapter extends SimpleBaseAdapter {
    ArrayList<MessageMode> messageModes = new ArrayList<MessageMode>();
    public MessageAdapter(Context context,ArrayList<MessageMode> messageModes) {
        super(context);
        this.messageModes = messageModes;
    }

    @Override
    public int getCount() {
        return messageModes.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.message_list_item, null);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_createTime = (TextView) view.findViewById(R.id.tv_createTime);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_title.setText(messageModes.get(i).getTitle());
        holder.tv_createTime.setText(messageModes.get(i).getCreated_at());
        holder.tv_content.setText(messageModes.get(i).getMsg());
        return view;
    }

    class ViewHolder{
        public TextView tv_title,tv_createTime,tv_content;
    }
}
