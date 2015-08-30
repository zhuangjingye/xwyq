package cn.com.myframe.mydialog.dialog;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.R;
import cn.com.myframe.mydialog.MyBasicDialog;

/**
 * Created by pizhuang on 2015/7/3.
 */
public class WaitDialog extends MyBasicDialog{
    public WaitDialog(BaseActivity baseActivity) {
        super(baseActivity);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_wait);
    }
}
