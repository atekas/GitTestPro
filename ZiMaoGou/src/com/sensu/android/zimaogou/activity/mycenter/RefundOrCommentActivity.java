package com.sensu.android.zimaogou.activity.mycenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.MyOrderGoodsMode;
import com.sensu.android.zimaogou.Mode.MyOrderMode;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.ProductCommentActivity;
import com.sensu.android.zimaogou.adapter.SimpleBaseAdapter;
import com.sensu.android.zimaogou.utils.ImageUtils;

/**
 * 申请退款 or 待评论商品页面
 * Created by qi.yang on 2015/12/1.
 */
public class RefundOrCommentActivity extends BaseActivity {

    ListView mGoodsListView;
    TextView mTitleTextView;
    private int type = 0;
    private ImageView mBackImageView;
    MyOrderMode myOrderMode = new MyOrderMode();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refundorcomment_activity);
        if(getIntent().getExtras() != null){
            type = getIntent().getExtras().getInt("type");
            myOrderMode = (MyOrderMode) getIntent().getExtras().get("data");
        }

        initView();
    }

    private void initView(){
        mGoodsListView = (ListView) findViewById(R.id.lv_goods);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mBackImageView = (ImageView) findViewById(R.id.back);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mGoodsListView.setDivider(null);
        mGoodsListView.setAdapter(new RefundOrCommentAdapter(type,myOrderMode));

        switch (type){
            case 0:
                mTitleTextView.setText("申请售后");
                break;
            case 1:
                mTitleTextView.setText("待评价");
                break;
        }
    }

    private class RefundOrCommentAdapter extends BaseAdapter {
        private int type = 0;//跳转哪个页面
        MyOrderMode myOrderMode = new MyOrderMode();
        public RefundOrCommentAdapter(int type,MyOrderMode myOrderMode) {
            this.type = type;
            this.myOrderMode= myOrderMode;
        }

        @Override
        public int getCount() {
            return myOrderMode.getGoods().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                holder = new ViewHolder();
                view = LayoutInflater.from(RefundOrCommentActivity.this).inflate(R.layout.refundorcomment_item,null);
                holder.bt_submit = (Button) view.findViewById(R.id.bt_submit);

                holder.img_product = (ImageView) view.findViewById(R.id.img_pro);
                holder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
                holder.tv_productSpc = (TextView) view.findViewById(R.id.tv_spc);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            final MyOrderGoodsMode myOrderGoodsMode = myOrderMode.getGoods().get(i);
            holder.bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (type){
                        case 0:
                            startActivity(new Intent(RefundOrCommentActivity.this, ApplySalesAfterActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(RefundOrCommentActivity.this, ProductCommentActivity.class).putExtra("order", myOrderMode)
                                    .putExtra("goods",myOrderGoodsMode));
                            finish();
                            break;
                    }
                }
            });
            switch (type){
                case 1:
                    if(myOrderMode.getGoods().get(i).getIs_commented().equals("0")) {
                        holder.bt_submit.setText("评价");
                        holder.bt_submit.setBackgroundResource(R.drawable.bt_commit);
                        holder.bt_submit.setClickable(true);
                    }else{
                        holder.bt_submit.setText("已评价");
                        holder.bt_submit.setBackgroundResource(R.drawable.bt_discomment);
                        holder.bt_submit.setClickable(false);
                    }
                    break;
                case 0:
                    holder.bt_submit.setText("申请售后");
            }
            ImageUtils.displayImage(myOrderMode.getGoods().get(i).getImage(), holder.img_product, ImageUtils.mItemTopOptions);
            holder.tv_productSpc.setText(myOrderMode.getGoods().get(i).getSpec());
            holder.tv_productName.setText(myOrderMode.getGoods().get(i).getName());
            return view;
        }

        private class ViewHolder{
            public Button bt_submit;
            public ImageView img_product;
            public TextView tv_productName;
            public TextView tv_productSpc;
        }
    }

}
