package cc.horizoom.ssl.xwyq.DataManager.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by pizhuang on 2015/9/8.
 * 新闻卡
 */
public class CardEntity {

    private String function_id;

    private String name;

    private long warning_push_content_nums;

    private ArrayList<NewsEntity> newsData;

    public CardEntity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            function_id = jsonObject.optString("ht_id");
            name = jsonObject.optString("ht_title");
            warning_push_content_nums = jsonObject.optLong("warning_push_content_nums");
            newsData = new ArrayList<NewsEntity>();
            JSONArray news_list = jsonObject.optJSONArray("main_news_list");
            for (int i=0;i<news_list.length();i++) {
                JSONObject jsonObject1 = news_list.optJSONObject(i);
                NewsEntity newsEntity = new NewsEntity(jsonObject1.toString());
                newsData.add(newsEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public long getWarning_push_content_nums() {
        return warning_push_content_nums;
    }

    public String getFunction_id() {
        return function_id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<NewsEntity> getNewsData() {
        return newsData;
    }
}


//
//{ 数据格式
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
//        }]
//        },
