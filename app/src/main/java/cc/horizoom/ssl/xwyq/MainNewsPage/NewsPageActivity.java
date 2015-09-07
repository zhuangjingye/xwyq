package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;

/**
 * Created by pizhuang on 2015/9/7.
 * 新闻显示页
 */
public class NewsPageActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout titleBackRl;//返回

    private TextView summaryTv;//摘要

    private TextView titleTv;//标题

    private WebView contentWebView;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);
        titleBackRl = (RelativeLayout) findViewById(R.id.titleBackRl);
        summaryTv = (TextView) findViewById(R.id.summaryTv);
        titleTv = (TextView) findViewById(R.id.titleTv);
        contentWebView = (WebView) findViewById(R.id.contentWebView);
        titleBackRl.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.titleBackRl:
                closeActivity(NewsPageActivity.class.getName());
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
}
