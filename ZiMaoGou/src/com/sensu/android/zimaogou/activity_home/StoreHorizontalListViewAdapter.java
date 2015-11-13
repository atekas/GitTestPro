package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.StoreMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

public class StoreHorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    Bitmap iconBitmap;
    private int selectIndex = -1;
    int Size = 3;
    ArrayList<StoreMode> stors = new ArrayList<StoreMode>();

    public StoreHorizontalListViewAdapter(Context context, ArrayList<StoreMode> stores) {
        this.mContext = context;
        this.stors = stores;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return stors.size();
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
                convertView = mInflater.inflate(R.layout.findstore_list_item, null);
                holder.mImage = (ImageView) convertView.findViewById(R.id.img_pro);
                holder.mTitle = (TextView) convertView.findViewById(R.id.tv_pro);
                holder.mRl_img = (RelativeLayout) convertView.findViewById(R.id.rl_img);
                holder.mCountryImage = (ImageView) convertView.findViewById(R.id.img_country);
                holder.mCountryName = (TextView) convertView.findViewById(R.id.tv_countryName);
                DisplayMetrics metric = new DisplayMetrics();
                int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.mRl_img.getLayoutParams();
                linearParams.width = (width - (DisplayUtils.dp2px((Size + 1) * 6))) / Size;
                linearParams.height = (width - (DisplayUtils.dp2px((Size + 1) * 6))) / Size;
                holder.mRl_img.setLayoutParams(linearParams);
                LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) holder.mTitle.getLayoutParams();
                linearParams1.width = (width - (DisplayUtils.dp2px((Size + 1) * 6))) / Size;
                holder.mTitle.setLayoutParams(linearParams1);

                convertView.setTag(holder);
            } else {
                holder = (DailyRecViewHolder) convertView.getTag();
            }
            if (position == selectIndex) {
                convertView.setSelected(true);
            } else {
                convertView.setSelected(false);
            }

            holder.mTitle.setText(stors.get(position).getStoreName());
            holder.mImage.setImageResource(stors.get(position).getTestStoreImage());
            holder.mCountryImage.setImageResource(stors.get(position).getTestCountryImage());
            holder.mCountryName.setText(stors.get(position).getTestCountryName());
//		iconBitmap = getPropThumnail(mIconIDs[position]);
//		holder.mImage.setImageBitmap(iconBitmap);
//		holder.mImage.setim

        return convertView;
    }



    private static class DailyRecViewHolder {
        private TextView mTitle,mCountryName;
        private ImageView mImage,mCountryImage;
        private RelativeLayout mRl_img;
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