package cc.horizoom.ssl.xwyq.DataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.myframe.BaseActivity;
import cn.com.myframe.MyUtils;
import cn.com.myframe.Mysharedperferences;

/**
 * Created by pi on 15-9-3.
 * 用户信息管理
 */
public class UserData implements DataInterface{
    private static UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }

    private String key = UserData.class.getName();

    private String customerId;//用户id

    private String userName;//用户名

    private String realName;//用户全名

    private String portrait;//头像

    private String microblog;

    private String micromessage;

    private String email;

    private String qq;

    private String telephone;

    private String mobile;

    private String country;

    private String province;

    private String city;

    private String address;

    private String post_code;

    private String company;

    private String summary;

    private String salesman;

    private String register_time;

    private String status;



    /**
     * 保存数据
     * @param baseActivity
     * @param json
     */
    public void saveData(BaseActivity baseActivity,String json) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,json);
        analyze(baseActivity);
    }

    @Override
    public void clearData() {
    }

    /**
     * 解析数据
     */
    public void analyze(BaseActivity baseActivity) {
        String json = Mysharedperferences.getIinstance().getString(baseActivity,key);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.optJSONObject(0);
            customerId = jsonObject.optString("customer_id");
            userName = jsonObject.optString("username");
            realName = jsonObject.optString("realname");
            portrait = jsonObject.optString("portrait");
            microblog = jsonObject.optString("microblog");
            micromessage = jsonObject.optString("micromessage");
            email = jsonObject.optString("email");
            qq = jsonObject.optString("qq");
            telephone = jsonObject.optString("telephone");
            mobile = jsonObject.optString("mobile");
            country = jsonObject.optString("country");
            province = jsonObject.optString("province");
            city = jsonObject.optString("city");
            address = jsonObject.optString("address");
            post_code = jsonObject.optString("post_code");
            company = jsonObject.optString("company");
            summary = jsonObject.optString("summary");
            salesman = jsonObject.optString("salesman");
            register_time = jsonObject.optString("register_time");
            status = jsonObject.optString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearSaveData(BaseActivity baseActivity) {
        Mysharedperferences.getIinstance().putString(baseActivity,key,"");
        ourInstance = new UserData();
    }

    public String getCustomerId(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return customerId;
    }

    public String getUserName(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return userName;
    }

    public String getRealName(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return realName;
    }

    public String getPortrait(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return portrait;
    }

    public String getMicroblog(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return microblog;
    }

    public String getMicromessage(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return micromessage;
    }

    public String getEmail(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return email;
    }

    public String getQq(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return qq;
    }

    public String getTelephone(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return telephone;
    }

    public String getMobile(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return mobile;
    }

    public String getCountry(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return country;
    }

    public String getProvince(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return province;
    }

    public String getCity(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return city;
    }

    public String getAddress(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return address;
    }

    public String getPost_code(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return post_code;
    }

    public String getCompany(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return company;
    }

    public String getSummary(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return summary;
    }

    public String getSalesman(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return salesman;
    }

    public String getRegister_time(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return register_time;
    }

    public String getStatus(BaseActivity baseActivity) {
        if (MyUtils.isEmpty(customerId)) {
            analyze(baseActivity);
        }
        return status;
    }
}

//[
//        {
//        "success": "true",
//        "message": "登陆成功！",
//        "customer_id": "49",
//        "username": "hsz",
//        "portrait": "http://fileimage.horizoom.cc/customer/20150522/143226336177portrait.jpg",
//        "realname": "中国红十字会",
//        "microblog": "",
//        "micromessage": "",
//        "email": "hsz@hsz.com",
//        "qq": "",
//        "telephone": "",
//        "mobile": "",
//        "country": "",
//        "province": "",
//        "city": "",
//        "address": "",
//        "post_code": "",
//        "company": "",
//        "summary": "中国红十字会",
//        "salesman": "syt",
//        "register_time": "1431673656",
//        "status": "1"
//        }
//        ]