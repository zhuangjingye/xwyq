package cn.com.myframe.view.MyBounceListView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import cn.com.myframe.MyUtils;
import cn.com.myframe.R;
import cn.com.myframe.view.BounceListView;
import cn.com.myframe.view.BounceScrollView;

/**
 * Created by pi on 15-9-4.
 */
public class MyBounceListview extends LinearLayout {
    private Context mContext;
    private ListViewForScrollView listViewForScrollView;
    private BounceScrollView bounceScrollView;
    private FrameLayout headFl;
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
        bounceScrollView = (BounceScrollView) view.findViewById(R.id.bounceScrollView);
        headFl = (FrameLayout) view.findViewById(R.id.headFl);
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
        if (null != v){
            headFl.removeAllViews();
            headFl.addView(v);
        }
    }
}
