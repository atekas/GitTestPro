package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/11/30.
 */
public class BuyReadingActivity extends BaseActivity {

    private WebView mWebView;
    private String mUrl = "http://www.sensu-sh.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_reading_activity);

        initViews();
    }

    private void initViews() {
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.loadUrl(mUrl);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        webSettings.setBuiltInZoomControls(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
