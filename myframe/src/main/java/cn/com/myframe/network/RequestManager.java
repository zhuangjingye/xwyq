package cn.com.myframe.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import cn.com.myframe.MyUtils;
import cn.com.myframe.network.volley.RequestQueue;
import cn.com.myframe.network.volley.toolbox.ImageLoader;

/**
 * Created by pi on 15-6-16.
 * 管理请求 单例模式主要处理图片的加载与缓存
 */
public class RequestManager {

    private volatile static  RequestManager requestManager;
    public static RequestManager getInstance() {
        if(null == requestManager) {
            synchronized (RequestManager.class) {
                if(null == requestManager) {
                    requestManager = new RequestManager();
                }
            }
        }
        return requestManager;
    }

}
