package org.portal;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.portal.configs.HandlerPaths;
import org.portal.db.entities.SubmittedForm;
import org.portal.handlers.Login;
import org.portal.handlers.Rbac;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class FlowEngine {

    private static Logger log = LoggerFactory.getLogger(FlowEngine.class);
    private Javalin app;
    private Dal dal;

    public FlowEngine(Javalin app) {
        this.app = app;
        this.dal = new Dal();
    }

    public void start() throws IOException, ParseException {
        log.info(">Starting the portal flow engine");

//        app._conf.addStaticFiles(cfg->{
//            cfg.hostedPath="/";
////            cfg.directory = "/static_content";
//            // Location.CLASSPATH (jar) or Location.EXTERNAL (file system)
//            cfg.location = Location.CLASSPATH;
//            // if the files should be pre-compressed and cached in memory (optimization)
//            cfg.precompress = false;
//        });

//        app._conf.accessManager(Rbac::accessManager);
        app.before("/secure/*", new Rbac());
        app.post("login", new Login());
        app.post("newUser", (ctx)->{
            JSONObject obj = ctx.bodyAsClass(JSONObject.class);
            dal.addUser(obj.get("name").toString(),obj.get("email").toString(),obj.get("password").toString());
            ctx.result("User Added Successfully!");
        });

        // add handlers for all forms in the portal
        List<JSONObject> list = dal.loadForms();
        //view all forms
        app.get(HandlerPaths.PATH_TO_BROWSE_FORMS, ctx -> {
            log.info("GET handler for browsing available forms");
            ctx.json(list);
        });

        for(JSONObject form: list){
            addFormHandlers(form);
        }

        // add handler for all submitted forms (going through workflow)
        List<SubmittedForm> submittedList = dal.loadSubmittedForms();
        for(SubmittedForm form: submittedList){
            addSubmittedFormHandlers(form);
        }
    }

    private void addFormHandlers(JSONObject form){

        // handler to fill out the form
        app.post(form.get("form_name")+"/"+ HandlerPaths.PATH_TO_FILL_FORM, (ctx)->{
            log.info("POST handler for filling out form: "+ form.get("form_name"));
            // TODO: check if allowed
            // TODO: do
            // get field infos
            String data = ctx.req.getParameter("data");
            dal.addSubmission(form, data);
            ctx.result("Submitted Successfully!");
        });
    }

    private void addSubmittedFormHandlers(SubmittedForm form) throws IOException, ParseException {
        String form_name = dal.getFormName(form);
        String SubmittedFormId = dal.getSubmittedFormId(form);

        // handler to view status
        app.get(form_name+"/"+SubmittedFormId,ctx -> {
            //TODO: check if allowed
            int status = form.getStatusId();
            ctx.result(dal.loadStatusFromId(status));
        });

//        // handler to update status
        app.post(form_name+"/"+SubmittedFormId+"/"+HandlerPaths.PATH_TO_UPDATE_FORM, (ctx) -> {
            //TODO: check if allowed
            //TODO: do
        });
    }
}
