package com.example.browser;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser);

        WebView webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); 
        webView.getSettings().setJavaScriptEnabled(true); 

        Uri data = getIntent().getData();
        if (data != null) {
            webView.loadUrl(data.toString());
        }
    }
}