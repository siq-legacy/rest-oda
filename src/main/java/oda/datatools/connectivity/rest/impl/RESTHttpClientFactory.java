package oda.datatools.connectivity.rest.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

public class RESTHttpClientFactory
{
  private static HttpClient client;

  public static synchronized HttpClient getThreadSafeClient()
  {
    if (client != null)
    {
    	return client;
    }
    else
    {
    	  PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
          cm.setMaxTotal(100);
          cm.setDefaultMaxPerRoute(10);
          client = new DefaultHttpClient(cm);
    	  return client;
    }
    
 
  }
}