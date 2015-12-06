package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.Protocol;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pizhuang on 2015/9/8.
 * 登录后的消息查询页
 */
public class LoginNewsActivity extends BaseMainNewsActivity {
    @Override
    public HashMap<String, String> getFunctionParameter() {
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        return map;
    }

    @Override
    public HashMap<String, String> getNewsListParameter() {
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        return map;
    }

    @Override
    public HashMap<String, String> getNewsParameter() {
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        return map;
    }

    @Override
    public String getNewsUrl() {
        return Protocol.CPCD;
    }

    @Override
    public String getNewsListUrl() {
        return Protocol.CPCL;
    }

    @Override
    public String getFunctionListUrl() {
        return Protocol.CFL;
    }

    @Override
    public String getCurrentClassName() {
        return LoginNewsActivity.class.getName();
    }

    @Override
    public void onUpdataSearchEt(final EditText searchEt) {
        searchEt.setEnabled(true);
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String key = searchEt.getText().toString();

                    NewsListData.getInstance().clearData();
                    requestCPCSL(key);
                }
                return false;
            }
        });
    }

    /**
     * 获取 关键字查询新闻列表
     * @param keyword
     */
    private void requestCPCSL(final String keyword){
        final String myKey;
        if (MyUtils.isEmpty(keyword)) {
            myKey = NewsListData.getInstance().getKeyWord();
        } else {
            myKey = keyword;
        }

        long page = NewsListData.getInstance().getPage(this);

        String url = Protocol.CPCSL;
        HashMap<String,String> map = new HashMap<String,String>();
        String customer_id = UserData.getInstance().getCustomerId(this);
        String fId = NewsListData.getInstance().getFunctionId(this);
        map.put("customer_id",customer_id);
        map.put("ht_id",fId);
        map.put("keyword",myKey);
        map.put("page",(page+1)+"");
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {

                        if ("没有更多内容了！".equals(message)) {
                            showToast(message);
                            hideWaitDialog();
                            return;
                        }

                        NewsListData.getInstance().setKeyWord(myKey);
                        NewsListData.getInstance().saveData(LoginNewsActivity.this, str);
                        newsAdapter.notifyDataSetChanged();
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    @Override
    protected void loadMore() {
        requestCPCSL("");
    }
}
