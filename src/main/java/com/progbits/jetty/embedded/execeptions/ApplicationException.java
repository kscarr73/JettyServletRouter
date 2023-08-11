package com.progbits.jetty.embedded.execeptions;

public class ApplicationException extends Exception {

    private int _status = 200;

    public ApplicationException(int status, String message) {
        super(message);

        _status = status;
    }

    public ApplicationException(int status, String message, Throwable t) {
        super(message, t);

        _status = status;
    }

    public Integer getStatus() {
        return _status;
    }
}