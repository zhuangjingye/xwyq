package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.dialog.ApplyDialog;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pi on 15-9-9.
 * 反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout backRl;
    private Button commitBtn;//提交按钮
    private EditText feedbackEt;//意见输入框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        commitBtn = (Button) findViewById(R.id.commitBtn);
        feedbackEt = (EditText) findViewById(R.id.feedbackEt);
        commitBtn.setOnClickListener(this);
        backRl.setOnClickListener(this);
    }


    /**
     * 提交反馈意见
     */
    private void requestFeedBack() {
        String feedback_message = feedbackEt.getText().toString();
        if (MyUtils.isEmpty(feedback_message)) {
            showToast(R.string.feed_back_err);
            return;
        }
        String url = Protocol.CFB;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        map.put("feedback_message ",feedback_message);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                hideWaitDialog();
                onRequestSuccessed(str);
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
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
        ApplyDialog applyDialog = new ApplyDialog(this,true,message);
        showDialog(applyDialog);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commitBtn:
                requestFeedBack();
                break;
            case R.id.backRl:
                closeActivity(FeedBackActivity.class.getName());
                break;
        }
    }
}
