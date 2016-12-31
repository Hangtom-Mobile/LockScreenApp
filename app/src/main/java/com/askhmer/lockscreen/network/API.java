package com.askhmer.lockscreen.network;

/**
 * Created by soklundy on 5/23/2016.
 */
public class API {

    public static final String BASEURL = "http://www.medayi.com/locknet/";

    /**
     * API url
     */
    public static final String REGISTER = BASEURL + "register_form_api.php";
    public static final String LOGINPW = BASEURL + "locknet_login_pw_api.php";
    public static final String REQUESTID = BASEURL + "locknet_id_find_api.php";
    public static final String REQUESTPASSWORDSTEP1 = BASEURL + "locknet_passwd_step1_api.php";
    public static final String REQUESTPASSWORDSTEP2 = BASEURL + "locknet_passwd_step2_api.php";
    public static final String CHECKCASHSLIDEID = BASEURL + "mb_id_chk_api.php";
    public static final String LOCKSCREENBACKGROUP = BASEURL + "locknet_api.php";
    public static final String REQUESTPOINT = BASEURL + "locknet_point_api.php";
    public static final String REQUESTMYPOINT = BASEURL + "my_point_api.php";
    public static final String LOGIN = BASEURL +  "locknet_login_api.php";
    public static final String COUNTMEMBER = BASEURL + "locknet_member_count_api.php";
    public static final String REQUESTVIDEO = BASEURL +  "lock_movie.php";
    public static final String CHECKRECOMMEND = BASEURL +  "locknet_recommend_chk_api.php";
    public static final String REQUESTRECOMMENDID = BASEURL +  "locknet_recommend_api.php";
    public static final String REQUESTEXCHANGPOINT = BASEURL + "get_monry_api.php";
    public static final String REQUESTAUTOLOGIN = "http://m.medayi.com/bbs/auto_login_api.php";
    public static final String CHECKPASSWORK = "http://www.medayi.com/bbs/password_chk_api.php";
    public static final String LOSEPASSWORD = "http://www.medayi.com/bbs/password_lost_api.php";
    public static final String CHANGPASSWORD = "http://www.medayi.com/bbs/password_change_api.php";
    public static final String MEMBERINFOAPI = "http://www.medayi.com/bbs/member_info_api.php";
    public static final String CHANGLOCATION = "http://www.medayi.com/bbs/change_location_api.php";
    public static final String SMSGATEWAY = BASEURL + "locknet_smscode_api.php";
    public static final String REQUESTCOMPANYTOPUP = "http://medayi.com/locknet/locknet_topup_category_api.php";
    public static final String REQUESTDETAILTOPUP = "http://medayi.com/locknet/locknet_topup_api.php";
    public static final String REQUESTBUYTOPUPCARD = BASEURL + "locknet_topup_charge_api.php";
    public static final String MESSAGE = BASEURL + "locknet_popup_api.php";
    public static final String POINTLIST = BASEURL + "my_point_list.php";
}
