package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15/12/5.
 * 卡片菜单数据
 */
public class MenuListData implements DataInterface {
    private static MenuListData ourInstance = new MenuListData();

    public static MenuListData getInstance() {
        return ourInstance;
    }

    private CardEntity selectedCardEntity;

    private MenuListData() {
        menuDatas = new ArrayList<CardEntity>();
    }


    private String customer_id;//用户id

    private ArrayList<CardEntity> menuDatas;//卡片数据

    private String key = MenuListData.class.getName();

    @Override
    public void saveData(BaseActivity baseActivity, String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    @Override
    public void clearData() {
        menuDatas.clear();
    }

    @Override
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            boolean success = jsonObject.optBoolean("success");
            if (!success) return;
            customer_id = jsonObject.optString("customer_id");
            JSONArray menuArray = jsonObject.optJSONArray("menu");
            menuDatas = new ArrayList<CardEntity>();
            for (int i=0;i<menuArray.length();i++) {
                JSONObject jsonObject1 = menuArray.optJSONObject(i);
                CardEntity cardEntity = new CardEntity(jsonObject1.toString());
                menuDatas.add(cardEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        ourInstance = new MenuListData();
    }

    public String getCustomer_id(BaseActivity baseActivity) {
        if(MyUtils.isEmpty(customer_id)) {
            analyze(baseActivity);
        }
        return customer_id;
    }

    public ArrayList<CardEntity> getMenuDatas(BaseActivity baseActivity) {
        if (menuDatas.size() == 0) {
            analyze(baseActivity);
        }
        return menuDatas;
    }

    public CardEntity getSelectedCardEntity() {
        return selectedCardEntity;
    }

    public void setSelectedCardEntity(CardEntity selectedCardEntity) {
        this.selectedCardEntity = selectedCardEntity;
    }
}
