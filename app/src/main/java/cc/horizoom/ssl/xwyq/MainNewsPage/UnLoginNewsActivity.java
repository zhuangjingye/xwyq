package cc.horizoom.ssl.xwyq.MainNewsPage;

import android.widget.EditText;

import java.util.HashMap;

import cc.horizoom.ssl.xwyq.Protocol;

/**
 * Created by pizhuang on 2015/9/8.
 */
public class UnLoginNewsActivity extends BaseMainNewsActivity {
    @Override
    public HashMap<String, String> getFunctionParameter() {
        return new HashMap<String,String>();
    }

    @Override
    public HashMap<String, String> getNewsListParameter() {
        return new HashMap<String,String>();
    }

    @Override
    public HashMap<String, String> getNewsParameter() {
        return new HashMap<String,String>();
    }

    @Override
    public String getNewsUrl() {
        return Protocol.UNLOGINPUSHCONTENTDETAIL;
    }

    @Override
    public String getNewsListUrl() {
        return Protocol.UNLOGINPUSHCONTENTLIST;
    }

    @Override
    public String getFunctionListUrl() {
        return Protocol.UNLOGINFUNCTIONLIST;
    }

    @Override
    public String getCurrentClassName() {
        return UnLoginNewsActivity.class.getName();
    }

    @Override
    public void onUpdataSearchEt(EditText searchEt) {
        searchEt.setEnabled(false);
    }
}
