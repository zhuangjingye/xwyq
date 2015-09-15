package cc.horizoom.ssl.xwyq.setting.more;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.view.MyImageView;

/**
 * Created by pizhuang on 2015/9/10.
 * 用户信息
 */
public class UserInfoActivity extends MyBaseActivity {

    private RelativeLayout backRl;//后退

    private MyImageView headIv;//头像

    private TextView nameTv;//名称

    private TextView userNameTv;//用户姓名

    private TextView userCustomNameTv;//客户姓名

    private TextView userSellNameTv;//对口销售

    private TextView userCustomTipTv;//用户简介

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        headIv = (MyImageView) findViewById(R.id.headIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        userNameTv = (TextView) findViewById(R.id.userNameTv);
        userCustomNameTv = (TextView) findViewById(R.id.userCustomNameTv);
        userSellNameTv = (TextView) findViewById(R.id.userSellNameTv);
        userCustomTipTv = (TextView) findViewById(R.id.userCustomTipTv);
        backRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity(UserInfoActivity.class.getName());
            }
        });
        updataView();
    }

    /**
     * 更新页面
     */
    private void updataView () {
        String headUrl = UserData.getInstance().getPortrait(this);
        if (!MyUtils.isEmpty(headUrl)) {
            doImageRequest(headUrl,headIv,R.mipmap.icon_design,R.mipmap.icon_design);
        }
        String name = UserData.getInstance().getRealName(this);
        nameTv.setText(name);
        String userName = UserData.getInstance().getUserName(this);
        userNameTv.setText(userName);
        userCustomNameTv.setText(name);
        String salesman = UserData.getInstance().getSalesman(this);
        userSellNameTv.setText(salesman);
        String summary = UserData.getInstance().getSummary(this);
        userCustomTipTv.setText(summary);
    }
}
