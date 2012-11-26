package oda.datatools.connectivity.rest.impl;

public class HttpRetryException extends Exception {
	public HttpRetryException(String message) {
        super(message);
    }
	public HttpRetryException() {
       
    }
}
