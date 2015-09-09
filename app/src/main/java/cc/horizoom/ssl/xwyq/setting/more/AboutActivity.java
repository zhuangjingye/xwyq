package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15-9-9.
 * 关于
 */
public class AboutActivity extends BaseActivity{

    private RelativeLayout backRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity(AboutActivity.class.getName());
            }
        });
    }
}