package com.progbits.jetty.embedded.execeptions;

/**
 *
 * @author scarr_jp
 */
public class AuthenticationException extends Exception {

    private int _status = 403;

    public AuthenticationException() {
        super("Forbidden");
    }

    public AuthenticationException(int status, String message) {
        super(message);

        _status = status;
    }

    public AuthenticationException(int status, String message, Throwable t) {
        super(message, t);

        _status = status;
    }

    public Integer getStatus() {
        return _status;
    }
}
