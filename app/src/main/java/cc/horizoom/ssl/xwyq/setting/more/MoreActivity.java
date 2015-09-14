package cc.horizoom.ssl.xwyq.setting.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.login.LoginActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyImageView;

/**
 * Created by pizhuang on 2015/9/9.
 * 更多设置
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener{

    public static final String key = "font_size";

    public static final String LARGE = "large";//字体 大

    public static final String MEDIUN = "medium";//字体 中

    public static final String SMALL = "small";//字体 小

    private MyImageView headIv;//我的头像

    private TextView nameTv;//名称

    private LinearLayout userInfoLl;//用户信息

    private LinearLayout wordSizeLl;//文字大小

    private LinearLayout feedBackLl;//用户反馈

    private LinearLayout helpLl;//帮助

    private LinearLayout aboutLl;//关于

    private Button logoutBtn;//注销

    private RelativeLayout backRl;//后退

    private LinearLayout textSizeSelectLl;//字体大小选择

    private TextView size1Tv;//大

    private TextView size2Tv;//大

    private TextView size3Tv;//大

    private TextView textSizeShowTv;//字体大小显示

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        headIv = (MyImageView) findViewById(R.id.headIv);//我的头像
        nameTv = (TextView) findViewById(R.id.nameTv);//名称
        userInfoLl = (LinearLayout) findViewById(R.id.userInfoLl);//用户信息
        wordSizeLl = (LinearLayout) findViewById(R.id.wordSizeLl);//文字大小
        feedBackLl = (LinearLayout) findViewById(R.id.feedBackLl);//用户反馈
        size1Tv = (TextView) findViewById(R.id.size1TV);//大
        size2Tv = (TextView) findViewById(R.id.size2TV);//大
        size3Tv = (TextView) findViewById(R.id.size3TV);//大
        textSizeSelectLl = (LinearLayout) findViewById(R.id.textSizeSelectLl);
        textSizeSelectLl.setVisibility(View.GONE);
        helpLl = (LinearLayout) findViewById(R.id.helpLl);//帮助
        aboutLl = (LinearLayout) findViewById(R.id.aboutLl);//关于
        logoutBtn = (Button) findViewById(R.id.logoutBtn);//注销
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        textSizeShowTv = (TextView) findViewById(R.id.textSizeShowTv);
        userInfoLl.setOnClickListener(this);
        wordSizeLl.setOnClickListener(this);
        feedBackLl.setOnClickListener(this);
        helpLl.setOnClickListener(this);
        aboutLl.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        backRl.setOnClickListener(this);
        size1Tv.setOnClickListener(this);
        size2Tv.setOnClickListener(this);
        size3Tv.setOnClickListener(this);
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
        updataTextSizeShowTv();
    }

    /**
     * 更新字体大小显示
     */
    private void updataTextSizeShowTv() {
        String font_size = Mysharedperferences.getIinstance().getString(this, key);
        String str = String.format(getString(R.string.word_size_0), getString(R.string.word_size_3));
        if (MyUtils.isEmpty(font_size)) {
            textSizeShowTv.setText(str);
            return;
        }
        if (LARGE.equals(font_size)) {
            str = String.format(getString(R.string.word_size_0), getString(R.string.word_size_1));
        } else if (MEDIUN.equals(font_size)) {
            str = String.format(getString(R.string.word_size_0), getString(R.string.word_size_2));
        } else if (SMALL.equals(font_size)) {
            str = String.format(getString(R.string.word_size_0), getString(R.string.word_size_3));
        }
        textSizeShowTv.setText(str);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfoLl:
                startUserInfoActivity();
                break;
            case R.id.wordSizeLl:
                testSizeSelected();
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
                logOut();
                break;
            case R.id.backRl:
                closeActivity(MoreActivity.class.getName());
                break;
            case R.id.size1TV:
                selectedSize(LARGE);
                break;
            case R.id.size2TV:
                selectedSize(MEDIUN);
                break;
            case R.id.size3TV:
                selectedSize(SMALL);
                break;
        }
    }

    /**
     * 文字大小选择
     */
    private void testSizeSelected() {
        if (textSizeSelectLl.getVisibility() == View.GONE) {
            textSizeSelectLl.setVisibility(View.VISIBLE);
        } else {
            textSizeSelectLl.setVisibility(View.GONE);
        }
    }

    /**
     * 设置文字大小
     * @param font_size
     */
    private void selectedSize(String font_size) {
        testSizeSelected();
        Mysharedperferences.getIinstance().putString(this, key, font_size);
        updataTextSizeShowTv();
    }

    /**
     * 打开关于页面
     */
    private void startUserInfoActivity() {
        Intent intent = new Intent();
        intent.setClass(this, UserInfoActivity.class);
        startActivity(intent);
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

    /**
     * 退出
     */
    private void logOut() {
        unLoginPushContentList();
    }

    /**
     * 未登录新闻列表
     */
    private void unLoginPushContentList() {
        UserData.getInstance().clearSaveData(this);
        NewsListData.getInstance().clearSaveData(this);
        String url = Protocol.UNLOGINPUSHCONTENTLIST;
        HashMap<String,String> hashMap = new HashMap<String,String>();
        showWaitDialog();
        doRequestString(url, hashMap, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    if (success) {
                        NewsListData.getInstance().clearData();
                        NewsListData.getInstance().saveData(MoreActivity.this, str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
                startLoginActivity();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
                startLoginActivity();
            }
        });
    }

    /**
     * 打开登录页
     */
    private void startLoginActivity() {
        closeActivity(MoreActivity.class.getName());
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }

}
