package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.RelativeLayout;

import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pizhuang on 2015/9/9.
 * 帮助
 */
public class HelpActivity extends BaseActivity {
    private RelativeLayout backRl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity(HelpActivity.class.getName());
            }
        });
    }
}
