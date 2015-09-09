package cc.horizoom.ssl.xwyq.setting.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.view.MyImageView;

/**
 * Created by pizhuang on 2015/9/9.
 * 更多设置
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener{

    private MyImageView headIv;//我的头像

    private TextView nameTv;//名称

    private LinearLayout userInfoLl;//用户信息

    private LinearLayout wordSizeLl;//文字大小

    private LinearLayout feedBackLl;//用户反馈

    private LinearLayout helpLl;//帮助

    private LinearLayout aboutLl;//关于

    private Button logoutBtn;//注销

    private RelativeLayout backRl;//后退

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        headIv = (MyImageView) findViewById(R.id.headIv);//我的头像
        nameTv = (TextView) findViewById(R.id.nameTv);//名称
        userInfoLl = (LinearLayout) findViewById(R.id.userInfoLl);//用户信息
        wordSizeLl = (LinearLayout) findViewById(R.id.wordSizeLl);//文字大小
        feedBackLl = (LinearLayout) findViewById(R.id.feedBackLl);//用户反馈
        helpLl = (LinearLayout) findViewById(R.id.helpLl);//帮助
        aboutLl = (LinearLayout) findViewById(R.id.aboutLl);//关于
        logoutBtn = (Button) findViewById(R.id.logoutBtn);//注销
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        userInfoLl.setOnClickListener(this);
        wordSizeLl.setOnClickListener(this);
        feedBackLl.setOnClickListener(this);
        helpLl.setOnClickListener(this);
        aboutLl.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        backRl.setOnClickListener(this);
        updataView();
    }

    /**
     * 更新页面
     */
    private void updataView () {
        String name = UserData.getInstance().getRealName(this);
        nameTv.setText(name);
        String headUrl = UserData.getInstance().getPortrait(this);
        doImageRequest(headUrl,headIv,R.mipmap.icon_design,R.mipmap.icon_design);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfoLl:
                break;
            case R.id.wordSizeLl:
                break;
            case R.id.feedBackLl:
                startFeedBackActivity();
                break;
            case R.id.helpLl:
                startHelpActivity();
                break;
            case R.id.aboutLl:
                startAboutActivity();
                break;
            case R.id.logoutBtn:
                break;
            case R.id.backRl:
                closeActivity(MoreActivity.class.getName());
                break;
        }
    }

    /**
     * 打开关于页面
     */
    private void startHelpActivity() {
        Intent intent = new Intent();
        intent.setClass(this,HelpActivity.class);
        startActivity(intent);
    }

    /**
     * 打开关于页面
     */
    private void startAboutActivity() {
        Intent intent = new Intent();
        intent.setClass(this,AboutActivity.class);
        startActivity(intent);
    }
    /**
     * 打开意见反馈
     */
    private void startFeedBackActivity() {
        Intent intent = new Intent();
        intent.setClass(this,FeedBackActivity.class);
        startActivity(intent);
    }
}
