package cc.horizoom.ssl.xwyq.DataManager;

import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pizhuang on 2015/9/8.
 * 卡片信息
 */
public class CardData implements DataInterface {
    private static CardData ourInstance = new CardData();

    public static CardData getInstance() {
        return ourInstance;
    }

    private CardData() {
        cardDatas = new ArrayList<CardEntity>();
    }


    private String customer_id;//用户id

    private ArrayList<CardEntity> cardDatas;//卡片数据

    private String key = CardData.class.getName();

    @Override
    public void saveData(BaseActivity baseActivity, String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    @Override
    public void clearData() {
        cardDatas.clear();
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
            JSONArray cardArray = jsonObject.optJSONArray("card");
            cardDatas = new ArrayList<CardEntity>();
            for (int i=0;i<cardArray.length();i++) {
                JSONObject jsonObject1 = cardArray.optJSONObject(i);
                CardEntity cardEntity = new CardEntity(jsonObject1.toString());
                cardDatas.add(cardEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        ourInstance = new CardData();
    }

    public String getCustomer_id(BaseActivity baseActivity) {
        if(MyUtils.isEmpty(customer_id)) {
            analyze(baseActivity);
        }
        return customer_id;
    }

    public ArrayList<CardEntity> getCardDatas(BaseActivity baseActivity) {
        if (cardDatas.size() == 0) {
            analyze(baseActivity);
        }
        return cardDatas;
    }
}


//[
//        {
//        "success": "true",
//        "customer_id": "49",
//        "card": [
//        {
//        "function_id": "28",
//        "name": "境外",
//        "news_list": [
//        {
//        "news_id": "329940",
//        "title": "“东方之星”救援已转入水下探摸： 船底被切割 总理向遇难者鞠躬",
//        "source": "",
//        "push_level": "4",
//        "publish_time": "2015-06-04 09:49:31",
//        "like_nums": "8",
//        "status": "0"
//        }
//        ]
//        }
//        ]
//        }
//        ]