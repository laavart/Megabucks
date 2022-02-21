package com.psloba.citra.exception;

public class DBInvalidException extends Exception{
    public DBInvalidException() {
        super("citra.Database and Arguments not match !");
    }
}