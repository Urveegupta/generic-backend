package org.portal.db;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.datasource.DataSourceBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;


/**
 * Get a connection to database
 * @return Connection object
 */
public class Connect {

    private static Logger log = LoggerFactory.getLogger(Connect.class);
    public static String URL;
    public static String USER;
    public static String PASS;

    public Connect(){
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config.json"));
            System.out.println("file read");
            JSONObject jsonObject = (JSONObject)obj;
            URL = jsonObject.get("DB_URL").toString();
            USER = jsonObject.get("DB_USERNAME").toString();
            PASS = jsonObject.get("DB_PASSWORD").toString();
            log.info("Received DB credentials");
        } catch(Exception e) {
            System.out.println(System.getProperty("user.dir"));
            log.info("Exception occured while reading db credentials");
            log.info(e.toString());
        }
    }

    public static ObjectContext getConnection(){

        ServerRuntime cayenneRuntime = ServerRuntime.builder()
                .dataSource(DataSourceBuilder
                        .url(URL)
                        .driver("com.mysql.cj.jdbc.Driver")
                        .userName(USER) // TODO: change to your actual username and password
                        .password(PASS).build())
                .addConfig("cayenne-project.xml")
                .build();
        ObjectContext context = cayenneRuntime.newContext();
        log.info("Connection established!");
        return context;
    }

}