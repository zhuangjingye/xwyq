package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.horizoom.ssl.xwyq.DataManager.FunctionListData;
import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.FunctionEntity;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.httpmime_4_2_6.apache.http.entity.mime.content.StringBody;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * Created by pi on 15-9-4.
 */
public abstract class BaseMainNewsActivity extends MyBaseActivity implements View.OnClickListener{

    private MyBounceListview myListView;

    private ArrayList<NewsEntity> data;

    private ImageView newHeadImg;//list到头图片

    private int originalHeadBgIvHeight;//背景imageview默认初始化高度

    private Bitmap headBitmap;//头图像

    protected NewsAdapter newsAdapter;//新闻适配起

    private RelativeLayout titleRl;//标题栏

    private RelativeLayout titleBackRl;//标题栏返回

    private View headView;//头

    private ImageView headImg;//放大缩小背景图

    private FrameRunnable frameRunnable;//帧线线程

    private TextView topLine;//标准线1

    private TextView titleLine;//标准线2

    private TextView bottomLine;//标准线3

    private TextView bgTitleLine;//背景taitle标记线

    private TextView headImgLine;//头标准线  停留位置

    private RelativeLayout searchRl;//查询框

    private int[] originalTopLinePositon;//线1初始位置

    private int[] originalTitleLinePositon;//线2初始位置

    private int[] originalBottomLinePositon;//线3初始位置

    private int[] originalheadImgLinePositon;//头标记线停留位置

    private int[] originalBgTitleLinPositon;//背景title标记线初始位置

    private int[] currentTopLinePositon = new int[2];

    private int[] currentTitleLinePositon = new int[2];

    private int[] currentBottomLinePositon = new int[2];

    private RelativeLayout classifyRl;//分类查询按钮

    private EditText searchEt;//搜索编辑框

    private LinearLayout searchLl;//编辑框布局

    private FunctionEntity selectFunctionEntity;//选中的功能

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updataView();
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener myListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (data.size() > 0 && l>=0) {
                NewsEntity newsEntity = data.get((int)l);
                requestNews(newsEntity);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearData();//每次开启是数据要从新获取
        setContentView(R.layout.activity_base_main_news);
        myListView = (MyBounceListview) findViewById(R.id.myListView);
        titleRl = (RelativeLayout) findViewById(R.id.titleRl);
        bgTitleLine = (TextView) findViewById(R.id.bgTitleLine);
        titleBackRl = (RelativeLayout) findViewById(R.id.titleBackRl);
        headImgLine = (TextView) findViewById(R.id.headImgLine);
        searchRl = (RelativeLayout) findViewById(R.id.searchRl);
        classifyRl = (RelativeLayout) findViewById(R.id.classifyRl);//分类查询按钮
        searchEt = (EditText) findViewById(R.id.searchEt);//搜索编辑框
        searchLl = (LinearLayout) findViewById(R.id.searchLl);
        classifyRl.setOnClickListener(this);
        titleBackRl.setOnClickListener(this);
        headImg = (ImageView) findViewById(R.id.headImg);
        data = NewsListData.getInstance().getNewsData(this);
        newsAdapter = new NewsAdapter(this,data);

        headBitmap = resizeImage(getResources().getDrawable(R.mipmap.bg_list01));
        headImg.setImageBitmap(headBitmap);
        originalHeadBgIvHeight = headImg.getLayoutParams().height;

        headView = getHeadview(headBitmap);
        myListView.addHeaderView(headView);
        myListView.addFooterView(getFooter());
        myListView.setAdapter(newsAdapter);
        myListView.setOnItemClickListener(myListOnItemClickListener);
        onUpdataSearchEt(searchEt);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (frameRunnable == null) {
            frameRunnable = new FrameRunnable(myHandler);
        }
        if (!frameRunnable.isRunning()) {
            frameRunnable.setIsRunning(true);
            new Thread(frameRunnable).start();
        }

        if (data.size() == 0) {//获得数据
            HashMap<String,String> map = new HashMap<String,String>();
            String customer_id = UserData.getInstance().getCustomerId(this);
            if (!MyUtils.isEmpty(customer_id)) {
                map.put("customer_id",customer_id);
                String keyword = NewsListData.getInstance().getKeyWord();
                map.put("keyword",keyword);
            }
            requestNewsList(map);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (frameRunnable != null && frameRunnable.isRunning()) {
            frameRunnable.setIsRunning(false);
        }
    }

    /**
     * 清空数据
     */
    private void clearData() {
        FunctionListData.getInstance().clearSaveData(this);
    }

    /**
     * 更新页面显示
     */
    private void updataView() {
        initOriginalPositoin();
        topLine.getLocationOnScreen(currentTopLinePositon);
        titleLine.getLocationOnScreen(currentTitleLinePositon);
        bottomLine.getLocationOnScreen(currentBottomLinePositon);
        updataHeadImgView();
        updataTitleRl();
        updataSearchRl();
    }

    /**
     * 更新头视图的位置
     */
    private void updataHeadImgView () {
        int dy=currentBottomLinePositon[1] - originalBottomLinePositon[1];
        if (dy >= 0) {
            ViewGroup.LayoutParams layoutParams = headImg.getLayoutParams();
            layoutParams.height = originalHeadBgIvHeight+dy;
            headImg.setLayoutParams(layoutParams);
            headImg.scrollTo(0, 0);
        } else {
            if ( currentBottomLinePositon[1] <= originalheadImgLinePositon[1]) {
                int dy1 = originalheadImgLinePositon[1] - originalBottomLinePositon[1];
                headImg.scrollTo(0,-dy1);
            } else {
                headImg.scrollTo(0,-dy);
            }
        }
    }

    /**
     * 更新title控件
     */
    private void updataTitleRl(){
        int dy = currentTitleLinePositon[1] - originalBgTitleLinPositon[1];
        if (dy <= 0) {
            titleRl.scrollTo(0,-dy);
        } else {
            titleRl.scrollTo(0, 0);
        }
    }

    /**
     * 更新搜索框位置
     */
    private void updataSearchRl() {
        int dy=currentBottomLinePositon[1] - originalBottomLinePositon[1];
        if (dy >= 0) {
            searchRl.scrollTo(0, 0);
        } else {

            if ( currentBottomLinePositon[1] <= originalheadImgLinePositon[1]) {
                int dy1 = originalheadImgLinePositon[1] - originalBottomLinePositon[1];
                searchRl.scrollTo(0,-dy1/3*2);
            } else {
                searchRl.scrollTo(0,-dy/3*2);
            }
        }
    }


    /**
     * 初始化位置信息
     */
    private void initOriginalPositoin() {
        if(originalTopLinePositon==null) {
            originalTopLinePositon = new int[2];
            originalTitleLinePositon = new int[2];
            originalBottomLinePositon = new int[2];
            originalBgTitleLinPositon = new int[2];
            originalheadImgLinePositon = new int[2];
            topLine.getLocationOnScreen(originalTopLinePositon);
            titleLine.getLocationOnScreen(originalTitleLinePositon);
            bottomLine.getLocationOnScreen(originalBottomLinePositon);
            bgTitleLine.getLocationOnScreen(originalBgTitleLinPositon);
            headImgLine.getLocationOnScreen(originalheadImgLinePositon);
        }

    }



    /**
     * 获得头
     * @param headBitmap
     * @return
     */
    private View getHeadview(Bitmap headBitmap) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.view_news_head, null);
        newHeadImg = (ImageView) v.findViewById(R.id.newsHeadImg);
        newHeadImg.setImageBitmap(headBitmap);
        topLine = (TextView) v.findViewById(R.id.headTopLineTv);//标准线1
        titleLine = (TextView) v.findViewById(R.id.titleLineTv);//标准线2
        bottomLine = (TextView) v.findViewById(R.id.headBottomLineTv);//标准线3
        return v;
    }

    /**
     * 获得list眉脚
     * @return
     */
    private View getFooter() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View v = layoutInflater.inflate(R.layout.view_news_footer, null);
        TextView loadTv = (TextView) v.findViewById(R.id.loadTv);
        loadTv.setOnClickListener(this);
        return v;
    }

    /**
     * 从新计算图片大小
     * @param drawable
     * @return
     */
    public Bitmap resizeImage(Drawable drawable) {
        Bitmap bitmapOrg = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmapOrg);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        WindowManager wm = this.getWindowManager();
        int newWidth = wm.getDefaultDisplay().getWidth();
        int newHeight = height;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                height, matrix, true);
        bitmapOrg.recycle();
        return resizedBitmap;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadTv://加载更多
                loadMore();
                break;
            case R.id.titleBackRl:
                closeActivity(getCurrentClassName());
                break;
            case R.id.classifyRl://查询分类
                getFunctionList();
                break;

        }
    }



    /**
     * 加载更多
     */
    protected void loadMore() {
        requestNewsList("");
    }

    /**
     * 跟踪view每一帧的变化
     */
    class FrameRunnable implements Runnable {

        private Handler handler;

        FrameRunnable(Handler handler) {
            this.handler = handler;
        }

        private boolean isRunning = false;

        public boolean isRunning() {
            return isRunning;
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }
    }

    /**
     * 获得功能列表
     */
    private void getFunctionList() {
        ArrayList<FunctionEntity> dataTmp = FunctionListData.getInstance().getData(this);
        if (dataTmp.size() != 0) {//如果有数据，则直接显示，不请求
            showFunctionPopUp();
            return;
        }
        String url = getFunctionListUrl();
        Map<String,String> map = getFunctionParameter();
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
                        functionListSuccessed(str);
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
     * 获得新闻分类的请求参数
     * @return
     */
    public abstract HashMap<String,String> getFunctionParameter();

    /**
     * 功能列表请求成功
     * @param json
     */
    private void functionListSuccessed(String json) {
        FunctionListData.getInstance().clearData();
        FunctionListData.getInstance().saveData(this, json);
        showFunctionPopUp();
    }

    /**
     * 弹出功能框
     */
    private void showFunctionPopUp() {
        final FunctionListPopUpWindow functionListPopUpWindow = new FunctionListPopUpWindow(this);
        final ArrayList<FunctionEntity> functionListData = FunctionListData.getInstance().getData(this);
        functionListPopUpWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                functionListPopUpWindow.dismiss();
                selectFunctionEntity = functionListData.get((int) l);
                setSearchEt();
                requestNewsList(selectFunctionEntity.getFunctionId());
            }
        });
        functionListPopUpWindow.showAsDropDown(searchLl);
    }

    /**
     * 设置搜索选项
     */
    private void setSearchEt() {
        if(selectFunctionEntity != null) {
            searchEt.setText(selectFunctionEntity.getName());
        }

    }

    /**
     * 获得新闻列表
     * 如果functionid为空，则说名使新的类型加载需要清空数据，否则就加载跟多
     */
    private void requestNewsList(String functionId) {
        HashMap<String,String> map = getNewsListParameter();
        if (!MyUtils.isEmpty(functionId)) {
            map.put("function_id",functionId);
            NewsListData.getInstance().clearData();
            newsAdapter.notifyDataSetChanged();
        } else {
            String fId = NewsListData.getInstance().getFunctionId(this);
            long page = NewsListData.getInstance().getPage(this);
            map.put("function_id",fId);
            map.put("page", (page + 1) + "");
        }
        requestNewsList(map);
    }
    /**
     * 获得新闻列表
     */
    private void requestNewsList(HashMap<String,String> map) {
        String url = getNewsListUrl();

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

                        if ("没有更多内容了！".equals(message)) {
                            showToast(message);
                            hideWaitDialog();
                            return;
                        }

                        NewsListData.getInstance().addData(BaseMainNewsActivity.this, str);
                        newsAdapter.notifyDataSetChanged();
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
     * 获得新闻列表的请求参数
     * @return
     */
    public abstract HashMap<String,String> getNewsListParameter();

    /**
     * 获取新闻
     * @param newsEntity
     */
    private void requestNews(NewsEntity newsEntity) {
        if (null == newsEntity) {
            return;
        }
        String url = getNewsUrl();
        HashMap<String,String> map = getNewsParameter();
        String news_id = newsEntity.getNewsId();
        map.put("news_id",news_id);
        String font_size = Mysharedperferences.getIinstance().getString(this, MoreActivity.key);
        if (MyUtils.isEmpty(font_size)) {
            font_size = MoreActivity.SMALL;
        }
        map.put("font_size",font_size);
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
                        NewsData.getInstance(BaseMainNewsActivity.this).saveData(BaseMainNewsActivity.this, str);
                        startNewsActivity();
                    } else {
                        showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Json格式错误");
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
     * 获得新闻的请求参数
     * @return
     */
    public abstract HashMap<String,String> getNewsParameter();
    /**
     * 打开新闻页
     */
    private void startNewsActivity() {
        Intent intent = new Intent();
        intent.setClass(this, NewsPageActivity.class);
        startActivity(intent);
    }

    /**
     * 获得新闻请求的url
     * @return
     */
    public abstract String getNewsUrl();

    /**
     * 获得新闻列表请求的url
     * @return
     */
    public abstract String getNewsListUrl();

    /**
     * 获得功能分类url
     * @return
     */
    public abstract String getFunctionListUrl();

    /**
     * 获得当前类名
     * @return
     */
    public abstract String getCurrentClassName();

    /**
     * 更新文本框设置
     * @param searchEt
     */
    public abstract void onUpdataSearchEt(final EditText searchEt);
}
