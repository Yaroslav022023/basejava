package com.basejava.webapp.exceptions;

public class NotExistCompanyException extends RuntimeException{
    public NotExistCompanyException() {
        super("Not exist Company");
    }
}