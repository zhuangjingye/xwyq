package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15-9-3.
 * 用户信息管理
 */
public class UserData implements DataInterface{
    private static UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    private String key = UserData.class.getName();

    private String customerId;//用户id

    private String userName;//用户名

    private String realName;//用户全名

    /**
     * 保存数据
     * @param baseActivity
     * @param json
     */
    public void saveData(BaseActivity baseActivity,String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    @Override
    public void clearData() {
    }

    /**
     * 解析数据
     */
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            customerId = jsonObject.optString("customer_id");
            userName = jsonObject.optString("username");
            realName = jsonObject.optString("realname");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        ourInstance = null;
    }

    public String getCustomerId(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return customerId;
    }

    public String getUserName(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(userName)) {
            analyze(baseActivity);
        }
        return userName;
    }

    public String getRealName(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(realName)) {
            analyze(baseActivity);
        }
        return realName;
    }
}
