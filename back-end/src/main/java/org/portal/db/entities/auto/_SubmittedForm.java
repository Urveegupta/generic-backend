package org.portal.db.entities.auto;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.Property;
import org.portal.db.entities.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class _SubmittedForm was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _SubmittedForm extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final String SUBMITTED_FORM_ID_PK_COLUMN = "submitted_form_id";

    public static final Property<Integer> FORM_ID = Property.create("formId", Integer.class);
    public static final Property<Integer> STATUS_ID = Property.create("statusId", Integer.class);
    public static final Property<User> USER = Property.create("user", User.class);

    protected Integer formId;
    protected Integer statusId;

    protected Object user;

    public void setFormId(int formId) {
        beforePropertyWrite("formId", this.formId, formId);
        this.formId = formId;
    }

    public int getFormId() {
        beforePropertyRead("formId");
        if(this.formId == null) {
            return 0;
        }
        return this.formId;
    }

    public void setStatusId(int statusId) {
        beforePropertyWrite("statusId", this.statusId, statusId);
        this.statusId = statusId;
    }

    public int getStatusId() {
        beforePropertyRead("statusId");
        if(this.statusId == null) {
            return 0;
        }
        return this.statusId;
    }

    public void setUser(User user) {
        setToOneTarget("user", user, true);
    }

    public User getUser() {
        return (User)readProperty("user");
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "formId":
                return this.formId;
            case "statusId":
                return this.statusId;
            case "user":
                return this.user;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "formId":
                this.formId = (Integer)val;
                break;
            case "statusId":
                this.statusId = (Integer)val;
                break;
            case "user":
                this.user = val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.formId);
        out.writeObject(this.statusId);
        out.writeObject(this.user);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.formId = (Integer)in.readObject();
        this.statusId = (Integer)in.readObject();
        this.user = in.readObject();
    }

}
