package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pizhuang on 2015/9/7.
 * 新闻显示页
 */
public class NewsPageActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout titleBackRl;//返回

    private TextView summaryTv;//摘要

    private TextView titleTv;//标题

    private WebView contentWebView;//内容

    private CheckBox myCheckBox;//复选框

    private RelativeLayout myCheckBoxRl;//复选框布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        titleBackRl = (RelativeLayout) findViewById(R.id.titleBackRl);
        summaryTv = (TextView) findViewById(R.id.summaryTv);
        titleTv = (TextView) findViewById(R.id.titleTv);
        contentWebView = (WebView) findViewById(R.id.contentWebView);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.setWebViewClient(new MyWebViewClient());
        titleBackRl.setOnClickListener(this);
        myCheckBoxRl = (RelativeLayout) findViewById(R.id.myCheckBoxRl);
        myCheckBox = (CheckBox) findViewById(R.id.myCheckBox);
        myCheckBox.setOnClickListener(this);
        updataView();
    }

    /**
     * 更新页面数据
     */
    private void updataView() {
        String summary = NewsData.getInstance(this).getSummary();
        String title = NewsData.getInstance(this).getTitle();
        String content = NewsData.getInstance(this).getContent();
        if (MyUtils.isEmpty(summary)) {
            summaryTv.setVisibility(View.GONE);
        } else {
            summaryTv.setVisibility(View.VISIBLE);
            summaryTv.setText(summary);
        }
        titleTv.setText(title);
        writeFile("context.html", content);
        String path = "file://"+getFilesDir()+"/context.html";
        contentWebView.loadUrl(path) ;
//        contentWebView.loadData(content,"text/html", "UTF-8");
//        contentWebView.loadDataWithBaseURL("", content, "text/html","utf-8", null);
//        contentWebView.loadUrl("http://baidu.com");
        String customerId = UserData.getInstance().getCustomerId(this);
        updataCheckBox();
        if (MyUtils.isEmpty(customerId)) {
            myCheckBoxRl.setVisibility(View.GONE);
        } else {
            myCheckBoxRl.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 更新复选框数据
     */
    private void updataCheckBox() {
        boolean isfavorite = NewsData.getInstance(this).is_favorite();
        myCheckBox.setChecked(isfavorite);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.titleBackRl:
                closeActivity(NewsPageActivity.class.getName());
                break;
            case R.id.myCheckBox:
                requestFavorite();
                break;

        }
    }


    //写数据
    public void writeFile(String fileName,String writestr){
        try{
            FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);
            byte [] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //读数据
    public String readFile(String fileName) {
        String res="";
        try{
            FileInputStream fin = openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return res;

    }

    /**
     * 请求收藏
     */
    private void requestFavorite() {
        String url = Protocol.CFO;
        String customer_id = UserData.getInstance().getCustomerId(this);
        String product_id = "1";
        String indexes_id = NewsData.getInstance(this).getNews_id();
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("customer_id",customer_id);
        hashMap.put("product_id",product_id);
        hashMap.put("indexes_id",indexes_id);
        showWaitDialog();
        doRequestString(url, hashMap, new RequestResult() {
            @Override
            public void onResponse(String str) {
                hideWaitDialog();
                onRequestFavoriteSuccessed(str);
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 请求成功处理
     * @param str
     */
    private void onRequestFavoriteSuccessed(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            String message = jsonObject.getString("message");
            boolean success = jsonObject.getBoolean("success");
            if (success) {
                boolean is_favorite = jsonObject.optBoolean("is_favorite");
                NewsData.getInstance(this).setIs_favorite(this, is_favorite);
            }
            showToast(message);
            updataCheckBox();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            MyUtils.log(NewsPageActivity.class,"url="+url);
            view.loadUrl(url);
            return true;
        }
    }
}
