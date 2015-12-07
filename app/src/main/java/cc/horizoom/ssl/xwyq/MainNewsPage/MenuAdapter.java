package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.MenuListData;
import cc.horizoom.ssl.xwyq.DataManager.entity.CardEntity;
import cc.horizoom.ssl.xwyq.DataManager.entity.NewsEntity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;

/**
 * Created by pi on 15/12/5.
 * 菜单适配器
 */
public class MenuAdapter extends BaseAdapter {

    protected ArrayList<CardEntity> data;

    protected BaseActivity baseActivity;

    public MenuAdapter(BaseActivity baseActivity,ArrayList<CardEntity> data) {
        this.baseActivity = baseActivity;
        this.data = data;
    }

    public void setData(ArrayList<CardEntity> data) {
        this.data = data;
        notifyDataSetChanged();
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
            view = layoutInflater.inflate(R.layout.activity_menu_list_item,null);
            holderView.classificationTv = (TextView) view.findViewById(R.id.classificationTv);
            holderView.titleTv = (TextView) view.findViewById(R.id.titleTv);
            holderView.sourceTv = (TextView) view.findViewById(R.id.sourceTv);
            holderView.itemClickTv = (TextView) view.findViewById(R.id.itemClickTv);
            holderView.timeTv = (TextView) view.findViewById(R.id.timeTv);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }
        CardEntity cardEntity = data.get(i);
        holderView.classificationTv.setText(cardEntity.getName());

        if (cardEntity.getNewsData() == null || cardEntity.getNewsData().size() == 0) {
            return view;
        }
        NewsEntity newsEntity = cardEntity.getNewsData().get(0);
        holderView.titleTv.setText(newsEntity.getTitle());
        holderView.timeTv.setText(newsEntity.getPublishTime());
        holderView.sourceTv.setText(newsEntity.getSource());
        holderView.itemClickTv.setTag(cardEntity);
        holderView.itemClickTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardEntity cardEntity = (CardEntity) view.getTag();
                startMainNewsPageActivity(cardEntity);
            }
        });
        return view;
    }

    /**
     * 打开主新闻卡片页
     * @param cardEntity
     */
    private void startMainNewsPageActivity(CardEntity cardEntity) {
        Intent intent = new Intent();
        intent.setClass(baseActivity,MainNewsPageActivity.class);
        MenuListData.getInstance().setSelectedCardEntity(cardEntity);
        baseActivity.startActivity(intent);
        baseActivity.closeActivity(MenuListActivity.class.getName());
    }

    class HolderView {
        TextView classificationTv;//分类
        TextView titleTv;//标题
        TextView sourceTv;//来源
        TextView timeTv;//时间
        TextView itemClickTv;//点击控件
    }
}
