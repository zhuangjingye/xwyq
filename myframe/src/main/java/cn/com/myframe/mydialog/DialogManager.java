package cn.com.myframe.mydialog;

import android.app.Dialog;

/**
 * Created by pi on 15-6-16.
 */
public class DialogManager {

    private static volatile DialogManager dialogManager;

    private Dialog dialog;

    private DialogManager() {

    }

    public static DialogManager getInstance() {
        if (null == dialogManager) {
            synchronized (DialogManager.class) {
                if(null == dialogManager) {
                    dialogManager = new DialogManager();
                }
            }
        }
        return dialogManager;
    }

    /**
     * 隐藏当前大activity并且显示新大dialog
     * @param myBasicDialog
     */
    public void showDialog(MyBasicDialog myBasicDialog) {
        if (null != dialog) {
            if (dialog.isShowing()) {
                hideDialog();
            }
            dialog = null;
        }
        dialog = myBasicDialog;
        dialog.show();
    }

    /**
     * 隐藏并销毁dialog
     */
    public void hideDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.hide();
            dialog.cancel();
            dialog = null;
        }
    }
}
