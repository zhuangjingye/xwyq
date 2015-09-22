package cc.horizoom.ssl.xwyq;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.horizoom.ssl.xwyq.MainNewsPage.MainNewsPageActivity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsPageActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import cn.com.myframe.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import cn.com.myframe.universalimageloader.cache.memory.impl.LruMemoryCache;
import cn.com.myframe.universalimageloader.core.DisplayImageOptions;
import cn.com.myframe.universalimageloader.core.ImageLoader;
import cn.com.myframe.universalimageloader.core.ImageLoaderConfiguration;
import cn.com.myframe.universalimageloader.core.assist.QueueProcessingType;
import cn.com.myframe.universalimageloader.core.decode.BaseImageDecoder;
import cn.com.myframe.universalimageloader.core.download.BaseImageDownloader;
import cn.com.myframe.universalimageloader.utils.StorageUtils;

/**
 * Created by pizhuang on 2015/9/9.
 */
public class MyApplaction extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

    /**
     * 初始化图片加载器
     */
    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .diskCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null)
//                .taskExecutor(...).taskExecutorForCachedImages(...)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(400)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

    }

    /**
     * 消息处理
     */
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler(){

        @Override
        public void launchApp(Context context, UMessage uMessage) {
            super.launchApp(context, uMessage);

        }

        @Override
        public void openActivity(Context context, UMessage uMessage) {
            super.openActivity(context, uMessage);
            MyUtils.log(MainNewsPageActivity.class, "uMessage.extra=" + uMessage.extra);
            Map<String,String> map = uMessage.extra;
            String action = map.get("action");
            if ("software_update".equals(action)) {
                updataSoftWare(map);
            } else if ("warning_push_content".equals(action)) {
                warningNews(map);
            }
        }

        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 更新软件
     * @param map
     */
    private void updataSoftWare(Map<String,String> map) {
        String url = map.get("update_url");
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);
        startActivity(intent);
    }

    /**
     * 预警新闻
     * @param map
     */
    private void warningNews(Map<String,String> map) {
        String newsId = map.get("news_id");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, NewsPageActivity.class);
        intent.putExtra("newsId",newsId);
        startActivity(intent);
    }

}
