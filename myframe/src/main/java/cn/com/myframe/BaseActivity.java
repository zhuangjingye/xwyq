package cn.com.myframe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import cn.com.myframe.mydialog.DialogManager;
import cn.com.myframe.mydialog.MyBasicDialog;
import cn.com.myframe.mydialog.dialog.WaitDialog;
import cn.com.myframe.network.MyRequest.MultipartRequest;
import cn.com.myframe.network.MyRequest.NormalPostRequest;
import cn.com.myframe.network.volley.AuthFailureError;
import cn.com.myframe.network.volley.DefaultRetryPolicy;
import cn.com.myframe.network.volley.Request;
import cn.com.myframe.network.volley.RequestQueue;
import cn.com.myframe.network.volley.Response;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.network.volley.toolbox.JsonObjectRequest;
import cn.com.myframe.network.volley.toolbox.StringRequest;
import cn.com.myframe.network.volley.toolbox.Volley;
import cn.com.myframe.toast.MyToast;
import cn.com.myframe.universalimageloader.core.DisplayImageOptions;
import cn.com.myframe.universalimageloader.core.assist.ImageScaleType;
import cn.com.myframe.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static cn.com.myframe.network.volley.Request.Method.GET;
import static cn.com.myframe.network.volley.Request.Method.POST;

/**
 * Created by pizhuang on 2015/8/24.
 * 基础功能activity 主要完成对话框控制  关闭activity toast 联网功能
 */
public class BaseActivity extends FragmentActivity {

    private RequestQueue requestQueue;

    //设置请求超时时间
    private DefaultRetryPolicy defaultRetryPolicy = new DefaultRetryPolicy(10000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private MyBasicDialog myBasicDialog;

    /**
     * 用来标志退出所有Activity的广播action
     */
    public static String BROAD_CASET_FINISH = "finish";
    /**
     * 选择关闭Activity的广播action
     */
    public static String BROAD_CASET_CHOSE_FINISH = "broad.caset.chose.finish";
    private BroadcastReceiverHelper rhelper;// 广播接收器

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://隐藏对话框
                    DialogManager.getInstance().hideDialog();
                    break;
                case 2://显示对话框
                    DialogManager.getInstance().showDialog(myBasicDialog);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        requestQueue = Volley.newRequestQueue(this);
        // 发送广播选择关闭界面
        rhelper = new BroadcastReceiverHelper(this);
        rhelper.registerAction(BROAD_CASET_CHOSE_FINISH);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rhelper);
    }

    /**
     * 方法描述 : 开始请求
     *
     * @param request void
     */
    public <T> void startRequest(Request<T> request) {
        request.setRetryPolicy(defaultRetryPolicy);

        requestQueue.add(request);
    }

    /**
     * 方法描述 : 获得请求队列
     *
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    /**
     * 类描述： 监听广播，根据收到的广播信息，关闭activity 创建者： pi 项目名称： MyFramework 创建时间： 2015年1月15日
     * 下午4:38:40 版本号： v1.0
     */
    class BroadcastReceiverHelper extends BroadcastReceiver {
        Context ct = null;
        BroadcastReceiverHelper receiver;

        public BroadcastReceiverHelper(Context c) {
            ct = c;
            receiver = this;
        }

        // 注册
        public void registerAction(String action) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            ct.registerReceiver(receiver, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BROAD_CASET_CHOSE_FINISH.equals(intent.getAction())) {
                ArrayList<String> list = intent
                        .getStringArrayListExtra("closeList");
                finishChosedActivity(list);
            }
        }
    }

    /**
     * 关闭全部activity
     */
    public void closeAllActivity() {
        closeActivity("all");
    }

    /**
     * 方法描述 : 创建者：pizhuang 创建时间： 2014年6月24日 上午11:08:28 void
     */
    private void finishChosedActivity(ArrayList<String> list) {
        if (list.contains("all")) {
            finish();
            return;
        }
        if (list.contains(this.getClass().getName())) {
            if (!onFinish()) {
                // IntentUtils.finishTopToBottom(BaseActivity.this, null, null);
                hideDialog();
                finish();
            }
        }
    }

    /**
     * 方法描述 : 关闭activity的方式，子类可自定义 如果自己定义的过重写并返回true 创建者：pizhuang 创建时间：
     * 2014年6月24日 上午10:55:00 void
     */
    public boolean onFinish() {
        return false;
    }

    /**
     * 方法描述 : 选择关闭activity 创建者：pizhuang 创建时间： 2014年6月24日 上午11:00:57
     *
     * @param arg className的数组 void
     */
    public void closeActivity(String... arg) {
        if (arg == null || arg.length == 0) {
            return;
        }
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < arg.length; i++) {
            list.add(arg[i]);
        }

        Intent intent = new Intent();
        intent.setAction(BROAD_CASET_CHOSE_FINISH);
        intent.putStringArrayListExtra("closeList", list);
        sendBroadcast(intent);
    }

    /**
     * 方法描述 : 通过toast显示信息
     * 创建时间： 2015年1月16日 下午2:33:38
     *
     * @param str
     */
    public void showToast(String str, int duration) {
        MyToast.makeText(this, str, duration).show();
    }

    public void showToast(int strId, int duration) {
        MyToast.makeText(this, strId, duration).show();
    }


    /**
     * 方法描述 : 默认短时间
     * 创建时间： 2015年1月16日 下午2:37:43
     *
     * @param str void
     */
    public void showToast(String str) {
        showToast(str, MyToast.LENGTH_SHORT);
    }

    public void showToast(int strId) {
        showToast(strId, MyToast.LENGTH_SHORT);
    }

    /**
     * 显示对话框
     *
     * @param myBasicDialog
     */
    public void showDialog(MyBasicDialog myBasicDialog) {
        this.myBasicDialog = myBasicDialog;
        myHandler.sendEmptyMessage(2);
    }

    /**
     * 隐藏对话框
     */
    public void hideDialog() {
        myHandler.sendEmptyMessage(1);
    }

    /**
     * 请求字符串
     *
     * @param urlTmp
     * @param result 请求结果
     */
    public void doRequestString(String urlTmp, final RequestResult result) {
        final String url = urlTmp;

        MyUtils.log(BaseActivity.class, "doRequestString_url=" + url);
        StringRequest stringRequest = new StringRequest(GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyUtils.log(BaseActivity.class, "doRequestString_result=" + response);
                        result.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.onErrResponse(error);
                MyUtils.log(BaseActivity.class, "doRequestString_str_error=" + error.getMessage());
                showToast("网络有问题，请稍后再试！");
            }
        });
        startRequest(stringRequest);
    }

    /**
     * 请求字符串
     *
     * @param url    地址
     * @param result 请求结果
     */
    public void doRequestString(String url, final Map<String, String> map, final RequestResult result) {
        MyUtils.log(BaseActivity.class, "doRequestString_url=" + url + "   map=" + map.toString());
        StringRequest stringRequest = new StringRequest(POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MyUtils.log(BaseActivity.class, "doRequestString_result=" + response);
                        result.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.onErrResponse(error);
                MyUtils.log(BaseActivity.class, "doRequestString_map_error=" + error.getMessage());
                showToast("网络有问题，请稍后再试！");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };
        startRequest(stringRequest);
    }

    /**
     * 获取json数据
     *
     * @param urlTmp
     * @param result
     */
    public void doRequestJson(String urlTmp, final RequestResult result) {
        String url = urlTmp;
        MyUtils.log(BaseActivity.class, "doRequestJson=" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyUtils.log(BaseActivity.class, "doRequestJson=" + response);
                        result.onResponse(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.onErrResponse(error);
                MyUtils.log(BaseActivity.class, "doRequestJson_str_error=" + error.getMessage());
                showToast("网络有问题，请稍后再试！");
            }
        });
        startRequest(jsonObjectRequest);
    }

    /**
     * 获取json数据
     *
     * @param url
     * @param result
     */
    public void doRequestJson(String url, final Map<String, String> map, final RequestResult result) {
        MyUtils.log(BaseActivity.class, "doRequestJson_map=" + url + "  map=" + map.toString());
        NormalPostRequest jsonObjectRequest = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyUtils.log(BaseActivity.class, "doRequestJson=" + response);
                        result.onResponse(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        result.onErrResponse(error);
                        MyUtils.log(BaseActivity.class, "doRequestJson_map_error=" + error.getMessage());
                        showToast("网络有问题，请稍后再试！");
                    }
                }, map);
        startRequest(jsonObjectRequest);
    }

    /**
     * 上传多图片
     *
     * @param url
     * @param fileMap
     * @param params
     * @param result
     */
    public void doMultipartRequest(String url, Map<String, File> fileMap, Map<String, String> params, final RequestResult result) {
        MyUtils.log(BaseActivity.class, "doMultipartRequest=" + url + "  map=" + params.toString());
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.onErrResponse(error);
                MyUtils.log(BaseActivity.class, "doMultipartRequest_1_error=" + error.getMessage());
                showToast("网络有问题，请稍后再试！");
            }
        };
        Response.Listener<String> mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.log(BaseActivity.class, "doMultipartRequest=" + response);
                result.onResponse(response);

            }
        };

        MultipartRequest multipartRequest = new MultipartRequest(url, errorListener, mListener, fileMap, params);
        startRequest(multipartRequest);
    }

    /**
     * 上传单图片
     *
     * @param url
     * @param filePartName
     * @param file
     * @param params
     * @param result
     */
    public void doSingleFileRequest(String url, String filePartName, File file, Map<String, String> params, final RequestResult result) {

        MyUtils.log(BaseActivity.class, "doMultipartRequest=" + url + "  map=" + params.toString());
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                result.onErrResponse(error);
                MyUtils.log(BaseActivity.class, "doMultipartRequest_error=" + error.getMessage());
                showToast("网络有问题，请稍后再试！");
            }
        };
        Response.Listener<String> mListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                MyUtils.log(BaseActivity.class, "doMultipartRequest=" + response);
                result.onResponse(response);
            }
        };
        MultipartRequest multipartRequest = new MultipartRequest(url, errorListener, mListener, filePartName, file, params);
        startRequest(multipartRequest);
    }


    /**
     * 获取图片 增加本地缓存
     * @param imageUrl
     * @param mImageView
     * @param default_image
     * @param failed_image
     */
    public void doImageRequest(String imageUrl, ImageView mImageView, int default_image, int failed_image) {
        cn.com.myframe.universalimageloader.core.ImageLoader.getInstance().displayImage(imageUrl, mImageView,
                getDisplayImageOptions(default_image, failed_image, false));
    }

    /**
     * 强制从网络获取资源
     * @param imageUrl
     * @param mImageView
     * @param default_image
     * @param failed_image
     */
    public void doImageRequestFromNet(String imageUrl, final ImageView mImageView, int default_image, int failed_image) {
        if (MyUtils.isEmpty(imageUrl)) return;
        cn.com.myframe.universalimageloader.core.ImageLoader.getInstance().displayImage(imageUrl, mImageView,
                getDisplayImageOptions(default_image, failed_image, true));
    }



    /**
     * 获得图片加载的默认设置
     * @return
     */
    private DisplayImageOptions getDisplayImageOptions(int default_image, int failed_image,boolean isGetFromNet) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(default_image) // resource or drawable
                .showImageForEmptyUri(default_image) // resource or drawable
                .showImageOnFail(failed_image) // resource or drawable
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(0)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
//                .preProcessor(...)
//                .postProcessor(...)
//                .extraForDownloader(...)
//                .considerExifParams(false) // default
//                .decodingOptions(...)
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
        options.setGetImageFromeNet(isGetFromNet);

        return options;
    }


    /**
     * 显示等待框
     */
    public void showWaitDialog() {
        WaitDialog waitDialog = new WaitDialog(this);
        showDialog(waitDialog);
    }

    /**
     * 隐藏等待框
     */
    public void hideWaitDialog() {
        hideDialog();
    }

    /**
     * 点击键盘以外部分，键盘消失
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public interface ImageRequestResult {
        public void onResponse(Bitmap response);
        public void onErrResponse(VolleyError error);
    }

    /**
     * 网络返回结果回调
     */
    public interface RequestResult {
        public void onResponse(String str);
        public void onErrResponse(VolleyError error);
    }
}
