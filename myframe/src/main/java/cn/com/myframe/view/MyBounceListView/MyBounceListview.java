package cn.com.myframe.view.MyBounceListView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;


import cn.com.myframe.R;

/**
 * Created by pi on 15-9-4.
 */
public class MyBounceListview extends LinearLayout {
    private Context mContext;
    private ListViewForScrollView listViewForScrollView;
    public MyBounceListview(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public MyBounceListview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.view_bounce_list,this);
        listViewForScrollView = (ListViewForScrollView) view.findViewById(R.id.listViewBounce);
    }

    /**
     * 设置适配器
     * @param baseAdapter
     */
    public void setAdapter(BaseAdapter baseAdapter) {
        listViewForScrollView.setAdapter(baseAdapter);

    }

    /**
     * 设置点击事件
     * @param myOnItemClickListener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener myOnItemClickListener){
        listViewForScrollView.setOnItemClickListener(myOnItemClickListener);

    }

    /**
     * 添加页脚
     * @param v
     */
    public void addFooterView(View v) {
        if (null != v) listViewForScrollView.addFooterView(v);
    }

    /**
     * 添加头
     * @param v
     */
    public void addHeaderView(View v){
        if (null != v) listViewForScrollView.addHeaderView(v);
    }
}
