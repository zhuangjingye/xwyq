package cc.horizoom.ssl.xwyq.setting.more;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15-9-9.
 * 关于
 */
public class AboutActivity extends MyBaseActivity {

    private RelativeLayout backRl;

    private TextView versionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        versionTv = (TextView) findViewById(R.id.versionTv);
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(AboutActivity.class.getName());
            }
        });
        setVersionTv();
    }

    /**
     * 设置版本信息
     */
    private void setVersionTv() {

        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            versionTv.setText("v"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionTv.setText("v??");
        }
    }
}
