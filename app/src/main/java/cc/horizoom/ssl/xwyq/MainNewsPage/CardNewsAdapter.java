package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsData;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.MainNewsPage.NewsAdapter;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.network.volley.VolleyError;

/**
 * Created by pizhuang on 2015/9/8.
 */
public class CardNewsAdapter extends NewsAdapter implements View.OnClickListener{
    public CardNewsAdapter(BaseActivity baseActivity, ArrayList<NewsEntity> data) {
        super(baseActivity, data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (null == view) {
            holderView = new HolderView();
            LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
            view = layoutInflater.inflate(R.layout.view_news_card_item,null);
            holderView.outLl = (LinearLayout) view.findViewById(R.id.outLl);
            holderView.contentTv = (TextView) view.findViewById(R.id.contentTv);
            holderView.numTv = (TextView) view.findViewById(R.id.numTv);
            holderView.timeTv = (TextView) view.findViewById(R.id.timeTv);
            holderView.smallBellIv = (ImageView) view.findViewById(R.id.smallBellIv);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        NewsEntity newsEntity = data.get(i);
//        holderView.numTv.setText(i+"");
        holderView.contentTv.setText(newsEntity.getTitle());
        holderView.timeTv.setText(newsEntity.getPublishTime());
        holderView.contentTv.setTextColor(baseActivity.getResources().getColor(R.color.c_fafafa));
        if (newsEntity.getPupushLevel() >= 5) {
            holderView.smallBellIv.setVisibility(View.VISIBLE);
        } else {
            holderView.smallBellIv.setVisibility(View.GONE);
        }
        holderView.outLl.setTag(newsEntity);
        holderView.outLl.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.outLl) {
            NewsEntity newsEntity = (NewsEntity) view.getTag();
            requestNews(newsEntity);
        }
    }

    /**
     * 获取新闻
     * @param newsEntity
     */
    private void requestNews(NewsEntity newsEntity) {
        if (null == newsEntity) {
            return;
        }
        String url = Protocol.CPCD;
        HashMap<String,String> map = new HashMap<String,String>();
        String news_id = newsEntity.getNewsId();
        map.put("news_id",news_id);
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
                        NewsData.getInstance(baseActivity).saveData(baseActivity, str);
                        Intent intent = new Intent();
                        intent.setClass(baseActivity, NewsPageActivity.class);
                        baseActivity.startActivity(intent);
                    } else {
                        baseActivity.showToast(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                baseActivity.hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                baseActivity.hideWaitDialog();
            }
        });
    }
}
