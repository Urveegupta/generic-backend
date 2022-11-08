package org.portal;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class JSONFileReader {
    private static Logger log = LoggerFactory.getLogger(JSONFileReader.class);

    public JSONObject parseFile(String filepath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filepath));
        JSONObject jsonObject = (JSONObject) obj;

        return jsonObject;
    }
//    String name = (String) jsonObject.get("Name");
//    String course = (String) jsonObject.get("Course");
//    JSONArray subjects = (JSONArray) jsonObject.get("Subjects");
//    Iterator iterator = subjects.iterator();
//    while(iterator.hasNext())
//    {
//        System.out.println(iterator.next());
//    }

}
