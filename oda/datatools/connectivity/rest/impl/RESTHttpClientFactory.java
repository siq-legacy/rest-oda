package oda.datatools.connectivity.rest.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

public class RESTHttpClientFactory {

    private static HttpClient client;

    public synchronized static HttpClient getThreadSafeClient() {
        if (client != null)
            return client;
        client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
                mgr.getSchemeRegistry()), params);
        return client;

    }
}