package cn.com.myframe.mydialog;

import android.app.Dialog;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.R;

/**
 * Created by pi on 15-6-16.
 */
public class MyBasicDialog extends Dialog{

    protected BaseActivity baseActivity;

    public MyBasicDialog(BaseActivity baseActivity) {
        super(baseActivity, R.style.Dialog_Legend);
        this.baseActivity = baseActivity;
    }

    public MyBasicDialog( BaseActivity baseActivity,int theme) {
        super(baseActivity, theme);
        this.baseActivity = baseActivity;
    }
}
