package cc.horizoom.ssl.xwyq.boot;
import android.content.Intent;
import android.os.Bundle;

import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.login.LoginActivity;

/**
 * 启动页，完成加载信息的工作
 */

public class BootActivity extends MyBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);
        startLoginActivity();
    }

    /**
     * 打开登录页
     */
    private void startLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }
}
