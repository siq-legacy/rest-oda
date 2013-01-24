package oda.datatools.connectivity.rest.siq.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class RESTHttpClientFactory
{
  private static HttpClient client;

  public static synchronized HttpClient getThreadSafeClient() throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException
  {
	  SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
      schemeRegistry.register(new Scheme("https", 443, new MockSSLSocketFactory()));

    if (client != null)
    {
    	return client;
    }
    else
    {
    	  PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);   	  
          cm.setMaxTotal(100);
          cm.setDefaultMaxPerRoute(10);
          client = new DefaultHttpClient(cm);
    	  return client;
    }
    
 
  }
 
}