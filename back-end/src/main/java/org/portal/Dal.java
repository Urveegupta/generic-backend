package org.portal;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.portal.db.Connect;
import org.portal.db.entities.SubmittedForm;
import org.portal.db.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dal {
    private static Logger log = LoggerFactory.getLogger(Dal.class);
    private ObjectContext dbContext;
    public Dal(){
        this.dbContext = Connect.getConnection();
    }

    public boolean loginAuth(String loginId, String passwd){
        // get users from db
        log.info(">Authorizing login");
        List<User> list = ObjectSelect.query(User.class).select(dbContext);
        for(User user: list){
            if(Objects.equals(user.getEmail(), loginId)){
                if(Objects.equals(user.getPassword(), passwd)){
                    return true;
                }
            }
        }
        return false;
    }

    public List<JSONObject> loadForms() throws IOException, ParseException {
        JSONFileReader reader = new JSONFileReader();
        JSONObject jsonObject = reader.parseFile(Const.FORM_PATH);
        return (List<JSONObject>) jsonObject.get("FORMS");
    }

    public List<JSONObject> loadStatuses() throws IOException, ParseException {
        JSONFileReader reader = new JSONFileReader();
        JSONObject jsonObject = reader.parseFile(Const.STATUS_PATH);
        return (List<JSONObject>) jsonObject.get("STATUS");
    }

    public List<SubmittedForm> loadSubmittedForms(){
        return ObjectSelect.query(SubmittedForm.class).select(dbContext);
    }

    public User getUser(String email){
        List<User> list = ObjectSelect.query(User.class).select(dbContext);
        for(User user: list){
            if(Objects.equals(user.getEmail(), email)){
                return user;
            }
        }
        return null;
    }

    public String getSubmittedFormId(SubmittedForm form){
        return (String) form.getObjectId().getIdSnapshot().get("submitted_form_id");
    }
    public String getFormName(SubmittedForm form) throws IOException, ParseException {
        int formId = form.getFormId();
        List<JSONObject> allForms = loadForms();
        for(JSONObject formData : allForms){
            if(Objects.equals(formId, formData.get(Const.KEY_FORM_ID))){
                return (String) formData.get(Const.KEY_FORM_NAME);
            }
        }
        return "Couldn't find form";
    }

    public String loadStatusFromId(int id) throws IOException, ParseException {
        for(JSONObject status: loadStatuses()){
            if(Objects.equals(id, status.get(Const.KEY_STATUS_ID))){
                return (String) status.get(Const.KEY_STATUS_NAME);
            }
        }
        return "Couldn't find status";
    }

    public void addUser(String username, String email, String password){
        User newUser = dbContext.newObject(User.class);
        newUser.setUserName(username);
        newUser.setEmail(email);
        newUser.setPassword(password);
        dbContext.commitChanges();
    }
}
