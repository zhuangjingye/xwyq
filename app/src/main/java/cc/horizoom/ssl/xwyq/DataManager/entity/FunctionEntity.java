package cc.horizoom.ssl.xwyq.DataManager.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pizhuang on 2015/9/6.
 * 分类功能
 */
public class FunctionEntity {

//    "function_id": "37",
//            "name": "头条热点",
//            "description": "公共-头条热点",
//            "is_public": "1"

    private String functionId;

    private String name;

    private String description;

    private boolean isPublic;

    public FunctionEntity(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            functionId = jsonObject.optString("function_id");
            name = jsonObject.optString("name");
            description = jsonObject.optString("description");
            isPublic = jsonObject.optBoolean("is_public");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFunctionId() {
        return functionId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
