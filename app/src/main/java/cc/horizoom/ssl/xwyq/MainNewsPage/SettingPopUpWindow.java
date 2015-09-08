package cc.horizoom.ssl.xwyq.MainNewsPage;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cc.horizoom.ssl.xwyq.DataManager.FunctionListData;
import cc.horizoom.ssl.xwyq.DataManager.entity.FunctionEntity;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.popupWindow.MyPopupWindow;

/**
 * Created by pizhuang on 2015/9/6.
 * 设置功能接口
 */
public class SettingPopUpWindow extends MyPopupWindow implements View.OnClickListener{

    private RelativeLayout waringRl;//预警

    private RelativeLayout preferenceRl;//偏好设置

    private RelativeLayout collectionRl;//收藏

    private RelativeLayout moreRl;//更多设置

    public SettingPopUpWindow(BaseActivity baseActivity) {
        super(baseActivity);
    }

    @Override
    public View getContentView() {
        LayoutInflater layoutInflater = LayoutInflater.from(baseActivity);
        View contentView = layoutInflater.inflate(R.layout.setting_popup_window,null);
        waringRl = (RelativeLayout) contentView.findViewById(R.id.waringRl);//预警
        preferenceRl = (RelativeLayout) contentView.findViewById(R.id.preferenceRl);//偏好设置
        collectionRl = (RelativeLayout) contentView.findViewById(R.id.collectionRl);//收藏
        moreRl = (RelativeLayout) contentView.findViewById(R.id.moreRl);//更多设置
        waringRl.setOnClickListener(this);
        preferenceRl.setOnClickListener(this);
        collectionRl.setOnClickListener(this);
        moreRl.setOnClickListener(this);
        return contentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.waringRl:
                break;
            case R.id.preferenceRl:
                break;
            case R.id.collectionRl:
                break;
            case R.id.moreRl:
                break;
        }
    }
}
