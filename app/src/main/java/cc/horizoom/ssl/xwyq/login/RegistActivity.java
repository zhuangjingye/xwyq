package cc.horizoom.ssl.xwyq.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.dialog.ApplyDialog;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pi on 15-9-2.
 * 注册页面
 */
public class RegistActivity extends MyBaseActivity implements View.OnClickListener{

    private RelativeLayout backRl;//返回键

    private TextView titleTv;//标题

    private EditText nameEt;//姓名输入

    private EditText companyEt;//公司名称

    private EditText telEt;//电话

    private EditText emailEt;//电子邮件

    private Button submitRegistBtn;//注册按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        backRl = (RelativeLayout) findViewById(R.id.backRl);//返回键
        titleTv = (TextView) findViewById(R.id.titleTv);//标题
        nameEt = (EditText) findViewById(R.id.nameEt);//姓名输入
        companyEt = (EditText) findViewById(R.id.companyEt);//公司名称
        telEt = (EditText) findViewById(R.id.telEt);//电话
        emailEt = (EditText) findViewById(R.id.emailEt);//电子邮件
        submitRegistBtn = (Button) findViewById(R.id.submitRegistBtn);//注册按钮
        backRl.setOnClickListener(this);
        submitRegistBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backRl:
                closeActivity(RegistActivity.class.getName());
                break;
            case R.id.submitRegistBtn:
                submitRegistBtn();
                break;
        }
    }

    /**
     * 请求注册
     */
    private void submitRegistBtn() {
        String name = nameEt.getText().toString();
        String company = companyEt.getText().toString();
        String tel = telEt.getText().toString();
        String email = emailEt.getText().toString();
        if (MyUtils.isEmpty(name.trim())) {
            showToast(R.string.name_err);
            return;
        }
        if (MyUtils.isEmpty(company.trim())) {
            showToast(R.string.company_err);
            return;
        }
        if (MyUtils.isEmpty(tel.trim())) {
            showToast(R.string.tel_err);
            return;
        }
        if (MyUtils.isEmpty(email.trim())) {
            showToast(R.string.email_err);
            return;
        }
        String url = Protocol.APPLYACCOUNT;
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("name",name);
        map.put("company",company);
        map.put("mobile",tel);
        map.put("email",email);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                MyUtils.log(RegistActivity.class, "str=" + str);
                hideWaitDialog();
                onRequestSuccessed(str);

            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideDialog();
            }
        });
    }

    /**
     * 请求成功处理
     * @param str
     */
    private void onRequestSuccessed(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            String message = jsonObject.getString("message");
            boolean success = jsonObject.getBoolean("success");
            showAppleyDialog(success,message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示结果框
     */
    private void showAppleyDialog(boolean result,String message) {
        ApplyDialog applyDialog = new ApplyDialog(this,false,message);
        showDialog(applyDialog);
    }
}
