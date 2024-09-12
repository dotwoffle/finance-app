package com.dotwoffle.api.exceptions;

/**This exception is thrown when the server determines that a query string in a GET request to the API endpoint for a
 * list of records is malformed.*/
public class MalformedQueryException extends Exception {

    /**Creates a new MalformedQueryException.
     * @param parameter The name of the parameter from the query string that caused the exception.
     * @param message The error message to be included with the exception.*/
    public MalformedQueryException(String parameter, String message) {
        super(message);
        this.PARAMETER = parameter;
    }

    /**The parameter that caused the query string to be malformed.*/
    public final String PARAMETER;

}
