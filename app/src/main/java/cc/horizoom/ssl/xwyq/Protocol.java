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

    //已登录用户卡片列表接口
    public static final String CCCPCL = HOST + PLAT + "api.customer_card_push_content_list.php";

    //已登陆客户用户文章【内容】接口：
    public static final String CPCD = HOST + PLAT +"api.customer_push_content_detail.php";

    //登陆客户文章【列表】接口：
    public static final String CPCL = HOST + PLAT +"api.customer_push_content_list.php";

    //已登陆客户文章【分类】接口：
    public static final String CFL =  HOST + PLAT + "api.customer_function_list.php";

    //客户文章【搜索列表】接口：
    public static final String CPCSL = HOST + PLAT + "api.customer_push_content_search_list.php";
}
