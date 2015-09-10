package cc.horizoom.ssl.xwyq.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.view.deletelistview.ListViewCompat;

/**
 * Created by pizhuang on 2015/9/10.
 * 收藏新闻列表
 */
public class FavoriteNewsActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout backRl;

    private ListViewCompat mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news_activity);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        backRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRl:
                closeActivity(FavoriteNewsActivity.class.getName());
                break;
        }
    }
}
