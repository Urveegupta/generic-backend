package org.portal.handlers;

import org.portal.Dal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.http.Context;
import io.javalin.http.Handler;
/**
 * @author Balwinder Sodhi
 */
public abstract class BaseHandler implements Handler{
    protected Logger log = LoggerFactory.getLogger(getClass());
    protected Context ctx;
    protected Dal dal;
}
