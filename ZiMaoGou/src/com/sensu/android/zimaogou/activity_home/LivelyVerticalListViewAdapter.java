package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.LivelyMode;
import com.sensu.android.zimaogou.Mode.StoreMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

public class LivelyVerticalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;

    Bitmap iconBitmap;
    private int selectIndex = -1;
    int Size = 3;
    ArrayList<LivelyMode> lives = new ArrayList<LivelyMode>();

    public LivelyVerticalListViewAdapter(Context context, ArrayList<LivelyMode> lives) {
        this.mContext = context;
        this.lives = lives;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return lives.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DailyRecViewHolder holder;
            if (convertView == null) {
                holder = new DailyRecViewHolder();
                convertView = mInflater.inflate(R.layout.lively_list_item, null);
                holder.mImage = (ImageView) convertView.findViewById(R.id.img_lively);
                holder.mTitle = (TextView) convertView.findViewById(R.id.livelyName);
                holder.mLine = convertView.findViewById(R.id.line_bottom);
                holder.mLowPrice = (TextView) convertView.findViewById(R.id.tv_lowPrice);
                DisplayMetrics metric = new DisplayMetrics();
                int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.mImage.getLayoutParams();
                linearParams.width = (width - (DisplayUtils.dp2px(12 * 2)));
                linearParams.height = (width - (DisplayUtils.dp2px(12 * 2)))/12*5;

                holder.mImage.setLayoutParams(linearParams);

                convertView.setTag(holder);
            } else {
                holder = (DailyRecViewHolder) convertView.getTag();
            }
            if (position == selectIndex) {
                convertView.setSelected(true);
            } else {
                convertView.setSelected(false);
            }

        holder.mImage.setImageResource(lives.get(position).getTestImage());
        holder.mLowPrice.setText(lives.get(position).getLowPrice()+"元起");
        holder.mTitle.setText(lives.get(position).getTitle());
        if(position == lives.size() -1 ){
            holder.mLine.setVisibility(View.GONE);
        }else{
            holder.mLine.setVisibility(View.VISIBLE);
        }
//		iconBitmap = getPropThumnail(mIconIDs[position]);
//		holder.mImage.setImageBitmap(iconBitmap);
//		holder.mImage.setim

        return convertView;
    }



    private static class DailyRecViewHolder {
        private TextView mTitle,mLowPrice;
        private ImageView mImage;
        private View mLine;
    }



    //	private Bitmap getPropThumnail(int id){
//		Drawable d = mContext.getResources().getDrawable(id);
//		Bitmap b = BitmapUtil.drawableToBitmap(d);
////		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
//		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
//		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
//
//		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
//
//		return thumBitmap;
//	}
    public void setSelectIndex(int i) {
        selectIndex = i;
    }
}