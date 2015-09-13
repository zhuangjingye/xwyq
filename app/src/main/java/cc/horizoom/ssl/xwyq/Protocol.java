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

    //收藏
    public static final String CFO = HOST + PLAT + "api.customer_favorite_operate.php";

    //已登陆客户【意见反馈】接口：
    public static final String CFB = HOST + PLAT + "api.customer_feedback.php";

    //已登陆客户【用户收藏之 收藏列表】接口：
    public static final String CFR = HOST + PLAT + "api.customer_favorite_news_list.php";

    //预警参数
    public static final String CWP = HOST + PLAT + "api.customer_warning_parameter.php";

    //已登陆客户【预警文章列表】接口：
    public static final String CWPCL = HOST + PLAT + "api.customer_warning_push_content_list.php";

    //已登陆客户【偏好设置之 偏好词库】接口： 基础词汇
    public static final String CIB = HOST + PLAT +"api.customer_interests_base.php";

    //已登陆客户【客户偏好词列表】接口：已选词汇
    public static final String CI = HOST + PLAT +"api.customer_interests.php";

    //已登陆客户【客户偏好词同步】接口：
    public static final String CIS = HOST + PLAT +"api.customer_interests_sync.php";

    //柱状图
    public static final String WC = "http://yqzx.horizoom.cc/mobile/warning_chart.php";
}
