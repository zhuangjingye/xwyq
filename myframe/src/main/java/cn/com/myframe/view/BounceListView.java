package cn.com.myframe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by pi on 15-9-2.
 * 模仿iso阻尼效果
 */
public class BounceListView extends ListView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

    private Context mContext;
    private int mMaxYOverscrollDistance;

    public BounceListView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initBounceListView();
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initBounceListView() {
        // get the density of the screen and do some maths with it on the max
        // overscroll distance
        // variable so that you get similar behaviors no matter what the screen
        // size

        final DisplayMetrics metrics = mContext.getResources()
                .getDisplayMetrics();
        final float density = metrics.density;
        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);

        this.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        try {
            Class<?> c = (Class<?>) Class.forName(AbsListView.class.getName());
            Field egtField = c.getDeclaredField("mEdgeGlowTop");
            Field egbBottom = c.getDeclaredField("mEdgeGlowBottom");
            egtField.setAccessible(true);
            egbBottom.setAccessible(true);
            Object egtObject = egtField.get(this); // this 指的是ListiVew实例
            Object egbObject = egbBottom.get(this);

            // egtObject.getClass() 实际上是一个 EdgeEffect 其中有两个重要属性 mGlow mEdge
            // 并且这两个属性都是Drawable类型
            Class<?> cc = (Class<?>) Class.forName(egtObject.getClass()
                    .getName());
            Field mGlow = cc.getDeclaredField("mGlow");
            mGlow.setAccessible(true);
            mGlow.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mGlow.set(egbObject, new ColorDrawable(Color.TRANSPARENT));

            Field mEdge = cc.getDeclaredField("mEdge");
            mEdge.setAccessible(true);
            mEdge.set(egtObject, new ColorDrawable(Color.TRANSPARENT));
            mEdge.set(egbObject, new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // This is where the magic happens, we have replaced the incoming
        // maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                mMaxYOverscrollDistance, isTouchEvent);
    }
}