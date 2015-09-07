package cc.horizoom.ssl.xwyq.DataManager;

//
//[
//        {
//        "success": "true",
//        "news_id": "9b1d6e50ec67ac081b57588d70881f77",
//        "is_secret": "true",
//        "type": "website",
//        "title": "贫困先心病患儿可到郑大三附院免费治疗",
//        "summary": "文章转自:网易新闻（原文：中原网-郑州晚报）",
//        "content": "<meta name=“viewport“ content=“width=device-width，initial-scale=1.0，maximum-scale=1.0，user-scalable=no“/><script language=“javascript“ src=“http：//yqzx.horizoom.cc/js/js.auto_resize_image.js“></script><div style=“font-size：22px;“>    贫困先心病患儿</p>可到郑大三附院免费治疗</p>近日，郑大三附院携手中国红十字基金会合生元母婴救助基金，将对符合条件的贫困家庭的先天性心脏病患儿实施免费救治。</p>此次免费救治主要针对年龄在1~14岁、新农合报销比例85%的农村贫困患儿。如果被确诊为室间隔缺损、房间隔缺损、动脉导管未闭、肺动脉狭窄4种简单的先天性心脏病，持有乡镇级贫困证明在郑大三附院即可申请享受免费救治。</p>此外，不符合免费救治条件的先心病患儿，来该院进行手术治疗也可申请合生元母婴救助基金的资助。新农合患儿除新农合报销外可获一次性资助金3000元，非新农合患儿可获一次性资助金8000元。</p>申请流程</p>符合条件的申请人提供相关资料领取申请表格完善申请表格递交医院进行审核通过拨款救助。如果审核未通过资料返还申请人。</p>郑州晚报记者 邢进 施杨</p>作者：邢进 施杨</p>    </div></div>",
//        "keywords": "",
//        "keywords_editor": "",
//        "is_favorite": "false",
//        "source_url": ""
//        }
//        ]

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pizhuang on 2015/9/7.
 */
public class NewsData implements DataInterface{
    private static NewsData ourInstance = new NewsData();

    public static NewsData getInstance(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(ourInstance.getContent())) {
            ourInstance.analyze(baseActivity);
        }
        return ourInstance;
    }

    private NewsData() {
    }

    private String key = NewsData.class.getName();

    private boolean success;

    private String news_id;

    private boolean is_secret;

    private String type;

    private String title;

    private String summary;

    private String content;

    private String keywords;

    private String keywords_editor;

    private boolean is_favorite;

    private String source_url;

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
            success = jsonObject.optBoolean("success");
            news_id = jsonObject.optString("news_id");
            is_secret = jsonObject.optBoolean("is_secret");
            type = jsonObject.optString("type");
            title = jsonObject.optString("title");
            summary = jsonObject.optString("summary");
            content = jsonObject.optString("content");
            keywords = jsonObject.optString("keywords");
            keywords_editor = jsonObject.optString("keywords_editor");
            is_favorite = jsonObject.optBoolean("is_favorite");
            source_url = jsonObject.optString("source_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {

    }

    public boolean isSuccess() {
        return success;
    }

    public String getNews_id() {
        return news_id;
    }

    public boolean is_secret() {
        return is_secret;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getKeywords_editor() {
        return keywords_editor;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public String getSource_url() {
        return source_url;
    }
}
