package cn.com.myframe.popupWindow;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.R;

/**
 * Created by pizhuang on 2015/9/6.
 */
public abstract class MyPopupWindow {
    protected BaseActivity baseActivity;

    public final PopupWindow popupWindow;

    public MyPopupWindow(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        popupWindow = new PopupWindow(getContentView(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
//        popupWindow.setBackgroundDrawable(null);
        popupWindow.setBackgroundDrawable(baseActivity.getResources().getDrawable(R.drawable.sharp_rectangle_popup_bg));
    }

    /**
     * 显示
     * @param view
     */
    public void showAsDropDown(View view) {
        if (null != popupWindow) {
            popupWindow.showAsDropDown(view);
        }
    }

    /**
     * 关闭
     */
    public void dismiss() {
        if (null != popupWindow)
        popupWindow.dismiss();
    }


    public abstract View getContentView();
}
