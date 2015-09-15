package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pizhuang on 2015/9/9.
 * 帮助
 */
public class HelpActivity extends MyBaseActivity {
    private RelativeLayout backRl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity(HelpActivity.class.getName());
            }
        });
    }
}
