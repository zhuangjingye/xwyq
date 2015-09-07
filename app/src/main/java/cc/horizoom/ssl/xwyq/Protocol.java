package cc.horizoom.ssl.xwyq;

/**
 * Created by pi on 15-9-3.
 * 接口
 */
public class Protocol {

    public static final String HOST = "http://api-mobile.horizoom.cc/";

    public static final String PLAT = "private/xwyq_iphone/";

    //注册
    public static final String APPLYACCOUNT = HOST + PLAT + "api.customer_apply_account.php";

    //登录
    public static final String LOGIN = HOST+"public/api.customer_login.php";

    //未登陆用户文章【分类】接口
    public static final String UNLOGINFUNCTIONLIST = HOST + PLAT + "api.unlogin_function_list.php";

    //未登陆用户文章【列表】接口：
    public static final String UNLOGINPUSHCONTENTLIST = HOST + PLAT + "api.unlogin_push_content_list.php";

    //未登陆用户文章【内容】接口：
    public static final String UNLOGINPUSHCONTENTDETAIL = HOST + PLAT + "api.unlogin_push_content_detail.php";
}
