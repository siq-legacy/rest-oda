package oda.datatools.connectivity.rest.siq.impl;

public class HttpRetryException extends Exception {
	public HttpRetryException(String message) {
        super(message);
    }
	public HttpRetryException() {
       
    }
}
