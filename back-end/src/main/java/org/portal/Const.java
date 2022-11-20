package org.portal;

/**
 * Define all constants in this class.
 * @author Balwinder Sodhi
 */
public class Const {

    public static final String USER = "KEY_USER";
    public static final String KEY_LOGIN_ID = "loginId";
    public static final String KEY_PASSWD = "password";
    public static final String KEY_NAV_INFO = "nav";
    public static final String KEY_USER_PROFILE = "userProfile";

    // form queries
    public static final String KEY_FORM_ID = "form_id";
    public static final String KEY_FORM_NAME = "form_name";
    public static final String KEY_STATUS_ID = "id";
    public static final String KEY_STATUS_NAME = "status";

    // file paths
    static String rel_path = "back-end/src/main/java/org/portal/configs/data/";
    public static final String ACTION_PATH = rel_path + "action.json";
    public static final String ROLE_PATH = rel_path + "actionPermission.json";
    public static final String STATUS_PATH = rel_path + "role.json";
    public static final String FORM_PATH = rel_path + "forms.json";
    public static final String TRANSITIONS_PATH = rel_path + "transition.json";
    public static final String PERMISSIONS_PATH = rel_path + "actionPermission.json";

}
