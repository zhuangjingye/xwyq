package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.NewListUnLoginData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cc.horizoom.ssl.xwyq.MyBaseActivity;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.login.NewsListAdapter;
import cn.com.myframe.MyUtils;
import cn.com.myframe.view.MyBounceListView.MyBounceListview;

/**
 * Created by pi on 15-9-4.
 */
public class BaseMainNewsActivity extends MyBaseActivity{

    private MyBounceListview myListView;

    private ArrayList<NewEntity> data;

    private NewsListAdapter newsListAdapter;

    private ImageView newHeadImg;//list到头图片

    private Bitmap headBitmap;//头图像

    private NewsAdapter newsAdapter;//新闻适配起

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_main_news);
        data = new ArrayList<>();
        newsAdapter = new NewsAdapter(this,data);
        myListView = (MyBounceListview) findViewById(R.id.myListView);
        myListView.setAdapter(newsAdapter);
        headBitmap = resizeImage(getResources().getDrawable(R.mipmap.bg_list01));
        myListView.addHeaderView(getHeadview(headBitmap));
        myListView.addFooterView(getFooter());
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
        loadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

}
