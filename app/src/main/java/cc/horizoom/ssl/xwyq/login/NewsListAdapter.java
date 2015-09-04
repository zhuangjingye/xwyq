package cc.horizoom.ssl.xwyq.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.entity.NewEntity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15-9-3.
 * 新闻列表
 */
public class NewsListAdapter extends BaseAdapter{

    private BaseActivity baseActivity;

    private ArrayList<NewEntity> data;

    public NewsListAdapter(BaseActivity baseActivity,ArrayList<NewEntity> data) {
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
        LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
        if (view == null) {
            holderView = new HolderView();
            view = layoutInflater.inflate(R.layout.list_item_news,null);
            holderView.newsTv = (TextView) view.findViewById(R.id.newsTv);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        NewEntity newEntity = data.get(i);
        holderView.newsTv.setText(newEntity.getTitle());
        return view;
    }

    private class HolderView {
        public TextView newsTv;
    }
}
