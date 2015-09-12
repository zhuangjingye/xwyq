package cn.com.myframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * Created by pizhuang on 2015/7/9.
 */
public class MyLinearLayout extends ViewGroup {
    private ArrayList<View> listView;

    private String name;

    public MyLinearLayout(Context context, AttributeSet attrs,
                          int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
        init();
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public MyLinearLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        listView = new ArrayList<View>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int x = 0;// 初始x坐标
        int y = 0;// 初始y坐标

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            child.measure(r - l, b - t);
            if (x + child.getMeasuredWidth() <= r) {
                child.layout(x, y, child.getMeasuredWidth() + x,
                        child.getMeasuredHeight() + y);
                x += child.getMeasuredWidth();
            } else {
                x = 0;
                y += child.getMeasuredHeight();
                child.layout(x, y, child.getMeasuredWidth() + x,
                        child.getMeasuredHeight() + y);
                x += child.getMeasuredWidth();
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec)-10;

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int cCount = getChildCount();

        int width = 0;// 初始x坐标
        int height = 0;// 初始y坐标
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (height == 0) {
                height += child.getMeasuredHeight();
            }
            if (width + child.getMeasuredWidth() < sizeWidth) {
                width += child.getMeasuredWidth();
            } else {
                width = 0;
                width += child.getMeasuredWidth();
                height += child.getMeasuredHeight();
            }

        }
        /**
         * 如果是wrap_content设置为我们计算的值 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(sizeWidth, height);
    }

    /**
     * 添加view
     *
     * @param view
     */
    public void addItemView(View view) {
        listView.add(view);
    }

    /**
     * 移除
     * @param view
     */
    public void removeItemView(View view) {
        listView.remove(view);
    }

    /**
     * 获得控件列表
     * @return
     */
    public ArrayList<View> getListView() {
        return listView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
