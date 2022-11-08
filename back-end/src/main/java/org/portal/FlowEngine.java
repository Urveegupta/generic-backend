package org.portal;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.portal.configs.HandlerPaths;
import org.portal.db.entities.Form;
import org.portal.handlers.Login;
import org.portal.handlers.Rbac;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FlowEngine {

    private static Logger log = LoggerFactory.getLogger(FlowEngine.class);
    private Javalin app;
    private Dal dal;

    public FlowEngine(Javalin app) {
        this.app = app;
        this.dal = new Dal();
    }

    public void start(){
        log.info(">Starting the portal flow engine");

        app._conf.addStaticFiles(cfg->{
            cfg.hostedPath="/";
            cfg.directory = "/static_content";
            // Location.CLASSPATH (jar) or Location.EXTERNAL (file system)
            cfg.location = Location.CLASSPATH;
            // if the files should be pre-compressed and cached in memory (optimization)
            cfg.precompress = false;
        });

//        app._conf.accessManager(Rbac::accessManager);
        app.before("/secure/*", new Rbac());
        app.post("login", new Login());

        // add handlers for all forms
        List<Form> list = dal.loadForms();
        for(Form form: list){
            addFormHandlers(form);
        }
    }

    private void addFormHandlers(Form form){

        // handler to fill out the form
        app.post(form.getFormName()+"/"+ HandlerPaths.PATH_TO_FILL_FORM, (ctx)->{
            log.info("POST handler for filling out form: "+ form.getFormName());
            // TODO: do
            ctx.result("Submitted Successfully!");
        });

        // handler to view status of own filled forms
        app.get(form.getFormName()+"/"+ HandlerPaths.PATH_TO_VIEW_FORM, (ctx) -> {
            log.info("GET handler to view status of own submitted forms");
            // TODO: do

        });

    }
}
