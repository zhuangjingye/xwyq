package cn.com.myframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import cn.com.myframe.MyUtils;

/**
 * Created by pi on 15-9-2.
 * 模仿iso阻尼效果
 */
public class BounceListView extends ListView {
    private float mLastDownY = 0f;
    private int mDistance = 0;
    private Context context;
    private View lastItem;
    public BounceListView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BounceListView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public BounceListView (Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean resutl = super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //手指按下时触发
                break;

            case MotionEvent.ACTION_CANCEL:
                break;

            case MotionEvent.ACTION_UP: //手指抬起之后触发

                break;

            case MotionEvent.ACTION_MOVE:  //手指按下之后滑动触发
                int top = getTop();
                int letf = getLeft();
                int bottom = getBottom();
                int rihgt = getRight();
                MyUtils.log(BounceListView.class,"top="+top
                        +"  letf="+letf
                        +"  bottom="+bottom
                        +"  rihgt="+rihgt);

                break;
        }
        return resutl;
    }

}