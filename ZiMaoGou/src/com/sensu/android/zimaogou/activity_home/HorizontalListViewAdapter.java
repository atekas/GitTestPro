package com.sensu.android.zimaogou.activity_home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.utils.DisplayUtils;

import java.util.ArrayList;

public class HorizontalListViewAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	int Size = 1; //一屏显示多少个
	int Type = 1;//哪个部分调用 1，每日推荐 2，拼单特价，3.发现好店铺
	ArrayList<ProductMode> pros = new ArrayList<ProductMode>();

	public HorizontalListViewAdapter(Context context,ArrayList<ProductMode> pros,int size,int type){
		this.mContext = context;
		this.pros = pros;
		this.Size = size;
		this.Type = type;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return pros.size();
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

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_pro);
			holder.mTitle=(TextView)convertView.findViewById(R.id.tv_pro);
			holder.mRl_img = (RelativeLayout) convertView.findViewById(R.id.rl_img);
			DisplayMetrics metric = new DisplayMetrics();
			int width = DisplayUtils.getDisplayWidth();//获得屏幕宽度
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.mRl_img.getLayoutParams();
			linearParams.width = (width-(DisplayUtils.dp2px((Size+1)*6)))/Size;
			linearParams.height = (width-(DisplayUtils.dp2px((Size+1)*6)))/Size;
			holder.mRl_img.setLayoutParams(linearParams);
			LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) holder.mTitle.getLayoutParams();
			linearParams1.width = (width-(DisplayUtils.dp2px((Size+1)*6)))/Size;
			holder.mTitle.setLayoutParams(linearParams1);

			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		holder.mTitle.setText(pros.get(position).getTestTitle());
		holder.mImage.setImageResource(pros.get(position).getTestImg());
//		iconBitmap = getPropThumnail(mIconIDs[position]);
//		holder.mImage.setImageBitmap(iconBitmap);
//		holder.mImage.setim
		return convertView;
	}




	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
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
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}