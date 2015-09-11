package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15-9-11.
 */
public class WarningParatmer implements DataInterface {
    private static WarningParatmer ourInstance = new WarningParatmer();

    public static WarningParatmer getInstance() {
        return ourInstance;
    }

    private WarningParatmer() {
    }

    private String key = "WarningParatmer";

    private String a_value;

    private String b_value;

    private String c_value;

    private String d_value;

    @Override
    public void saveData(BaseActivity baseActivity, String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    @Override
    public void clearData() {

    }

    @Override
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            a_value = jsonObject.optString("a_value");
            b_value = jsonObject.optString("b_value");
            c_value = jsonObject.optString("c_value");
            d_value = jsonObject.optString("d_value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {

    }

    public String getA_value(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(a_value)) {
            analyze(baseActivity);
        }
        return a_value;
    }

    public String getB_value(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(b_value)) {
            analyze(baseActivity);
        }
        return b_value;
    }

    public String getC_value(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(c_value)) {
            analyze(baseActivity);
        }
        return c_value;
    }

    public String getD_value(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(d_value)) {
            analyze(baseActivity);
        }
        return d_value;
    }
}
