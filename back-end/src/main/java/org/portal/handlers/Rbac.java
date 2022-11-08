package org.portal.handlers;

import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Set;

public class Rbac extends BaseHandler{

    @Override
    public void handle(Context ctx) throws Exception {
        log.info(">Performing role based access control checks.");
        // TODO: Check the access for this user

        boolean allowed = false;
        if (!allowed) {
            ctx.result("Access not allowed!");
        }
    }

//    public static void accessManager(Handler handler,Context ctx, Set<RouteRole> permittedRoles){
//        if(permittedRoles.contains(Role.ANYONE)) {
//            handler.handle(ctx)
//            ctx.userRoles.any { it in permittedRoles } -> handler.handle(ctx)
//            else -> {
//                ctx.header(Header.WWW_AUTHENTICATE, "Basic")
//                throw UnauthorizedResponse();
//            }
//        }
//    }
}
