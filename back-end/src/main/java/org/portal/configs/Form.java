package org.portal.configs;

import java.util.HashMap;

public class Form {

    private int form_id;
    private String form_name;

    private HashMap<String, String> fields;
    public Form(String form_name){

    }

    public String getFormName(){
        return this.form_name;
    }
    public Integer getFormId(){ return this.form_id; }
}
