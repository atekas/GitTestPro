package com.sensu.android.zimaogou.activity.mycenter;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import com.sensu.android.zimaogou.R;
import com.sensu.android.zimaogou.activity.BaseActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * webview 展示页面
 * @author yangqi
 *
 */
public class WebViewActivity extends BaseActivity {


	WebView webview;
	ImageView mBackImageView;
	TextView mTitleTextView;
	String title ="";
	String URL = "http://www.sensu-sh.com/";
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
			if(title.indexOf("专题")>=0) {
				URL = getIntent().getExtras().getString("url");
			}
		}
		mTitleTextView = (TextView) findViewById(R.id.tv_title);
		mBackImageView = (ImageView) findViewById(R.id.back);
		mBackImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});


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
			 webview.loadUrl(URL);
		 }else if(title.indexOf("关于")>= 0){
			 URL = "http://"+getIntent().getExtras().getString("url");
			 webview.loadUrl(URL);
		 }else if(title.indexOf("广告") >= 0){
			 webview.loadUrl(URL);
		 }else if(title.indexOf("购物须知") >= 0){
			 webview.loadUrl(URL);
		 }else if(title.indexOf("专题")>=0){
			 webview.loadUrl(URL);
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

}
