package cc.horizoom.ssl.xwyq.DataManager.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pi on 15-9-3.
 * 新闻实体类
 */
public class NewEntity {

//    "news_id": "150870",
//            "title": "湖南临湘市长涉嫌吸毒被查 消息称其产生幻觉后报警",
//            "source": "",
//            "push_level": "5",
//            "publish_time": "0000-00-00 00:00:00",
//            "status": "0"

    private String newsId;

    private String title;

    private String source;

    private String pupushLevel;

    private String publishTime;

    private String status;

    public NewEntity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            newsId = jsonObject.optString("news_id");
            title = jsonObject.optString("title");
            source = jsonObject.optString("source");
            pupushLevel = jsonObject.optString("push_level");
            publishTime = jsonObject.optString("publish_time");
            status = jsonObject.optString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNewsId() {
        return newsId;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public String getPupushLevel() {
        return pupushLevel;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getStatus() {
        return status;
    }
}
