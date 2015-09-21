package cc.horizoom.ssl.xwyq;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;
import cn.com.myframe.BaseActivity;

public class MyBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
