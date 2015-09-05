package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.NewListUnLoginData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.login.NewsListAdapter;
import cn.com.myframe.MyUtils;
import cn.com.myframe.view.BounceScrollView;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * Created by pi on 15-9-4.
 */
public class BaseMainNewsActivity extends MyBaseActivity implements View.OnClickListener{

    private MyBounceListview myListView;

    private ArrayList<NewEntity> data;

    private NewsListAdapter newsListAdapter;

    private ImageView newHeadImg;//list到头图片

    private int originalHeadBgIvHeight;//背景imageview默认初始化高度

    private Bitmap headBitmap;//头图像

    private NewsAdapter newsAdapter;//新闻适配起

    private RelativeLayout titleRl;//标题栏

    private RelativeLayout titleBackRl;//标题栏返回

    private RelativeLayout titleListRl;//标题栏

    private RelativeLayout titleListBackRl;//标题栏返回

    private View headView;//头

    private BounceScrollView.BounceScrollViewListener bounceScrollViewListener
            = new BounceScrollView.BounceScrollViewListener() {
        @Override
        public void onScroll(int offset) {
            MyUtils.log(BaseMainNewsActivity.class,"offset="+offset);
            if (offset >= 0) {
                scrollDown(offset);
            } else {
//                scrollUp(offset);
            }
        }

        @Override
        public void onViewChange() {
            updataView();
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main_news);
        myListView = (MyBounceListview) findViewById(R.id.myListView);
        titleRl = (RelativeLayout) findViewById(R.id.titleRl);
        titleRl.setVisibility(View.VISIBLE);
        titleBackRl = (RelativeLayout) findViewById(R.id.titleBackRl);
        titleBackRl.setOnClickListener(this);
        data = new ArrayList<>();
        newsAdapter = new NewsAdapter(this,data);
        myListView.setAdapter(newsAdapter);
        headBitmap = resizeImage(getResources().getDrawable(R.mipmap.bg_list01));
        headView = getHeadview(headBitmap);
        myListView.addHeaderView(headView);
        myListView.addFooterView(getFooter());
        myListView.setBounceScrollViewListener(bounceScrollViewListener);

        updataView();
    }

    /**
     * 更新页面显示
     */
    private void updataView() {
        int[] titleRlPosition = new int[2];
        int[] titleListRlPosition = new int[2];
        titleRl.getLocationOnScreen(titleRlPosition);
        titleListRl.getLocationOnScreen(titleListRlPosition);
        if (titleListRlPosition[1] < titleRlPosition[1]) {
            titleRl.setVisibility(View.INVISIBLE);
            titleListRl.setVisibility(View.VISIBLE);
        } else {
            titleRl.setVisibility(View.VISIBLE);
            titleListRl.setVisibility(View.INVISIBLE);
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
        originalHeadBgIvHeight = newHeadImg.getLayoutParams().height;
        titleListRl = (RelativeLayout) v.findViewById(R.id.titleListRl);
        titleListRl.setVisibility(View.INVISIBLE);
        newHeadImg.setImageBitmap(headBitmap);
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

    /**
     * 向下移动
     * @param offset
     */
    private void scrollDown(int offset) {
        ViewGroup.LayoutParams layoutParams = newHeadImg.getLayoutParams();
        layoutParams.height = originalHeadBgIvHeight+offset;
        MyUtils.log(BaseMainNewsActivity.class,"layoutParams.height="+layoutParams.height);
        newHeadImg.setLayoutParams(layoutParams);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loadTv://加载更多
                loadMore();
                break;
            case R.id.titleBackRl:
                closeActivity(BaseMainNewsActivity.class.getName());
                break;

        }
    }

    /**
     * 加载更多
     */
    private void loadMore() {
        ArrayList<NewEntity> dataTmp = NewListUnLoginData.getInstance().getNewsData(this);
        data.addAll(dataTmp);
        newsAdapter.notifyDataSetChanged();
    }
}
