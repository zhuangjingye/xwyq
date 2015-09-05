package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15-9-5.
 * 新闻适配器
 */
public class NewsAdapter extends BaseAdapter{

    private ArrayList<NewEntity> data;

    private BaseActivity baseActivity;

    public NewsAdapter(BaseActivity baseActivity,ArrayList<NewEntity> data) {
        this.baseActivity = baseActivity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HolderView holderView;
        if (null == view) {
            holderView = new HolderView();
            LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
            view = layoutInflater.inflate(R.layout.view_news_item,null);
            holderView.outLl = (LinearLayout) view.findViewById(R.id.outLl);
            holderView.contentTv = (TextView) view.findViewById(R.id.contentTv);
            holderView.numTv = (TextView) view.findViewById(R.id.numTv);
            holderView.timeTv = (TextView) view.findViewById(R.id.timeTv);
            holderView.smallBellIv = (ImageView) view.findViewById(R.id.smallBellIv);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        NewEntity newEntity = data.get(i);
        holderView.numTv.setText(i+"");
        holderView.contentTv.setText(newEntity.getTitle());
        holderView.timeTv.setText(newEntity.getPublishTime());
        return view;
    }

    class HolderView {
        LinearLayout outLl;//外部控键
        TextView numTv;//序号
        TextView contentTv;//内容
        TextView timeTv;//时间
        ImageView smallBellIv;//铃铛
    }
}
