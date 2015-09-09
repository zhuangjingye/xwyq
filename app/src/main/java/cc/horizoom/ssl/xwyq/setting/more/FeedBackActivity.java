package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15-9-9.
 * 反馈
 */
public class FeedBackActivity extends BaseActivity {
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
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(FeedBackActivity.class.getName());
            }
        });
    }
}
