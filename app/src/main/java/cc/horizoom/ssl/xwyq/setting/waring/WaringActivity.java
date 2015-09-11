package cc.horizoom.ssl.xwyq.setting.waring;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * Created by pizhuang on 2015/9/11.
 * 预警页面
 */
public class WaringActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout backRl;//返回

    private RelativeLayout headRl;//上半部分

    private TextView securityScoreTv;//安全得分

    private TextView socerTv;//得分

    private TextView histogramTv;//柱状图

    private TextView messageTv;//消息

    private LinearLayout barDownLl;//向下啦

    private LinearLayout barUpLl;//向上拉

    private MyBounceListview listview;//消息列表

    private WebView webView;//柱状图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waring);
        backRl = (RelativeLayout) findViewById(R.id.backRl);//返回
        securityScoreTv = (TextView) findViewById(R.id.securityScoreTv);//安全得分
        socerTv = (TextView) findViewById(R.id.socerTv);//得分
        histogramTv = (TextView) findViewById(R.id.histogramTv);//柱状图
        messageTv = (TextView) findViewById(R.id.messageTv);//消息
        barDownLl = (LinearLayout) findViewById(R.id.barDownLl);//向下啦
        barUpLl = (LinearLayout) findViewById(R.id.barUpLl);//向上拉
        listview = (MyBounceListview) findViewById(R.id.listView);//消息列表
        webView = (WebView) findViewById(R.id.webView);//柱状图
        headRl = (RelativeLayout) findViewById(R.id.headRl);
        backRl.setOnClickListener(this);
        barDownLl.setOnClickListener(this);
        barUpLl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backRl:
                closeActivity(WaringActivity.class.getName());
                break;
            case R.id.barUpLl:
                showWebView();
                break;
            case R.id.barDownLl:
                hideWebView();
                break;

        }
    }

    /**
     * 显示网页
     */
    private void showWebView () {
        if (View.GONE != webView.getVisibility()) return;
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        int height = getMoveDy();
        webView.setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(0,0,height,0);//初始化
        translateAnimation.setInterpolator(decelerateInterpolator);
        translateAnimation.setDuration(200);  //设置动画时间
        webView.startAnimation(translateAnimation);
    }

    /**
     * 获得移动距离
     * @return
     */
    private int getMoveDy() {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        headRl.measure(w, h);
        int headRlheight = headRl.getMeasuredHeight();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        int height = screenHeight - headRlheight;
        return height;
    }
    /**
     * 隐藏网页
     */
    private void hideWebView () {
        if (View.GONE == webView.getVisibility()) return;
        int height = getMoveDy();
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        Animation translateAnimation = new TranslateAnimation(0,0,0,height);//初始化
        translateAnimation.setInterpolator(accelerateInterpolator);
        translateAnimation.setDuration(200);  //设置动画时间
        webView.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                webView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
