package cc.horizoom.ssl.xwyq.login;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.NewListUnLoginData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.view.BounceListView;
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
        ArrayList<NewEntity> data = NewListUnLoginData.getInstance().getNewsData(this);
        newsListAdapter = new NewsListAdapter(this,data);
        newList.setAdapter(newsListAdapter);
        newList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
}
