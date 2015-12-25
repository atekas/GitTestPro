package com.sensu.android.zimaogou.activity_home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.ReqResponse.CommendProductResponse;
import com.sensu.android.zimaogou.ReqResponse.GroupBuyListResponse;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class HomeGroupAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    List<GroupBuyListResponse.GroupBuyListData> pros = new ArrayList<GroupBuyListResponse.GroupBuyListData>();
    int width = DisplayUtils.getDisplayWidth();
    int selectIndex = 0;
    public HomeGroupAdapter(Context context, List<GroupBuyListResponse.GroupBuyListData> pros) {
        this.mContext = context;
        this.pros = pros;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(pros.size()<5) {
            return pros.size();
        }else{
            return 5;
        }
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

        GroupSpeViewHolder gsHolder;

        if (convertView == null) {
            gsHolder = new GroupSpeViewHolder();
            convertView = mInflater.inflate(R.layout.groupspecial_list_item, null);
            gsHolder.mImage = (ImageView) convertView.findViewById(R.id.img_pro);
            gsHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_title);
            gsHolder.mPersons = (TextView) convertView.findViewById(R.id.tv_persons);
            gsHolder.mSalePrice = (TextView) convertView.findViewById(R.id.tv_salePrice);
            gsHolder.mOrigPrice = (TextView) convertView.findViewById(R.id.tv_origPrice);
            gsHolder.mOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            gsHolder.mOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            gsHolder.mOrigPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//抗锯齿
            convertView.setTag(gsHolder);
        } else {
            gsHolder = (GroupSpeViewHolder) convertView.getTag();
        }
        ImageUtils.displayImage(pros.get(position).media,gsHolder.mImage);
        gsHolder.mTitle.setText(pros.get(position).content);
        gsHolder.mPersons.setText(pros.get(position).max_num+"人成团");
        gsHolder.mSalePrice.setText(pros.get(position).price);
        gsHolder.mOrigPrice.setText("￥"+pros.get(position).price_market);
        gsHolder.mTitle.setText(pros.get(position).name);


        return convertView;
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