package com.basejava.webapp.exceptions;

public class ExistPeriodException extends RuntimeException{
    public ExistPeriodException() {
        super("The new period cannot contain the same 'start date' or the same 'end data' as " +
                "the previous period. Enter the correct date for the period.");
    }
}