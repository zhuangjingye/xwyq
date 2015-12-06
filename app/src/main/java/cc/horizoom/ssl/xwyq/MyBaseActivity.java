package cc.horizoom.ssl.xwyq;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.network.volley.VolleyError;

public class MyBaseActivity extends BaseActivity {

    public static boolean isActive = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            pushWarningContentReadStatus();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false; //全局变量 记录当前已经进入后台
        }
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 重置推送状态接口
     */
    private void pushWarningContentReadStatus() {
        String url = Protocol.UAPWCRS;
        String customer_id = UserData.getInstance().getCustomerId(this);
        if (TextUtils.isEmpty(customer_id)) return;
        Map<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {

            }

            @Override
            public void onErrResponse(VolleyError error) {

            }
        });
    }

}
