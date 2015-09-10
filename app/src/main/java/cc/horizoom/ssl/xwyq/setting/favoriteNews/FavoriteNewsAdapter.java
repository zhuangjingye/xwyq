package cc.horizoom.ssl.xwyq.setting.favoriteNews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsAdapter;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsPageActivity;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.setting.more.MoreActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.deletelistview.SlideView;

/**
 * Created by pizhuang on 2015/9/8.
 */
public class FavoriteNewsAdapter extends NewsAdapter implements SlideView.OnSlideListener{
    public FavoriteNewsAdapter(BaseActivity baseActivity, ArrayList<NewsEntity> data) {
        super(baseActivity, data);
    }

    private SlideView mLastSlideViewWithStatusOn;

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        SlideView slideView;
        if (null == view) {
            LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
            View newsView = layoutInflater.inflate(R.layout.view_news_item, null);
            slideView = new SlideView(baseActivity);
            slideView.setContentView(newsView);
            slideView.scrollTo(MyUtils.dipToPx(baseActivity, 120), 0);
            holderView = new HolderView(slideView);
            slideView.setOnSlideListener(this);
            slideView.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
            slideView = holderView.sildeView;
        }
        NewsEntity newsEntity = data.get(i);
        newsEntity.slideView = slideView;
        newsEntity.slideView.shrink();

//        holderView.numTv.setText(i+"");
        holderView.contentTv.setText(newsEntity.getTitle());
        holderView.timeTv.setText(newsEntity.getPublishTime());
        holderView.contentTv.setTextColor(baseActivity.getResources().getColor(R.color.c_02070b));
        if (newsEntity.getPupushLevel() >= 5) {
            holderView.smallBellIv.setVisibility(View.VISIBLE);
        } else {
            holderView.smallBellIv.setVisibility(View.GONE);
        }
        holderView.outLl.setTag(newsEntity);
        holderView.deleteHolder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(baseActivity, "删除第" + i + "个条目", Toast.LENGTH_LONG).show();
            }
        });
        return newsEntity.slideView;
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    class HolderView {
        public LinearLayout outLl;//外部控键
        public TextView numTv;//序号
        public TextView contentTv;//内容
        public TextView timeTv;//时间
        public ImageView smallBellIv;//铃铛
        public ViewGroup deleteHolder;
        public SlideView sildeView;
        HolderView(SlideView sildeView) {
            this.sildeView = sildeView;
            outLl = (LinearLayout) sildeView.findViewById(R.id.outLl);
            contentTv = (TextView) sildeView.findViewById(R.id.contentTv);
            numTv = (TextView) sildeView.findViewById(R.id.numTv);
            timeTv = (TextView) sildeView.findViewById(R.id.timeTv);
            smallBellIv = (ImageView) sildeView.findViewById(R.id.smallBellIv);
            deleteHolder = (ViewGroup)sildeView.findViewById(R.id.holder);
        }
    }

}
