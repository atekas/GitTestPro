package com.sensu.android.zimaogou.activity.mycenter;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import org.json.JSONException;
import org.json.JSONObject;

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
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
		mBackImageView = (ImageView) findViewById(R.id.back);
		mBackImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		findViewById(R.id.share).setOnClickListener(this);

		mTitleTextView.setText(title);
		webview = (WebView) findViewById(R.id.webView);
		
//		webview.setSaveEnabled(true);
//		webview.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
//		webview.getSettings().setLoadWithOverviewMode(true);
//		//webview_evacuation.getSettings().setUseWideViewPort(true);
//
//		 int   screenDensity   = this.getResources().getDisplayMetrics(). densityDpi ;
//           WebSettings.ZoomDensity   zoomDensity   = WebSettings.ZoomDensity. MEDIUM ;
//		      switch (screenDensity){
//		       case   DisplayMetrics.DENSITY_LOW :
//		           zoomDensity = WebSettings.ZoomDensity.CLOSE;
//		          break ;
//		      case   DisplayMetrics.DENSITY_MEDIUM :
//		            zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//		         break ;
//		        case   DisplayMetrics.DENSITY_HIGH :
//		           zoomDensity = WebSettings.ZoomDensity.FAR;
//		           break ;
//
//		       }
//
//		    //  webview_evacuation.setInitialScale(150);
//		 webview.getSettings().setDefaultZoom(zoomDensity);
//		 webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		 if(title.indexOf("用户协议")>=0){
			 URL= "http://139.196.108.137:80/v1/user/register_agreement";
			 webview.loadUrl(URL);
		 }else if(title.indexOf("关于")>= 0){
			 URL= "http://www.sensu-sh.com/";

			 URL = "http://"+getIntent().getExtras().getString("url");
			 webview.loadUrl(URL);
		 }else if(title.indexOf("诞生秘密") >= 0){
			 URL= "http://139.196.108.137:80/v1/user/shop_notice";

			 webview.loadUrl(URL);
		 }else if(title.indexOf("专题")>=0){
			 URL = getIntent().getExtras().getString("url");
			 webview.loadUrl(URL);
			 findViewById(R.id.share).setVisibility(View.VISIBLE);
		 }else{
			 URL = getIntent().getExtras().getString("url");
			 webview.loadUrl(URL);
			 findViewById(R.id.share).setVisibility(View.VISIBLE);
		 }
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		webSettings.setBuiltInZoomControls(true);
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
				UMImage image = new UMImage(WebViewActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
				new ShareAction(WebViewActivity.this).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
						.withText(title)
						.withTitle(title)
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
						.withText(title)
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
						.withText(title)
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
}
