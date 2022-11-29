package org.portal;

import io.javalin.http.Context;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.exp.Property;
import org.apache.cayenne.query.ObjectSelect;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.portal.db.Connect;
import org.portal.db.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Dal {
    private static Logger log = LoggerFactory.getLogger(Dal.class);
    private ObjectContext dbContext;
    public Dal(){
        Connect conn = new Connect();
        this.dbContext = conn.getConnection();
    }

    public boolean loginAuth(String loginId, String passwd){
        // get users from db
        log.info(">Authorizing login");
        List<User> list = ObjectSelect.query(User.class).select(dbContext);
        for(User user: list){
            if(Objects.equals(user.getEmail().toLowerCase(), loginId)){
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

    public List<JSONObject> loadActionPermissions() throws IOException, ParseException{
        JSONFileReader reader = new JSONFileReader();
        JSONObject jsonObject = reader.parseFile(Const.PERMISSIONS_PATH);
        return (List<JSONObject>) jsonObject.get("PERMISSIONS");
    }

//    public List<SubmittedForm> loadSubmittedForms(){
//        return ObjectSelect.query(SubmittedForm.class).select(dbContext);
//    }

    public User getUser(String email){
        List<User> list = ObjectSelect.query(User.class).where(User.EMAIL.eq(email)).select(dbContext);
        for(User user: list){
            if(Objects.equals(user.getEmail().toLowerCase(), email.toLowerCase())){
                return user;
            }
        }
        return null;
    }

//    public String getSubmittedFormId(SubmittedForm form){
//        return (String) form.getObjectId().getIdSnapshot().get("submitted_form_id");
//    }
//    public String getFormName(SubmittedForm form) throws IOException, ParseException {
//        int formId = form.getFormId();
//        List<JSONObject> allForms = loadForms();
//        for(JSONObject formData : allForms){
//            if(Objects.equals(formId, formData.get(Const.KEY_FORM_ID))){
//                return (String) formData.get(Const.KEY_FORM_NAME);
//            }
//        }
//        return "Couldn't find form";
//    }

    public String loadStatusFromId(int id) throws IOException, ParseException {
        for(JSONObject status: loadStatuses()){
            if(Objects.equals(id, status.get(Const.KEY_STATUS_ID))){
                return (String) status.get(Const.KEY_STATUS_NAME);
            }
        }
        return "Couldn't find status";
    }

    public boolean addUser(String username, String email, String password, int roleid){
        //check if already exists
        List<User> list = ObjectSelect.query(User.class).where(User.EMAIL.eq(email.toLowerCase())).select(dbContext);
        if(list.isEmpty()) {
            User newUser = dbContext.newObject(User.class);
            newUser.setUserName(username);
            newUser.setEmail(email.toLowerCase());
            newUser.setPassword(password);
            newUser.setRoleId(roleid);
            dbContext.commitChanges();
            return true;
        }
        else{
            return false;
        }
    }
    public String getStatusfromObject(String form_name, Object formObj) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String methodName = "getStatusId";
        String prefix = "org.portal.db.entities.";
        String className = prefix + form_name.substring(0, 1).toUpperCase() + form_name.substring(1);
        Class formClass = Class.forName(className);
        Method method = formClass.getMethod(methodName);
        method.setAccessible(true);
        log.info(method.getReturnType().toString());
        Object result = method.invoke(formObj);
        log.info("result -> "+ result.toString());
        return (String) result;
    }
    public void addSubmission(JSONObject form, Context ctx) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // create new form object using form name
        String form_name = (String) form.get("form_name");
        String prefix = "org.portal.db.entities.";
        String className = prefix + form_name.substring(0, 1).toUpperCase() + form_name.substring(1);
        Class formClass = Class.forName(className);
        Object formObj = dbContext.newObject(formClass);
        log.info(formObj.getClass().toString());

        //retrieve fields required by the form
        Field[] fields = formClass.getFields();

        //populate fields
        for(Field field: fields){
            if(Objects.equals(field.getType(), Property.class)){
                //get method to populate field
                String field_name = field.getName();
                String[] parts = field_name.split("_");
                String methodName = "set";
                for(String part: parts){
                    String newPart = part.charAt(0) + part.substring(1).toLowerCase();
                    methodName = methodName + newPart;
                }
                log.info(methodName);

                //create method instance
                Object propValue = field.get(formObj);
                log.info("prop value: "+ Arrays.toString(propValue.getClass().getFields()));
                Method propMethod = Property.class.getMethod("getType");
                Object type = propMethod.invoke(propValue);
                Class typeClass = (Class) type;
                log.info(type.toString());
                //declare method parameter class types
                Method method = formClass.getMethod(methodName, typeClass);
                String arg = ctx.req.getParameter(field_name.toLowerCase());
                log.info(arg);
                //invoke method
                method.invoke(formObj,arg);
            }
        }
        //commit changes to db
        dbContext.commitChanges();
    }

    /* 
        returns True if a role is allowed to do a particular action
    */ 
    public boolean checkActionPermission(Integer from_status_id, Integer action_id, Integer to_status_id, Integer role_id) throws Exception {

        List<JSONObject> permissions = loadActionPermissions();
        for(JSONObject permission : permissions)
        {
            if(permission.get("actionId") == action_id &&
                permission.get("statusId") == from_status_id &&
                permission.get("newStatusId" ) == to_status_id){
                   
                JSONArray role_ids = (JSONArray)permission.get("roleroleId");
                for(int i = 0; i < role_ids.size(); i++){
                    if(role_ids.get(i) == role_id)
                    {
                        return true;
                    }
                }  

            }
        }

        return false;
    }

    public List getAllSubmittedForms(String form_name) throws Exception{
        String prefix = "org.portal.db.entities.";
        String className = prefix + form_name.substring(0, 1).toUpperCase() + form_name.substring(1);
        Class<?> formClass = Class.forName(className);
        return ObjectSelect.query(formClass).select(dbContext);
    }
}
