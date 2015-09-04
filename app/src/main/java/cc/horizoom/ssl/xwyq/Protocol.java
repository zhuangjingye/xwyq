package cc.horizoom.ssl.xwyq;

/**
 * Created by pi on 15-9-3.
 * 接口
 */
public class Protocol {

    public static final String HOST = "http://api-mobile.horizoom.cc/";

    //注册
    public static final String APPLYACCOUNT = HOST + "private/xwyq_iphone/api.customer_apply_account.php";

    //未登陆用户文章【分类】接口
    public static final String UNLOGINFUNCTIONLIST = HOST + "private/xwyq_andriod_phone/api.unlogin_function_list.php";
    //未登陆用户文章【列表】接口：
    public static final String UNLOGINPUSHCONTENTLIST = HOST + "private/xwyq_iphone/api.unlogin_push_content_list.php";
}
