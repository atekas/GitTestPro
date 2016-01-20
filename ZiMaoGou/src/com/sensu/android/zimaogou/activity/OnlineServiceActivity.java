package com.sensu.android.zimaogou.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.sensu.android.zimaogou.R;

/**
 * Created by zhangwentao on 2015/12/2.
 */
public class OnlineServiceActivity extends BaseActivity implements View.OnClickListener {
    private WebView mWebView;
    private String mUrl = "http://chat16.live800.com/live800/chatClient/chatbox.jsp?companyID=569294&configID=113486&jid=8998592194";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_service_activity);

        initViews();
    }

    private void initViews() {
        findViewById(R.id.back).setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
