package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15-9-3.
 * 未登录新闻列表
 */
public class NewListUnLoginData implements DataInterface{
    private static NewListUnLoginData ourInstance = new NewListUnLoginData();

    public static NewListUnLoginData getInstance() {
        return ourInstance;
    }

    private NewListUnLoginData() {
        if (null == newsData) {
            newsData = new ArrayList<NewEntity>();
        }
    }


//    "success": "true",
//            "function_id": "37",
//            "page": "0",
//    "list": [

    private String key = NewListUnLoginData.class.getName();

    private String functionId;

    private long page = -1;

    private ArrayList<NewEntity> newsData;
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
     * 清除缓存数据
     * @param baseActivity
     */
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        ourInstance = null;
    }

    /**
     * 清空数据
     */
    public void clearData() {
        if(newsData != null) {
            newsData.clear();
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
            functionId = jsonObject.optString("function_id");
            page = jsonObject.optInt("page");
            JSONArray newsDataArray = jsonObject.optJSONArray("list");
            for (int i=0;i<newsDataArray.length();i++) {
                JSONObject jsonObject1 = newsDataArray.optJSONObject(i);
                NewEntity newEntity = new NewEntity(jsonObject1.toString());
                newsData.add(newEntity);
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

    public ArrayList<NewEntity> getNewsData(BaseActivity baseActivity) {
        if (newsData.size() == 0) {
            analyze(baseActivity);
        }
        return newsData;
    }
}
