package cc.horizoom.ssl.xwyq.dialog;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cc.horizoom.ssl.xwyq.R;
import cc.horizoom.ssl.xwyq.boot.BootActivity;
import cc.horizoom.ssl.xwyq.login.RegistActivity;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.mydialog.MyBasicDialog;

/**
 * Created by pi on 15-9-3.
 * 申请账号对话框
 */
public class ApplyDialog extends MyBasicDialog {

    private ImageView iconIv;//图标

    private TextView titleTv;//标题

    private TextView contentTv;//内容

    private Button okBtn;//确认按钮

    private boolean result;//请求结果

    private String message;//消息

    public ApplyDialog(BaseActivity baseActivity,boolean result,String message) {
        super(baseActivity);
        setContentView(R.layout.dialog_apply);
        this.result = result;
        this.message = message;
        iconIv = (ImageView) findViewById(R.id.iconIv);//图标
        titleTv = (TextView) findViewById(R.id.titleTv);//标题
        contentTv = (TextView) findViewById(R.id.contentTv);//内容
        okBtn = (Button) findViewById(R.id.okBtn);//确认按钮
        if (result) {
            updateViewOk();
        } else {
            updateViewNo();
        }
    }

    /**
     * 失败页面
     */
    private void updateViewNo() {
        iconIv.setImageResource(R.mipmap.icon_apply_no);
        titleTv.setText(baseActivity.getString(R.string.apply_no));
        contentTv.setText(message);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity.hideDialog();
            }
        });
    }

    /**
     * 成功页面
     */
    private void updateViewOk() {
        iconIv.setImageResource(R.mipmap.icon_apply_ok);
        titleTv.setText(baseActivity.getString(R.string.apply_ok));
        contentTv.setText(message);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity.hideDialog();
                baseActivity.closeActivity(RegistActivity.class.getName());
            }
        });
    }

}
