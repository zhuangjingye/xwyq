package cn.com.myframe.webVeiw;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by pi on 15-9-13.
 */
public class MyWebVeiwClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}