package com.inhatc.android_webview;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView objWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        objWebView = (WebView)findViewById(R.id.webView1);
        objWebView.setWebViewClient(new Android_WebViewClient());
        objWebView.getSettings().setJavaScriptEnabled(true);
        objWebView.loadUrl("http://www.google.com");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private class Android_WebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            int keyCode = event.getKeyCode();
            if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && objWebView.canGoBack()) {
                objWebView.goBack();
                return true;
            }
            else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && objWebView.canGoForward()) {
                objWebView.goForward();
                return true;
            }
            return false;
        }
    }
}