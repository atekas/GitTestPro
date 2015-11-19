package com.sensu.android.zimaogou.activity.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sensu.android.zimaogou.Mode.ProductMode;
import com.sensu.android.zimaogou.Mode.Warehouse;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.VerifyOrderActivity;
import com.sensu.android.zimaogou.utils.DisplayUtils;
import com.sensu.android.zimaogou.utils.PromptUtils;
import com.sensu.android.zimaogou.utils.UiUtils;
import com.sensu.android.zimaogou.widget.OnRefreshListener;
import com.sensu.android.zimaogou.widget.RefreshListView;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by zhangwentao on 2015/11/10.
 */
public class ShoppingBagFragment extends BaseFragment {
    RefreshListView mGoodsListView;
    TextView mFunctionTextView,mAmountTextView;
    ImageView mAllChooseImageView;
    LinearLayout mBottomCenterLinearLayout;
    Button mSubmitButton;
    boolean isEdit = false;//是否为编辑状态 默认为否
    ArrayList<Warehouse> mWarehouses = new ArrayList<Warehouse>();
    GoodsAdapter goodsAdapter = new GoodsAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_bag_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        } else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mWarehouses.size() == 0) {
            setData();
        }
    }

    @Override
    protected void initView() {
        mGoodsListView = (RefreshListView) mParentActivity.findViewById(R.id.lv_goods);
        mAllChooseImageView = (ImageView) mParentActivity.findViewById(R.id.img_allChoose);
        mAmountTextView = (TextView) mParentActivity.findViewById(R.id.tv_amount);
        mBottomCenterLinearLayout = (LinearLayout) mParentActivity.findViewById(R.id.ll_bottom_center);
        mSubmitButton = (Button) mParentActivity.findViewById(R.id.bt_submit);
        mFunctionTextView = (TextView) mParentActivity.findViewById(R.id.tv_function);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit){//编辑状态 则删除购物车物品
                    deleteData();
                    goodsAdapter.reFlush();
                }else{
                    PromptUtils.showToast("购买成功");
                    startActivity(new Intent(mParentActivity, VerifyOrderActivity.class));
                }
            }
        });

        mFunctionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit){
                    mFunctionTextView.setText("编辑");
                    isEdit = false;
                    mBottomCenterLinearLayout.setVisibility(View.VISIBLE);
                    mSubmitButton.setText("去结算");
                    setChooseStatus(true);
                    getAmountMoney();
                }else{
                    mFunctionTextView.setText("完成");
                    isEdit = true;
                    mBottomCenterLinearLayout.setVisibility(View.GONE);
                    mSubmitButton.setText("删除");
                    setChooseStatus(false);

                }
                goodsAdapter.reFlush();
            }
        });
        mGoodsListView.setAdapter(goodsAdapter);
        mGoodsListView.setDivider(null);
        mGoodsListView.setOnRefreshListener(mOnRefreshListener);
        setData();
        goodsAdapter.reFlush();
        getAmountMoney();
    }
    private void setData(){
        ArrayList<ProductMode> pros1 = new ArrayList<ProductMode>();
        ArrayList<ProductMode> pros2 = new ArrayList<ProductMode>();
        ProductMode mode1 = new ProductMode();
        mode1.setTestImg(R.drawable.product1);
        mode1.setTestTitle("懒人支架，给你最好的体验");
        mode1.setPrice(100);
        mode1.setNum(1);
        pros1.add(mode1);
        ProductMode mode2 = new ProductMode();
        mode2.setTestImg(R.drawable.product2);
        mode2.setPrice(101);
        mode2.setNum(1);
        mode2.setTestTitle("抹茶手工皂精油");
        pros1.add(mode2);

        ProductMode mode3 = new ProductMode();
        mode3.setTestImg(R.drawable.product3);
        mode3.setPrice(103);
        mode3.setNum(1);
        mode3.setTestTitle("YEASALAND——your best partner");
        pros2.add(mode3);
        ProductMode mode4 = new ProductMode();
        mode4.setTestImg(R.drawable.product1);
        mode4.setPrice(104);
        mode4.setNum(1);
        mode4.setTestTitle("可伤过的痛过的我，向谁述说");
        pros2.add(mode4);
        ProductMode mode5 = new ProductMode();
        mode5.setTestImg(R.drawable.product2);
        mode5.setPrice(105);
        mode5.setNum(1);
        mode5.setTestTitle("还是放开了说好不分的手");
        pros2.add(mode5);
        Warehouse warehouse1 = new Warehouse();
        warehouse1.setName("韩国保税仓发货");
        warehouse1.setPros(pros1);
        warehouse1.setChoose(true);
        Warehouse warehouse2 = new Warehouse();
        warehouse2.setName("韩国保税仓发货");
        warehouse2.setPros(pros2);
        warehouse2.setChoose(true);
        mWarehouses.add(warehouse1);
        mWarehouses.add(warehouse2);

    }

    /**
     *
     * 合计金额
     *
     */
    private void getAmountMoney(){
        double money = 0;
        for(Warehouse wh:mWarehouses){
            for(ProductMode pro: wh.getPros()){
                money += pro.getPrice()*pro.getNum();
            }
        }
        mAmountTextView.setText("￥"+money);
    }
    /**
     *
     * 设置数据的选择状态
     *
     */
    private void setChooseStatus(boolean isChoose){
        for(int i = 0 ; i < mWarehouses.size(); i++){
            mWarehouses.get(i).setChoose(isChoose);
        }
    }
    /**
     *
     * 检查提交按钮是否可点击
     *
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkClickAble(){
        boolean isClickAble =false;
        for(int i = 0; i < mWarehouses.size();i++){
            if(mWarehouses.get(i).isChoose()){
                isClickAble = true;
                break;
            }else{
                for(int j = 0 ; j < mWarehouses.get(i).getPros().size(); j ++){
                    if(mWarehouses.get(i).getPros().get(j).isChoose()){
                        isClickAble = true;
                        break;
                    }
                }
            }
        }
        if(isClickAble){
            mSubmitButton.setClickable(true);
            mSubmitButton.setBackground(getResources().getDrawable(R.drawable.account_button_selector));
        }else{
            mSubmitButton.setClickable(false);
            mSubmitButton.setBackgroundColor(getResources().getColor(R.color.account_disClick_color));
        }
    }
    /**
     *
     * 删除购物车中数据
     *
     */
    private void deleteData(){
        for(int i = 0; i < mWarehouses.size();i++){
            if(mWarehouses.get(i).isChoose()){
                mWarehouses.remove(i);
                continue;
            }else{
                for(int j = 0 ; j < mWarehouses.get(i).getPros().size(); j ++){
                    if(mWarehouses.get(i).getPros().get(j).isChoose()){
                        mWarehouses.get(i).getPros().remove(j);
                    }
                }
            }
        }
    }
    private Handler mHandler = new Handler();
    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onDownPullRefresh() {
            //下拉刷新接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoodsListView.hideHeaderView();
                }
            }, 2000);
        }

        @Override
        public void onLoadingMore() {
            //上拉加载接口
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mGoodsListView.hideFooterView();
                }
            }, 2000);
        }
    };
    /**
     *
     * 商品父列表布局
     *
     */
    class GoodsAdapter extends BaseAdapter{
        public void reFlush( ){
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mWarehouses.size();
        }

        @Override
        public Object getItem(int i) {
            return mWarehouses.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final GoodsViewHolder goodsViewHolder;
            if(view == null){
                goodsViewHolder = new GoodsViewHolder();
                view = LayoutInflater.from(mParentActivity).inflate(R.layout.shopping_list_item, null);
                goodsViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
                goodsViewHolder.tv_warehouseName = (TextView) view.findViewById(R.id.tv_warehouseName);
                goodsViewHolder.lv_products = (ListView) view.findViewById(R.id.lv_products);
                view.setTag(goodsViewHolder);
            }else{
                goodsViewHolder = (GoodsViewHolder) view.getTag();
            }
            final Warehouse warehouse = mWarehouses.get(i);
            final int position = i;
            final ProductsAdapter productsAdapter = new ProductsAdapter();
            goodsViewHolder.tv_warehouseName.setText(warehouse.getName());
            goodsViewHolder.lv_products.setAdapter(productsAdapter);
            goodsViewHolder.lv_products.setDivider(null);
            if(warehouse.isChoose()){
                goodsViewHolder.img_choose.setImageResource(R.drawable.d_03);
            }else{
                goodsViewHolder.img_choose.setImageResource(R.drawable.d_06);
            }
            goodsViewHolder.img_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(warehouse.isChoose()){
                        mWarehouses.get(position).setChoose(false);
                        goodsViewHolder.img_choose.setImageResource(R.drawable.d_06);
                        productsAdapter.reFlush(position,goodsViewHolder.img_choose);


                    }else{
                        mWarehouses.get(position).setChoose(true);
                        goodsViewHolder.img_choose.setImageResource(R.drawable.d_03);
                        productsAdapter.reFlush(position,goodsViewHolder.img_choose);


                    }
                }
            });
            productsAdapter.reFlush(position,goodsViewHolder.img_choose);
            UiUtils.setListViewHeightBasedOnChilds(goodsViewHolder.lv_products);

            return view;
        }
    }

    class GoodsViewHolder {
        public ImageView img_choose;
        public TextView tv_warehouseName;
        public ListView lv_products;
    }
    /**
     *
     * 商品子列表布局
     *
     */
    class ProductsAdapter extends BaseAdapter{
        int index;
        ImageView fatherChooseImage;
        public void reFlush(int position,ImageView fatherChooseImage){
            index = position;
            this.fatherChooseImage = fatherChooseImage;
            this.notifyDataSetChanged();
        }
        @Override
        public int getCount() {
            return mWarehouses.get(index).getPros().size();
        }

        @Override
        public Object getItem(int i) {
            return mWarehouses.get(index).getPros().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ProductsViewHolder productsViewHolder;
            if(view == null){
                productsViewHolder = new ProductsViewHolder();
                view = LinearLayout.inflate(mParentActivity,R.layout.shopping_list_child_item,null);
                productsViewHolder.et_productNum = (EditText) view.findViewById(R.id.et_productNum);
                productsViewHolder.img_choose = (ImageView) view.findViewById(R.id.img_choose);
                productsViewHolder.img_pro = (ImageView) view.findViewById(R.id.img_pro);
                productsViewHolder.ll_editNum = (LinearLayout) view.findViewById(R.id.ll_editNum);
                productsViewHolder.rl_showType = (RelativeLayout) view.findViewById(R.id.rl_showType);
                productsViewHolder.tv_productName = (TextView) view.findViewById(R.id.tv_productName);
                productsViewHolder.tv_productNum = (TextView) view.findViewById(R.id.tv_productNum);
                productsViewHolder.tv_productPrice = (TextView) view.findViewById(R.id.tv_productPrice);
                productsViewHolder.bt_subtract = (Button) view.findViewById(R.id.bt_subtract);
                productsViewHolder.bt_add = (Button) view.findViewById(R.id.bt_add);
                view.setTag(productsViewHolder);
            }else{
                productsViewHolder = (ProductsViewHolder) view.getTag();
            }
            final int position = i;
            if(isEdit){//如果是编辑状态
                productsViewHolder.rl_showType.setVisibility(View.GONE);
                productsViewHolder.ll_editNum.setVisibility(View.VISIBLE);


            }else{
                productsViewHolder.rl_showType.setVisibility(View.VISIBLE);
                productsViewHolder.ll_editNum.setVisibility(View.GONE);
            }
            if(mWarehouses.get(index).getPros().get(i).isChoose()){
                productsViewHolder.img_choose.setImageResource(R.drawable.d_03);
            }else{
                productsViewHolder.img_choose.setImageResource(R.drawable.d_06);
            }
            productsViewHolder.img_pro.setImageResource(mWarehouses.get(index).getPros().get(i).getTestImg());
            productsViewHolder.tv_productPrice.setText("￥" + mWarehouses.get(index).getPros().get(i).getPrice());
            productsViewHolder.tv_productName.setText(mWarehouses.get(index).getPros().get(i).getTestTitle());
            productsViewHolder.tv_productNum.setText("x"+mWarehouses.get(index).getPros().get(i).getNum());
            productsViewHolder.et_productNum.setText(mWarehouses.get(index).getPros().get(i).getNum()+"");
            productsViewHolder.img_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mWarehouses.get(index).getPros().get(position).isChoose()){
                        if(mWarehouses.get(index).getPros().size() == 1){
                            mWarehouses.get(index).setChoose(false);
                            fatherChooseImage.setImageResource(R.drawable.d_06);
                            productsViewHolder.img_choose.setImageResource(R.drawable.d_06);
                        }else{
                            mWarehouses.get(index).getPros().get(position).setChoose(false);
                            productsViewHolder.img_choose.setImageResource(R.drawable.d_06);
                        }
                    }else{
                        if(mWarehouses.get(index).getPros().size() == 1){
                            mWarehouses.get(index).setChoose(true);
                            fatherChooseImage.setImageResource(R.drawable.d_03);
                            productsViewHolder.img_choose.setImageResource(R.drawable.d_03);
                        }else{
                            mWarehouses.get(index).getPros().get(position).setChoose(true);
                            productsViewHolder.img_choose.setImageResource(R.drawable.d_03);

                        }
                    }

                }
            });

            productsViewHolder.bt_add.setOnClickListener(new View.OnClickListener() {//添加按钮
                @Override
                public void onClick(View view) {
                    mWarehouses.get(index).getPros().get(position).setNum(mWarehouses.get(index).getPros().get(position).getNum()+1);
                    productsViewHolder.et_productNum.setText(mWarehouses.get(index).getPros().get(position).getNum()+"");
                }
            });
            productsViewHolder.bt_subtract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWarehouses.get(index).getPros().get(position).setNum(mWarehouses.get(index).getPros().get(position).getNum()-1);
                    productsViewHolder.et_productNum.setText(mWarehouses.get(index).getPros().get(position).getNum()+"");
                }
            });
            if(mWarehouses.get(index).getPros().get(position).getNum() == 1){
                productsViewHolder.bt_subtract.setClickable(false);
                productsViewHolder.bt_subtract.setBackgroundResource(R.drawable.c_24);
            }else{
                productsViewHolder.bt_subtract.setClickable(true);
                productsViewHolder.bt_subtract.setBackgroundResource(R.drawable.c_26);
            }
            productsViewHolder.et_productNum.addTextChangedListener(new TextWatcher() {//编辑数量监听
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(productsViewHolder.et_productNum.getText()!=null&&!productsViewHolder.et_productNum.getText().toString().equals("")&&!productsViewHolder.et_productNum.getText().toString().trim().equals("0")){
                        mWarehouses.get(index).getPros().get(position).setNum(Integer.parseInt(productsViewHolder.et_productNum.getText().toString()));
                        productsViewHolder.bt_subtract.setClickable(false);
                        productsViewHolder.bt_subtract.setBackgroundResource(R.drawable.c_24);
                        if(mWarehouses.get(index).getPros().get(position).getNum()>1){
                            productsViewHolder.bt_subtract.setClickable(true);
                            productsViewHolder.bt_subtract.setBackgroundResource(R.drawable.c_26);
                        }
                    }else{
                        mWarehouses.get(index).getPros().get(position).setNum(1);
                        productsViewHolder.et_productNum.setText(mWarehouses.get(index).getPros().get(position).getNum() + "");
                        productsViewHolder.bt_subtract.setClickable(false);
                        productsViewHolder.bt_subtract.setBackgroundResource(R.drawable.c_24);

                    }

                }
            });
            return view;
        }
    }

    class ProductsViewHolder {
        public ImageView img_choose,img_pro;
        public TextView tv_productName,tv_productNum,tv_productPrice;
        public EditText et_productNum;
        public RelativeLayout rl_showType;
        public LinearLayout ll_editNum;
        public Button bt_subtract;
        public Button bt_add;
    }
}
