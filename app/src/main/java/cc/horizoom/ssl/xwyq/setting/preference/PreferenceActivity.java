package cc.horizoom.ssl.xwyq.setting.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cc.horizoom.ssl.xwyq.DataManager.NewsListData;
import cc.horizoom.ssl.xwyq.DataManager.UserData;
import cc.horizoom.ssl.xwyq.Protocol;
import cc.horizoom.ssl.xwyq.R;
import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;
import cn.com.myframe.network.volley.VolleyError;
import cn.com.myframe.view.MyLinearLayout;

/**
 * Created by pi on 15-9-12.
 * 偏好设置
 */
public class PreferenceActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout backRl;

    private RelativeLayout complateRl;

    private MyLinearLayout myWordMll;

    private MyLinearLayout baseWordMll;

    private RelativeLayout outRl;

    private View flyView;

    private View selectView;

    private boolean isMoving = false;//是否移动

    public PreferenceActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        flyView = getWordView("xxxxx");
        backRl = (RelativeLayout) findViewById(R.id.backRl);
        outRl = (RelativeLayout) findViewById(R.id.outRl);
        complateRl = (RelativeLayout) findViewById(R.id.complateRl);
        myWordMll = (MyLinearLayout) findViewById(R.id.myWordMll);
        myWordMll.setName("myWordMll");
        baseWordMll = (MyLinearLayout) findViewById(R.id.baseWordMll);
        baseWordMll.setName("baseWordMll");
        backRl.setOnClickListener(this);
        complateRl.setOnClickListener(this);
        outRl.addView(flyView);
        flyView.setVisibility(View.GONE);
        initView();
    }

    /**
     * 初始化数据
     */
    private void initView () {
        String interests_base = Mysharedperferences.getIinstance().getString(this,"interests_base");
        String interests = Mysharedperferences.getIinstance().getString(this,"interests");
        if (!MyUtils.isEmpty(interests)) {
            String[] interestArray = interests.split(",");
            for (int i=0;i<interestArray.length;i++) {
                String str = interestArray[i];
                View view = getWordView(str);
                myWordMll.addItemView(view);
                myWordMll.addView(view);
            }
        }

        if (!MyUtils.isEmpty(interests_base)) {
            String[] interestsBaseArray = interests_base.split(",");
            for (int i=0;i<interestsBaseArray.length;i++) {
                String str = interestsBaseArray[i];
                View view = getWordView(str);
                baseWordMll.addItemView(view);
                baseWordMll.addView(view);
            }
        }
    }

    /**
     * 获得view
     * @param str
     * @return
     */
    private View getWordView (String str) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.view_word_item, null);
        Button wordTv = (Button) view.findViewById(R.id.wordBtn);
        wordTv.setText(str);
        wordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMoving) return;
                MyLinearLayout myll = (MyLinearLayout) v.getParent().getParent();
                selectView = (View) v.getParent();
                if (myll.getName().equals("myWordMll")) {
                    startMove(myll,baseWordMll,selectView);
                } else {
                    startMove(myll, myWordMll, selectView);
                }
            }
        });
        return view;
    }

    /**
     * 开始移动控件
     * @param start
     * @param end
     * @param view
     */
    private void startMove(final MyLinearLayout start,final MyLinearLayout end,View view){
        float startX,startY;
        float endX,endY;
        float flyX,flyY;
        int[] flyLocation = new int[2];
        flyView.getLocationOnScreen(flyLocation);
        flyX = flyLocation[0];
        flyY = flyLocation[1];

        int[] startLocation = new int[2];
        view.getLocationInWindow(startLocation);
        startX = startLocation[0];
        startY = startLocation[1];
        ArrayList<View> endList = end.getListView();
        if (endList != null && endList.size() > 0) {
            View endView = endList.get(endList.size()-1);
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            endView.measure(w, h);
            int height = endView.getMeasuredHeight();
            int width = endView.getMeasuredWidth();
            int[] location = new int[2];
            endView.getLocationInWindow(location);
            endX = location[0] + width;
            endY = location[1];
        } else {
            int[] location = new int[2];
            end.getLocationInWindow(location);
            endX = location[0];
            endY = location[1];
        }
        setViewName(flyView,getViewName(view));

        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        Animation translateAnimation = new TranslateAnimation(startX-flyX,endX-flyX,startY-flyY,endY-flyY);//初始化
        translateAnimation.setInterpolator(accelerateInterpolator);
        translateAnimation.setDuration(200);  //设置动画时间
        flyView.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoving = true;
                flyView.setVisibility(View.VISIBLE);
                start.removeItemView(selectView);
                start.removeView(selectView);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flyView.setVisibility(View.GONE);
                end.addItemView(selectView);
                end.addView(selectView);
                isMoving = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 获得控件的名字
     * @param view
     * @return
     */
    private String getViewName(View view) {
        Button wordTv = (Button) view.findViewById(R.id.wordBtn);
        return wordTv.getText().toString();
    }

    /**
     * 设置控件的名字
     * @param view
     * @param name
     */
    private void setViewName(View view,String name){
        Button wordTv = (Button) view.findViewById(R.id.wordBtn);
        wordTv.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backRl:
                closeActivity(PreferenceActivity.class.getName());
                break;
            case R.id.complateRl:
                requestCIS();
                break;
        }
    }

    /**
     * 请求同步
     */
    private void requestCIS() {
        String customer_id = UserData.getInstance().getCustomerId(this);
        String interests = getCISstr();
        String url = Protocol.CIS;
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("customer_id",customer_id);
        map.put("interests",interests);
        showWaitDialog();
        doRequestString(url, map, new RequestResult() {
            @Override
            public void onResponse(String str) {
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    JSONObject jsonObject = jsonArray.optJSONObject(0);
                    boolean success = jsonObject.optBoolean("success");
                    String message = jsonObject.optString("message");
                    showToast(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideWaitDialog();
            }

            @Override
            public void onErrResponse(VolleyError error) {
                hideWaitDialog();
            }
        });
    }

    /**
     * 获得同步数据
     * @return
     */
    private String getCISstr() {
        ArrayList<View> list = myWordMll.getListView();
        if (null == list || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<list.size();i++) {
            String name = getViewName(list.get(i));
            sb.append(name);
            if (i < (list.size()-1)) {
                sb.append(",");
            }
        };
        return sb.toString();
    }
}
