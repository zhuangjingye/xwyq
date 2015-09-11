package cc.horizoom.ssl.xwyq.MainNewsPage;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.WarningParatmer;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.favoriteNews.FavoriteNewsActivity;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cc.horizoom.ssl.xwyq.setting.warning.WarningActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.popupWindow.MyPopupWindow;

/**
 * Created by pizhuang on 2015/9/6.
 * 设置功能接口
 */
public class SettingPopUpWindow extends MyPopupWindow implements View.OnClickListener{

    private RelativeLayout waringRl;//预警

    private RelativeLayout preferenceRl;//偏好设置

    private RelativeLayout collectionRl;//收藏

    private RelativeLayout moreRl;//更多设置

    public SettingPopUpWindow(BaseActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    public View getContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
        View contentView = layoutInflater.inflate(R.layout.setting_popup_window,null);
        waringRl = (RelativeLayout) contentView.findViewById(R.id.waringRl);//预警
        preferenceRl = (RelativeLayout) contentView.findViewById(R.id.preferenceRl);//偏好设置
        collectionRl = (RelativeLayout) contentView.findViewById(R.id.collectionRl);//收藏
        moreRl = (RelativeLayout) contentView.findViewById(R.id.moreRl);//更多设置
        waringRl.setOnClickListener(this);
        preferenceRl.setOnClickListener(this);
        collectionRl.setOnClickListener(this);
        moreRl.setOnClickListener(this);
        return contentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waringRl:
//                startWaringActivity();
                requestWarningParameter();
                break;
            case R.id.preferenceRl:
                break;
            case R.id.collectionRl:
                requestFavoriteNews();
                break;
            case R.id.moreRl:
                startMoreActivity();
                break;
        }
        dismiss();
    }

    /**
     * 打开更多页面
     */
    private void startFavoriteNewsActivity() {
        Intent intent = new Intent(baseActivity, FavoriteNewsActivity.class);
        baseActivity.startActivity(intent);
    }

    /**
     * 打开更多页面
     */
    private void startMoreActivity() {
        Intent intent = new Intent(baseActivity, MoreActivity.class);
        baseActivity.startActivity(intent);
    }

    /**
     * 打开舆情页面
     */
    private void startWaringActivity() {
        Intent intent = new Intent(baseActivity, WarningActivity.class);
        baseActivity.startActivity(intent);
    }

    /**
     * 获取收藏新闻
     * customer_id  （客户id,必填）
     * page（分页id,默认为0）
     */
    private void requestFavoriteNews() {
        NewsListData.getInstance().clearSaveData(baseActivity);
        String customer_id = UserData.getInstance().getCustomerId(baseActivity);
        String url = Protocol.CFR;
        HashMap<String,String> hashMap = new HashMap<String,String>();
        hashMap.put("customer_id",customer_id);
        hashMap.put("page","0");
        baseActivity.showWaitDialog();
        baseActivity.doRequestString(url, hashMap, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                baseActivity.hideWaitDialog();
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    String message = jsonObject.getString("message");
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        NewsListData.getInstance().saveData(baseActivity, str);
                        startFavoriteNewsActivity();
                    } else {
                        baseActivity.showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrResponse(VolleyError error) {
                baseActivity.hideWaitDialog();
            }
        });
    }

    /**
     * 获得安全参数
     */
    private void requestWarningParameter() {
        String url = Protocol.CWP;
        String customer_id = UserData.getInstance().getCustomerId(baseActivity);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        baseActivity.showWaitDialog();
        baseActivity.doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    if (success) {
                        WarningParatmer.getInstance().saveData(baseActivity, str);
                        requestWarningNewList();
                    } else {
                        baseActivity.showToast(message);
                        baseActivity.hideWaitDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    baseActivity.hideWaitDialog();
                }
            }

            @Override
            public void onErrResponse(VolleyError error) {
                baseActivity.hideWaitDialog();
            }
        });
    }

    /**
     * 请求预警新闻列表
     */
    private void requestWarningNewList() {
        String url = Protocol.CWPCL;
        String customer_id = UserData.getInstance().getCustomerId(baseActivity);
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        baseActivity.doRequestString(url, map, new BaseActivity.RequestResult() {
            @Override
            public void onResponse(String str) {
                NewsListData.getInstance().clearSaveData(baseActivity);
                NewsListData.getInstance().saveData(baseActivity,str);
                baseActivity.hideWaitDialog();
                startWaringActivity();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                baseActivity.hideWaitDialog();
            }
        });
    }
}
