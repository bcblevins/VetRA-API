package com.bcb.vetra.exception;

/**
 * General purpose exception class for DAO operations.
 */
public class DaoException extends RuntimeException{

    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
