package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.CardData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.WarningParatmer;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.view.CardsView;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.warning.WarningActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pizhuang on 2015/9/8.
 * 主页带选项卡的新闻分类列表
 */
public class MainNewsPageActivity extends MyBaseActivity implements View.OnClickListener{

    private RelativeLayout settingRl;//设置

    private RelativeLayout leftRl;//向左按钮

    private RelativeLayout rightRl;//向右按钮

    private FrameLayout contentFl;//内容

    private ImageView settingIv;//设置

    private TextView waringNumTv;//重要新闻个数

    private RelativeLayout warningRl;//重要新闻入口

    private RelativeLayout waringNumRl;//个数布局

    private ImageView warningIv;

    private CardsView cardsView;

    private CardsView.OnChangeListener onChangeListener = new CardsView.OnChangeListener() {
        @Override
        public void onChangeListener(int num) {
            if (num == 0) {
                waringNumRl.setVisibility(View.GONE);
                warningIv.setImageResource(R.mipmap.warning_bell);
            } else {
                waringNumRl.setVisibility(View.VISIBLE);
                warningIv.setImageResource(R.mipmap.warning_bell_red);
            }

            if (num >= 99) {
                waringNumTv.setText("99");
            } else {
                waringNumTv.setText(num+"");
            }
        }
    };

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://请求分类卡片 如果数据为空则需要从新加载
                    requestCCCPCL();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initYoumengPush();
        setContentView(R.layout.activity_main_news_page);
        settingRl = (RelativeLayout) findViewById(R.id.settingRl);//设置
        leftRl = (RelativeLayout) findViewById(R.id.leftRl);//向左按钮
        rightRl = (RelativeLayout) findViewById(R.id.rightRl);//向右按钮
        contentFl = (FrameLayout) findViewById(R.id.contentFl);//内容
        settingIv = (ImageView) findViewById(R.id.settingIv);//设置
        waringNumTv = (TextView) findViewById(R.id.waringNumTv);
        warningRl = (RelativeLayout) findViewById(R.id.warningRl);
        waringNumRl = (RelativeLayout) findViewById(R.id.waringNumRl);
        warningIv = (ImageView) findViewById(R.id.warningIv);
        warningRl.setOnClickListener(this);
        settingRl.setOnClickListener(this);
        leftRl.setOnClickListener(this);
        rightRl.setOnClickListener(this);
//        ArrayList<CardEntity> data = CardData.getInstance().getCardDatas(MainNewsPageActivity.this);
        CardData.getInstance().clearSaveData(MainNewsPageActivity.this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<CardEntity> data = CardData.getInstance().getCardDatas(MainNewsPageActivity.this);
        if (data.size() == 0) {
            myHandler.sendEmptyMessage(1);
        } else {
            updataView();
        }
    }

    /**
     * 更新页面数据
     */
    private void updataView() {
        if (cardsView == null) {
            cardsView = new CardsView(this);
            cardsView.setOnChangeListener(onChangeListener);
            cardsView.updateView(this);
            contentFl.removeAllViews();
            contentFl.addView(cardsView);
        }
    }

    /**
     * 已登录用户卡片列表接口
     */
    private void requestCCCPCL() {
        String url = Protocol.CCCPCL;
        HashMap<String,String> map = new HashMap<String,String>();
        String customer_id = UserData.getInstance().getCustomerId(this);
        map.put("customer_id", customer_id);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        CardData.getInstance().clearSaveData(MainNewsPageActivity.this);
                        CardData.getInstance().saveData(MainNewsPageActivity.this, str);
                        onRequestCCCPCLSuccess();
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 请求分类卡成功回调
     */
    private void onRequestCCCPCLSuccess() {
        cardsView = null;
        updataView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settingRl:
                showSettingPopUpWindow();
                break;
            case R.id.leftRl:
                if (null != cardsView)
                cardsView.scrollToLeft();
                break;
            case R.id.rightRl:
                if (null != cardsView)
                cardsView.scrollToRight();
                break;
            case R.id.warningRl:
                requestWarningParameter();
                break;
        }
    }

    /**
     * 获得安全参数
     */
    private void requestWarningParameter() {
        String url = Protocol.CWP;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        showWaitDialog();
        doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        WarningParatmer.getInstance().saveData(MainNewsPageActivity.this, str);
                        requestWarningNewList();
                    } else {
                        showToast(message);
                        hideWaitDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideWaitDialog();
                }
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 请求预警新闻列表
     */
    private void requestWarningNewList() {
        String url = Protocol.CWPCL;
        String customer_id = UserData.getInstance().getCustomerId(this);
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("customer_id", customer_id);
        map.put("ht_id","all");
        map.put("page","1");
        doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                NewsListData.getInstance().clearSaveData(MainNewsPageActivity.this);
                NewsListData.getInstance().saveData(MainNewsPageActivity.this, str);
                hideWaitDialog();
                startWaringActivity();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 打开舆情页面
     */
    private void startWaringActivity() {
        Intent intent = new Intent(this, WarningActivity.class);
        startActivity(intent);
    }


    /**
     * 显示设置窗口
     */
    private void showSettingPopUpWindow() {
        SettingPopUpWindow settingPopUpWindow = new SettingPopUpWindow(this);
        settingPopUpWindow.showAsDropDown(settingIv);
    }

    /**
     * 打开新闻页面
     */
    public void startLoginNewsActivity() {
        NewsListData.getInstance().clearSaveData(this);
        Intent intent = new Intent();
        intent.setClass(this,LoginNewsActivity.class);
        startActivity(intent);
    }

    /**
     * 初始化友盟推送
     */
    private void initYoumengPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable(mRegisterCallback);
        mPushAgent.onAppStart();
//        getToken();
    }

//    private void getToken() {
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String device_token = "";
//                while (MyUtils.isEmpty(device_token)) {
//                    device_token = UmengRegistrar.getRegistrationId(MainNewsPageActivity.this);
//                    MyUtils.log(MainNewsPageActivity.class,"device_token="+device_token);
//                    try {
//                        sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (!MyUtils.isEmpty(device_token)) {
//                        String customer_id = UserData.getInstance().getCustomerId(MainNewsPageActivity.this);
//                        PushAgent mPushAgent = PushAgent.getInstance(MainNewsPageActivity.this);
//                        try {
//                            mPushAgent.addAlias(customer_id, "hz_passport");
//                            mPushAgent.addExclusiveAlias(customer_id, "hz_passport");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        }.start();
//
//    }


    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
        @Override
        public void onRegistered(String registrationId) {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    String device_token = UmengRegistrar.getRegistrationId(MainNewsPageActivity.this);
                    String customer_id = UserData.getInstance().getCustomerId(MainNewsPageActivity.this);
                    MyUtils.log(MainNewsPageActivity.class,"device_token="+device_token);
                    MyUtils.log(MainNewsPageActivity.class,"customer_id="+customer_id);
                    PushAgent mPushAgent = PushAgent.getInstance(MainNewsPageActivity.this);
                    try {
                        mPushAgent.removeAlias(customer_id, "hz_passport");
                        mPushAgent.addAlias(customer_id, "hz_passport");
//                mPushAgent.addExclusiveAlias(customer_id, "hz_passport");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    };

}
