package cc.horizoom.ssl.xwyq.setting.favoriteNews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsPageActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.deletelistview.ListViewCompat;

/**
 * Created by pizhuang on 2015/9/10.
 * 收藏新闻列表
 */
public class FavoriteNewsActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private RelativeLayout backRl;

    private ListViewCompat mListView;

    private FavoriteNewsAdapter favoriteNewsAdapter;

    private ArrayList<NewsEntity> newsEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news_activity);
        mListView = (ListViewCompat) findViewById(R.id.list);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        backRl.setOnClickListener(this);
        newsEntities = NewsListData.getInstance().getNewsData(this);
        favoriteNewsAdapter = new FavoriteNewsAdapter(this,newsEntities);
        mListView.setAdapter(favoriteNewsAdapter);
        mListView.setOnItemClickListener(this);
//        setListViewHeight(mListView);
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        BaseAdapter listAdapter = (BaseAdapter) listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRl:
                closeActivity(FavoriteNewsActivity.class.getName());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsEntity newsEntity = newsEntities.get(position);
        requestNews(newsEntity);
    }

    /**
     * 获取新闻
     * @param newsEntity
     */
    private void requestNews(NewsEntity newsEntity) {
        if (null == newsEntity) {
            return;
        }
        String url = Protocol.CPCD;
        HashMap<String,String> map = new HashMap<String,String>();
        String customer_id = UserData.getInstance().getCustomerId(this);
        String news_id = newsEntity.getNewsId();
        map.put("news_id",news_id);
        String font_size = Mysharedperferences.getIinstance().getString(this, MoreActivity.key);
        if (MyUtils.isEmpty(font_size)) {
            font_size = MoreActivity.SMALL;
        }
        map.put("font_size",font_size);
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
                        NewsData.getInstance(FavoriteNewsActivity.this).saveData(FavoriteNewsActivity.this, str);
                        Intent intent = new Intent();
                        intent.setClass(FavoriteNewsActivity.this, NewsPageActivity.class);
                        startActivity(intent);
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }
}
