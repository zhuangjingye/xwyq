package cc.horizoom.ssl.xwyq.login;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.MainNewsPageActivity;
import cc.horizoom.ssl.xwyq.MainNewsPage.UnLoginNewsActivity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * 登录页
 */
public class LoginActivity extends MyBaseActivity implements View.OnClickListener{

    private MyBounceListview newList;//新闻列表

    private EditText nameEt;//名字输入框

    private EditText pwdEt;//密码输入框

    private Button longinBtn;//登录

    private Button registBtn;//注册

    private Button forgetBtn;//忘记密码

    private NewsListAdapter newsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        newList = (MyBounceListview) findViewById(R.id.newList);
        nameEt = (EditText) findViewById(R.id.nameEt);
        pwdEt = (EditText) findViewById(R.id.pwdEt);
        longinBtn = (Button) findViewById(R.id.loginBtn);
        registBtn = (Button) findViewById(R.id.registBtn);
        forgetBtn = (Button) findViewById(R.id.forgetBtn);
        longinBtn.setOnClickListener(this);
        registBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        updataView();
    }

    /**
     * 更新页面数据
     */
    private void updataView () {
        ArrayList<NewsEntity> data = NewsListData.getInstance().getNewsData(this);
        newsListAdapter = new NewsListAdapter(this,data);
        newList.setAdapter(newsListAdapter);
        newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startUnLoginActivity();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtn:
                loginRequest();
                break;
            case R.id.registBtn:
                startRegistActivity();
                break;
            case R.id.forgetBtn:
                startFrogetActivity();
                break;

        }
    }

    /**
     * 登录
     */
    private void loginRequest() {
        String name = nameEt.getText().toString();
        String pwd = pwdEt.getText().toString();
        String url = Protocol.LOGIN;
        if (MyUtils.isEmpty(name)) {
            showToast(R.string.name_err);
            return;
        }
        if (MyUtils.isEmpty(pwd)) {
            showToast(R.string.pwd_err);
            return;
        }
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("username",name);
        hashMap.put("password",pwd);
        showWaitDialog();
        doRequestString(url, hashMap, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        loginSuccessed(str);
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

    /**
     * 登录成功
     * @param json
     */
    private void loginSuccessed(String json) {
        UserData.getInstance().saveData(this,json);
        Intent intent = new Intent(this, MainNewsPageActivity.class);
        startActivity(intent);
        closeActivity(LoginActivity.class.getName());
    }


    /**
     * 打开注册页面
     */
    private void startRegistActivity() {
        Intent intent = new Intent();
        intent.setClass(this,RegistActivity.class);
        startActivity(intent);
    }

    /**
     * 打开忘记密码页面
     */
    private void startFrogetActivity() {

    }

    /**
     * 未登录新闻列表
     */
    private void startUnLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this,UnLoginNewsActivity.class);
        startActivity(intent);
    }
}
