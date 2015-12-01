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
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

public class HorizontalListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    Bitmap iconBitmap;
    private int selectIndex = -1;
    int Size = 1; //一屏显示多少个
    int Type = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
    ArrayList<ProductMode> pros = new ArrayList<ProductMode>();
    int width = DisplayUtils.getDisplayWidth();
    public HorizontalListViewAdapter(Context context, ArrayList<ProductMode> pros, int type) {
        this.mContext = context;
        this.pros = pros;
        if (type == 1) {
            this.Size = 3;
        }

        this.Type = type;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return pros.size();
    }

    @Override
    public Object getItem(int position) {
        return pros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DailyRecViewHolder holder;
        GroupSpeViewHolder gsHolder;
        if (Type == 1) {
            if (convertView == null) {
                holder = new DailyRecViewHolder();
                convertView = mInflater.inflate(R.layout.dailyrecommend_list_item, null);
                holder.mImage = (ImageView) convertView.findViewById(R.id.img_pro);
                holder.mTitle = (TextView) convertView.findViewById(R.id.tv_pro);
                holder.mRl_img = (RelativeLayout) convertView.findViewById(R.id.rl_img);

                holder.mSalePrice = (TextView) convertView.findViewById(R.id.tv_salePrice);
                holder.mOrigPrice = (TextView) convertView.findViewById(R.id.tv_origPrice);
                holder.mOrigPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                holder.mOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                holder.mOrigPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//抗锯齿

                DisplayMetrics metric = new DisplayMetrics();
                //获得屏幕宽度

                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.mRl_img.getLayoutParams();
                linearParams.width = (width - (DisplayUtils.dp2px((Size + 1 ) * 6))) / Size;
                linearParams.height = (width - (DisplayUtils.dp2px((Size + 1) * 6))) / Size;
                holder.mRl_img.setLayoutParams(linearParams);
                LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) holder.mTitle.getLayoutParams();
                linearParams1.width = (width - (DisplayUtils.dp2px((Size + 1) * 6))) / Size;
                holder.mTitle.setLayoutParams(linearParams1);
                if (position == selectIndex) {
                    convertView.setSelected(true);
                } else {
                    convertView.setSelected(false);
                }
                convertView.setTag(holder);
            } else {
                holder = (DailyRecViewHolder) convertView.getTag();
            }


            holder.mTitle.setText(pros.get(position).getTestTitle());
            holder.mImage.setImageResource(pros.get(position).getTestImg());
//		iconBitmap = getPropThumnail(mIconIDs[position]);
//		holder.mImage.setImageBitmap(iconBitmap);
//		holder.mImage.setim
        }else {
            if (Type == 2) {
                if (convertView == null) {
                    gsHolder = new GroupSpeViewHolder();
                    convertView = mInflater.inflate(R.layout.groupspecial_list_item, null);
                    gsHolder.mImage = (ImageView) convertView.findViewById(R.id.img_pro);
                    gsHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
                    gsHolder.mPersons = (TextView) convertView.findViewById(R.id.tv_persons);
                    gsHolder.mSalePrice = (TextView) convertView.findViewById(R.id.tv_salePrice);
                    gsHolder.mOrigPrice = (TextView) convertView.findViewById(R.id.tv_origPrice);
                    gsHolder.mOrigPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    gsHolder.mOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                    gsHolder.mOrigPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);//抗锯齿
                    convertView.setTag(gsHolder);
                }else{
                    gsHolder = (GroupSpeViewHolder) convertView.getTag();
                }

                gsHolder.mTitle.setText(pros.get(position).getTestTitle());
                gsHolder.mImage.setImageResource(pros.get(position).getTestImg());
            }

        }
        return convertView;
    }


    /**
     * 每日推荐ViewHolder
     */
    private static class DailyRecViewHolder {
        private TextView mTitle,mSalePrice,mOrigPrice;
        private ImageView mImage;
        private RelativeLayout mRl_img;
    }

    /**
     * 拼单特价ViewHolder
     */
    private static class GroupSpeViewHolder {
        private ImageView mImage;
        private TextView mPersons, mTitle, mSalePrice, mOrigPrice;
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