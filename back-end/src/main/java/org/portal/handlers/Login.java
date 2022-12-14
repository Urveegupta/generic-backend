package org.portal.handlers;

import io.javalin.http.Context;
import org.portal.Const;
import org.portal.Dal;
import org.portal.db.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Login extends BaseHandler{

    public Login(){this.dal = new Dal();}

    @Override
    public void handle(Context context){
        this.ctx = context; // MUST be the first line

        log.info("Handling the login request.");
        HashMap<String, Object> userSetup = new HashMap<>();
        if (!authenticate()) {
            sendErrorJson("Failed to authenticate.");
            return;
        }
        try {
            userSetup.put(Const.KEY_USER_PROFILE, ctx.sessionAttribute(Const.USER));
            log.info(">user setup complete");
            log.info(ctx.sessionAttribute(Const.USER));
        }
        catch (Exception ex){
            log.info("Failed to create user navigation");
            log.info(ex.toString());
            sendErrorJson("Failed to create User Navigation");
        }
        ctx.result("User Logged In!");
    }

    private void sendErrorJson(String msg) {
        ctx.result(msg);
    }
    private boolean authenticate() {
        String loginId = ctx.req.getParameter(Const.KEY_LOGIN_ID);
        String passwd = ctx.req.getParameter(Const.KEY_PASSWD);
        /*
         * Check the password against DB etc.
         * Store the user profile information in session.
         */

        boolean authOk = dal.loginAuth(loginId.toLowerCase(),passwd);
        if (authOk) {
            ctx.sessionAttribute(Const.LOGGED_IN, true);
            ctx.sessionAttribute(Const.USER, dal.getUser(loginId));
        }
        return authOk;
    }
}
