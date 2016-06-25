package com.askhmer.mobileapp.network;

/**
 * Created by soklundy on 5/23/2016.
 */
public class API {

    public static final String BASEURL = "http://www.medayi.com/locknet/";

    /**
     * API url
     */
    public static final String REGISTER = BASEURL + "register_form_api.php";
    public static final String CHECKCASHSLIDEID = BASEURL + "mb_id_chk_api.php";
    public static final String LOCKSCREENBACKGROUP = BASEURL + "locknet_api.php";
    public static final String REQUESTPOINT = BASEURL + "locknet_point_api.php";
    public static final String REQUESTMYPOINT = BASEURL + "my_point_api.php";
    public static final String REQUESTAUTOLOGIN = "http://www.medayi.com/bbs/auto_login_api.php";
    public static final String CHECKPASSWORK = "http://www.medayi.com/bbs/password_chk_api.php";
}
