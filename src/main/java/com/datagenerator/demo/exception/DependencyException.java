package com.datagenerator.demo.exception;

public class DependencyException extends RuntimeException {
    
    private static final long serialVersionUID = -6443053415907402665L;
    private String errorMessage;
    private int statusCode;

    public DependencyException(String message) {
        super(message);
    }

    public DependencyException(String message, Exception e) {
        super(message, e);
    }

    public DependencyException(String errorMessage, int statusCode) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

}
