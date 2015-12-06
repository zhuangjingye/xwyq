package cc.horizoom.ssl.xwyq.boot;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.CardData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.MainNewsPage.MainNewsPageActivity;
import cc.horizoom.ssl.xwyq.MainNewsPage.MenuListActivity;
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
        startTim = System.currentTimeMillis();
        unLoginPushContentList();
//        requestCCCPCL();
    }

    /**
     * 未登录新闻列表
     */
    private void unLoginPushContentList() {
        String url = Protocol.UNLOGINPUSHCONTENTLIST;
        HashMap<String,String> hashMap = new HashMap<String,String>();
        doRequestString(url, hashMap, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    if (success) {
                        NewsListData.getInstance().clearData();
                        NewsListData.getInstance().saveData(BootActivity.this, str);
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
            Intent intent = new Intent(this, MenuListActivity.class);
            startActivity(intent);
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

//    /**
//     * 已登录用户卡片列表接口
//     */
//    private void requestCCCPCL() {
//        CardData.getInstance().clearSaveData(BootActivity.this);
//        String url = Protocol.CCCPCL;
//        HashMap<String,String> map = new HashMap<String,String>();
//        String customer_id = UserData.getInstance().getCustomerId(this);
//        if (MyUtils.isEmpty(customer_id)) {
//            return;
//        }
//        map.put("customer_id", customer_id);
//        doRequestString(url, map, new RequestResult() {
//            @Override
//            public void onResponse(String str) {
//                try {
//                    JSONArray jsonArray = new JSONArray(str);
//                    JSONObject jsonObject = jsonArray.optJSONObject(0);
//                    boolean success = jsonObject.optBoolean("success");
//                    String message = jsonObject.optString("message");
//                    if (success) {
//                        CardData.getInstance().saveData(BootActivity.this, str);
//                    } else {
//                        showToast(message);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onErrResponse(VolleyError error) {
//
//            }
//        });
//    }
}
