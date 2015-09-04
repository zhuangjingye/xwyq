package cc.horizoom.ssl.xwyq.boot;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewListUnLoginData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.login.LoginActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * 启动页，完成加载信息的工作
 */

public class BootActivity extends MyBaseActivity {

    private long startTim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
//        startLoginActivity();
        startTim = System.currentTimeMillis();
        String customerId = UserData.getInstance().getCustomerId(this);
        if (MyUtils.isEmpty(customerId)) {//未登录获取未登录的新闻列表
            unLoginPushContentList();
        } else {//已登录的话获取文章分类接口

        }
    }

    /**
     * 未登录新闻列表
     */
    private void unLoginPushContentList() {
        String url = Protocol.UNLOGINPUSHCONTENTLIST;
        doRequestString(url, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    if (success){
                        NewListUnLoginData.getInstance().clearData();
                        NewListUnLoginData.getInstance().saveData(BootActivity.this,str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadingFinish();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                loadingFinish();
            }
        });
    }

    /**
     * 加载结束
     */
    private void loadingFinish() {
        long currentTime = System.currentTimeMillis();
        long sleepTim = 1500-(currentTime-startTim);//当前要睡眠的时间，保证loading页面 停留3秒
        if (sleepTim > 0 ) {
            try {
                Thread.sleep(sleepTim);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String customerId = UserData.getInstance().getCustomerId(this);
        if (MyUtils.isEmpty(customerId)) {//未登录跳转到登录页
            startLoginActivity();
        } else {//已登录的话获取文章分类接口

        }
        closeActivity(BootActivity.class.getName());
    }

    /**
     * 打开登录页
     */
    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}
