package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15-9-3.
 * 未登录新闻列表
 */
public class NewsListData implements DataInterface{
    private static NewsListData ourInstance = new NewsListData();

    public static NewsListData getInstance() {
        return ourInstance;
    }

    private NewsListData() {
        if (null == newsData) {
            newsData = new ArrayList<NewsEntity>();
        }
    }


//    "success": "true",
//            "function_id": "37",
//            "page": "0",
//    "list": [

    private String key = NewsListData.class.getName();

    private String functionId = "all";

    private long page = -1;

    private String keyWord;//关键字

    private ArrayList<NewsEntity> newsData;
    /**
     * 保存数据
     * @param baseActivity
     * @param json
     */
    public void saveData(BaseActivity baseActivity,String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }
    /**
    * 保存数据
    * @param baseActivity
    * @param json
    */
    public void addData(BaseActivity baseActivity,String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);

        analyze(baseActivity);
    }

    /**
     * 清除缓存数据
     * @param baseActivity
     */
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        clearData();
    }

    /**
     * 清空数据
     */
    public void clearData() {
        if(newsData != null) {
            newsData.clear();
            functionId = "all";
            page = -1;
            keyWord = "";
        }
    }

    /**
     * 解析数据
     */
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            functionId = jsonObject.optString("ht_id","");
            page = jsonObject.optLong("page");
            JSONArray newsDataArray = jsonObject.optJSONArray("list");
            if (null == newsDataArray || newsDataArray.length() == 0) {
                newsDataArray = jsonObject.optJSONArray("result");
            }
            if (null == newsDataArray || newsDataArray.length() == 0)return;
            for (int i=0;i<newsDataArray.length();i++) {
                JSONObject jsonObject1 = newsDataArray.optJSONObject(i);
                NewsEntity newsEntity = new NewsEntity(jsonObject1.toString());
                newsData.add(newsEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFunctionId(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(functionId)) {
            analyze(baseActivity);
        }
        return functionId;
    }

    public long getPage(BaseActivity baseActivity) {
        if (page == -1) {
            analyze(baseActivity);
        }

        return page;
    }

    public ArrayList<NewsEntity> getNewsData(BaseActivity baseActivity) {
        if (newsData.size() == 0) {
            analyze(baseActivity);
        }
        return newsData;
    }

    public String getKeyWord() {
        if (MyUtils.isEmpty(keyWord)) {
            return "全部";
        }
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }
}
