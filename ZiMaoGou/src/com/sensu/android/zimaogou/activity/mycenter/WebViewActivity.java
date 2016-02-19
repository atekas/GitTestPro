package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.sensu.android.zimaogou.IConstants;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.sensu.android.zimaogou.activity.ProductDetailsActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * webview 展示页面
 * @author yangqi
 *
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {


	WebView webview;
	ImageView mBackImageView;
	TextView mTitleTextView;
	String title ="";
	String URL = "";
	String mComment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		if(getIntent().getExtras() != null){
			title = getIntent().getExtras().getString("title");
		}
		mComment = getIntent().getExtras().getString("comment");
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
		mBackImageView = (ImageView) findViewById(R.id.back);
		mBackImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		findViewById(R.id.share).setOnClickListener(this);

		mTitleTextView.setText(title);
		webview = (WebView) findViewById(R.id.webView);
		
		 if(title.indexOf("用户协议")>=0){
			 URL= IConstants.HOST + "user/register_agreement";
		 }else if(title.indexOf("关于")>= 0){
			 URL = getIntent().getExtras().getString("url");
		 }else if(title.indexOf("诞生秘密") >= 0){
			 URL= IConstants.HOST + "user/shop_notice";
		 }else if(title.indexOf("专题")>=0){
			 URL = getIntent().getExtras().getString("url");
			 findViewById(R.id.share).setVisibility(View.VISIBLE);
		 }else{
			 URL = getIntent().getExtras().getString("url");
			 findViewById(R.id.share).setVisibility(View.VISIBLE);
		 }
		webview.loadUrl(URL);

		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webSettings.setBuiltInZoomControls(true);
		webview.addJavascriptInterface(new Object() {
			@JavascriptInterface
			public void method(String str) {
				Intent intent = new Intent(WebViewActivity.this, ProductDetailsActivity.class);
				intent.putExtra(ProductDetailsActivity.PRODUCT_ID, str);
				startActivity(intent);
			}
		},  "js");
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.share:
				shareDialog();
				break;
		}
	}

	/**
	 * 分享
	 */
	Dialog mShareDialog;

	private void shareDialog() {
		mShareDialog = new Dialog(this, R.style.dialog);
		mShareDialog.setCancelable(true);
		mShareDialog.setContentView(R.layout.share_dialog);
		LinearLayout ll_wx = (LinearLayout) mShareDialog.findViewById(R.id.ll_wx);
		LinearLayout ll_friends = (LinearLayout) mShareDialog.findViewById(R.id.ll_friends);
		LinearLayout ll_sina = (LinearLayout) mShareDialog.findViewById(R.id.ll_sina);
		ll_sina.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
						.withText(title)
						.withTitle(mComment)
						.withTargetUrl(URL)
						.withMedia(new UMImage(WebViewActivity.this, R.drawable.zimaogou_icon))
						.share();
				mShareDialog.dismiss();
			}
		});
		ll_wx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
						.withTitle(title)
						.withText(mComment)
						.withTargetUrl(URL)
						.withMedia(new UMImage(WebViewActivity.this, R.drawable.zimaogou_icon))
						.share();
				mShareDialog.dismiss();
			}
		});
		ll_friends.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
						.withTitle(title)
						.withText(mComment)
						.withTargetUrl(URL)
						.withMedia(new UMImage(WebViewActivity.this, R.drawable.zimaogou_icon))
						.share();
				mShareDialog.dismiss();
			}
		});

		mShareDialog.show();
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(WebViewActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(WebViewActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(WebViewActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

//	public void method(String str) {
//		startActivity(new Intent(this, LoginActivity.class));
//	}
}
