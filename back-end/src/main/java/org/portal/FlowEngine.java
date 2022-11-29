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

import org.portal.db.entities.User;

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
        // add handlers
        for(JSONObject form: list){
            addFormHandlers(form);
        }

        // add handler for all submitted forms (going through workflow)
        
        List<SubmittedForm> submittedList = dal.loadSubmittedForms();
        for(SubmittedForm form: submittedList){
            addSubmittedFormHandlers(form);
        }
    }

    private void addFormHandlers(JSONObject form) throws IOException, ParseException{
        log.info("adding handlers for form"+ form.get("form_name"));
        
        // handler to fill out the form
        app.post(form.get("form_name")+"/"+ HandlerPaths.PATH_TO_FILL_FORM, (ctx)->{
            log.info("POST handler for filling out form: "+ form.get("form_name"));
            // TODO: check if allowed
            // TODO: do
            // get field infos
            String data = ctx.req.getParameter("data");
            log.info("data in req");
            dal.addSubmission(form, ctx);
            ctx.result("Submitted Successfully!");
        });

        app.get(form.get("form_name")+"/view", (ctx) -> {
            // User currUser = ctx.sessionAttribute(Const.KEY_USER_PROFILE);
            // int roleId = currUser.getRoleId();

            // get all (say) leave forms that have been submitted
            List<Object> allSubmittedForms = dal.getAllSubmittedForms((String)form.get("form_name"));
            JSONObject result = new JSONObject();
            for(int i=0; i<allSubmittedForms.size(); i++)
            {
                Object sForm = (Object)allSubmittedForms.get(i);
                // convert this to (say) leave type

                // get object form_id
                int status = sForm.getStatusId();
                if(dal.checkActionPermission(status, 3, status, 1))
                {
                    // form displayed on UI for the user
                    result.put(sForm.getFormId(), sForm);
                }
            }

            ctx.json(result);
        });
    }

    private void addSubmittedFormHandlers(SubmittedForm form) throws IOException, ParseException {
        String form_name = dal.getFormName(form);
        String SubmittedFormId = dal.getSubmittedFormId(form);

        // handler to view status
        app.get(form_name+"/"+SubmittedFormId,ctx -> {
            int status = form.getStatusId();
            //TODO: add current user role_id
            // action_id for view is set to be 3
            if(dal.checkActionPermission(status, 3, status, 1))
            {
                ctx.result(dal.loadStatusFromId(status));
            }
            else
            {
                ctx.result("Not allowed to view");
            }
            
        });

        // handler to update status
        app.post(form_name+"/"+SubmittedFormId+"/"+HandlerPaths.PATH_TO_UPDATE_FORM, (ctx) -> {
            //TODO: check if allowed
            int status = form.getStatusId();
            //TODO: add current user role_id
            // action_id for view is set to be 3
            if(dal.checkActionPermission(status, 3, status, 1))
            {
                ctx.result(dal.loadStatusFromId(status));
            }
            else
            {
                ctx.result("Not allowed to cg");
            }
            //TODO: do
        });
    }
}
