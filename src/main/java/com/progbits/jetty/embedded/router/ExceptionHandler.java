package com.progbits.jetty.embedded.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author scarr
 */
public interface ExceptionHandler<T> {

    /**
     * This should check the Throwable and see if it can be handled.
     *
     * If it is handled, return true, if not return false
     *
     * @param resp Handle the Exception
     * @param ex The throwable to test
     * @return true/false did this Handler handle the exception
     */
    abstract boolean handle(Throwable ex, HttpServletRequest req, HttpServletResponse resp);
}
