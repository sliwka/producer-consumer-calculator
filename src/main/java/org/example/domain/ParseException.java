package org.example.domain;

public class ParseException extends Exception {
    public ParseException(Exception innerException) {
        super(innerException);
    }

    public ParseException(String message) {
        super(message);
    }
}
