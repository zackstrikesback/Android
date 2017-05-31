package com.zackstrikesback.imgur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=9df18c0b96fcb0d&response_type=REQUESTED_RESPONSE_TYPE&state=APPLICATION_STATE");
        myWebView.setWebViewClient(new WebViewClient());
    }
}
