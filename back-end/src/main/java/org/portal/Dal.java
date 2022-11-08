package org.portal;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.ObjectSelect;
import org.portal.db.Connect;
import org.portal.db.entities.Form;
import org.portal.db.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public List<Form> loadForms(){
        return ObjectSelect.query(Form.class).select(dbContext);
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
}
