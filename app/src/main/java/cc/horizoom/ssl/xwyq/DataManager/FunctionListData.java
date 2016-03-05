package cc.horizoom.ssl.xwyq.DataManager;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.FunctionEntity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pizhuang on 2015/9/6.
 */
public class FunctionListData implements DataInterface {
    private static FunctionListData ourInstance = new FunctionListData();

    public static FunctionListData getInstance() {
        return ourInstance;
    }

    private FunctionListData() {
        data = new ArrayList<FunctionEntity>();
    }

    private String key = FunctionListData.class.getName();

    private ArrayList<FunctionEntity> data;

    @Override
    public void saveData(BaseActivity baseActivity, String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    public ArrayList<FunctionEntity> getData(BaseActivity baseActivity) {
        if (data.size() == 0) {
            analyze(baseActivity);
        }
        return data;
    }

    @Override
    public void clearData() {
        data.clear();
    }

    @Override
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            JSONArray jsonList = jsonObject.optJSONArray("list");
            for (int i=0;i<jsonList.length();i++) {
                JSONObject jsonObject1 = jsonList.optJSONObject(i);
                FunctionEntity functionEntity = new FunctionEntity(jsonObject1.toString());
                data.add(functionEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        data.clear();
    }

    /**
     *  判断一个字符串是否是默认的关键字
     * @param str
     * @return
     */
    public boolean isFunctionStr(String str) {
        if (TextUtils.equals("全部",str)) {
            return true;
        }
        if (data!=null && data.size()>0) {
            for (int i=0;i<data.size();i++) {
                FunctionEntity functionEntity = data.get(i);
                if (TextUtils.equals(functionEntity.getName(),str)) {
                    return true;
                }
            }

        }
        return false;
    }
}
