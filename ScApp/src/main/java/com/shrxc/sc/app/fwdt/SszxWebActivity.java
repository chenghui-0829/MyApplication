package com.shrxc.sc.app.fwdt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shrxc.sc.app.R;
import com.shrxc.sc.app.utils.SystemBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SszxWebActivity extends AppCompatActivity {

    @BindView(R.id.sszx_web_activity_webview)
    WebView webView;
    private String webUrl = "https://m.sporttery.cn/app/zf/fb/hotleague.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sszx_web);
        ButterKnife.bind(this);
        SystemBarUtil.SetStatusColor(this, R.drawable.app_title_bg);
        initView();
    }

    @OnClick(R.id.sszx_web_activity_back_icon)
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sszx_web_activity_back_icon:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    private void initView() {

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        // 设置支持缩放
        // ws.setBuiltInZoomControls(true);
        webView.loadUrl(webUrl);
        webView.setWebViewClient(new MyWebViewClient());
    }

    // Web视图
    private class MyWebViewClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
