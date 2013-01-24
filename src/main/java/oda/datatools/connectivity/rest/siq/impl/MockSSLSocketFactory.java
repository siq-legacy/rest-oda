package oda.datatools.connectivity.rest.siq.impl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;


import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

public class MockSSLSocketFactory extends SSLSocketFactory {

public MockSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
    super(trustStrategy, hostNameVerify);
}

private static final X509HostnameVerifier hostNameVerify = new X509HostnameVerifier() {

	@Override
	public boolean verify(String arg0, SSLSession arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void verify(String arg0, SSLSocket arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verify(String arg0, X509Certificate arg1) throws SSLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verify(String arg0, String[] arg1, String[] arg2)
			throws SSLException {
		// TODO Auto-generated method stub
		
	}
   
};

private static final TrustStrategy trustStrategy = new TrustStrategy() {
    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
    }
};
}