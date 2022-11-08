package org.portal;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        var app = Javalin.create().start(8080);
        FlowEngine fe = new FlowEngine(app);
        fe.start();
    }
}