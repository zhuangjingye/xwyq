package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.horizoom.ssl.xwyq.DataManager.MenuListData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.WarningParatmer;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.warning.WarningActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * 新闻菜单列表
 */
public class MenuListActivity extends MyBaseActivity implements View.OnClickListener {

    private MyBounceListview myListView;

    private MenuAdapter menuAdapter;

    private TextView waringNumTv;//重要新闻个数

    private RelativeLayout warningRl;//重要新闻入口

    private RelativeLayout waringNumRl;//个数布局

    private ImageView waringIv;//铃铛图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        myListView = (MyBounceListview) findViewById(R.id.myListView);

        waringNumTv = (TextView) findViewById(R.id.waringNumTv);
        warningRl = (RelativeLayout) findViewById(R.id.warningRl);
        waringNumRl = (RelativeLayout) findViewById(R.id.waringNumRl);
        waringIv = (ImageView) findViewById(R.id.waringIv);
        warningRl.setOnClickListener(this);
        ArrayList<CardEntity> menuData = MenuListData.getInstance().getMenuDatas(this);
        menuAdapter = new MenuAdapter(this,menuData);
        myListView.setAdapter(menuAdapter);
        customerMenuFunctionTopicList();
        updateView();
    }

    /**
     * 更新预警铃铛的显示
     */
    private void updateWaringView() {
        long num = 0;//预警消息总数
        ArrayList<CardEntity> menuData = MenuListData.getInstance().getMenuDatas(this);
        if (menuData != null && menuData.size() != 0) {
//            for (CardEntity cardEntity:menuData) {
//                if (cardEntity != null) {
//                    num += cardEntity.getWarning_push_content_nums();
//                }
//            }
            num = menuData.get(0).getWarning_push_content_nums();
        }
        if (num == 0) {
            waringNumRl.setVisibility(View.GONE);
            waringIv.setImageResource(R.mipmap.warning_bell);
        } else {
            waringNumRl.setVisibility(View.VISIBLE);
            waringIv.setImageResource(R.mipmap.warning_bell_red);
            if (num >= 99) {
                waringNumTv.setText("99");
            } else {
                waringNumTv.setText(num+"");
            }
        }
    }

    /**
     * 更新页面
     */
    private void updateView () {
        ArrayList<CardEntity> menuData = MenuListData.getInstance().getMenuDatas(this);
        menuAdapter.setData(menuData);
        updateWaringView();
    }

    /**
     * 菜单页数据获取
     */
    private void customerMenuFunctionTopicList() {
        String url = Protocol.CMFTL;
        String customer_id = UserData.getInstance().getCustomerId(this);
        if (TextUtils.isEmpty(customer_id)) return;
        Map<String,String> map = new HashMap<String,String>();
        map.put("customer_id", customer_id);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                hideDialog();
                MenuListData.getInstance().saveData(MenuListActivity.this, str);
                updateView();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.warningRl:
                requestWarningParameter();
                break;
        }
    }

    /**
     * 获得安全参数
     */
    private void requestWarningParameter() {
        String url = Protocol.CWP;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        showWaitDialog();
        doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        WarningParatmer.getInstance().saveData(MenuListActivity.this, str);
                        requestWarningNewList();
                    } else {
                        showToast(message);
                        hideWaitDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideWaitDialog();
                }
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 请求预警新闻列表
     */
    private void requestWarningNewList() {
        String url = Protocol.CWPCL;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("ht_id","all");
        map.put("page", "1");
        doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                NewsListData.getInstance().clearSaveData(MenuListActivity.this);
                NewsListData.getInstance().saveData(MenuListActivity.this, str);
                hideWaitDialog();
                startWaringActivity();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 打开舆情页面
     */
    private void startWaringActivity() {
        Intent intent = new Intent(this, WarningActivity.class);
        startActivity(intent);
    }

}
