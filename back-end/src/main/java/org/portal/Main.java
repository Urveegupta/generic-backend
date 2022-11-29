package org.portal;

import io.javalin.Javalin;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        Javalin app = Javalin.create().start(8080);
        FlowEngine fe = new FlowEngine(app);
        fe.start();
    }
}