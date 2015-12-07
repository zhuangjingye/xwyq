package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.horizoom.ssl.xwyq.DataManager.MenuListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * 新闻菜单列表
 */
public class MenuListActivity extends MyBaseActivity {

    private MyBounceListview myListView;

    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        myListView = (MyBounceListview) findViewById(R.id.myListView);
        ArrayList<CardEntity> menuData = MenuListData.getInstance().getMenuDatas(this);
        menuAdapter = new MenuAdapter(this,menuData);
        myListView.setAdapter(menuAdapter);
        customerMenuFunctionTopicList();
    }

    /**
     * 更新页面
     */
    private void updateView () {
        ArrayList<CardEntity> menuData = MenuListData.getInstance().getMenuDatas(this);
        menuAdapter.setData(menuData);
    }

    /**
     * 重置推送状态接口
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

}
