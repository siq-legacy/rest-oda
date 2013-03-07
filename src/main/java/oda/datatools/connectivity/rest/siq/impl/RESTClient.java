package oda.datatools.connectivity.rest.siq.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTClient implements RESTConstants {
    private int responseCode;
    private String message;
    private String response;
    private HttpResponse httpResponse;
    private HttpClient client;
    private HttpHost host;
    private HttpRoute route;
    private int hitcounts;
    private String url;
    private RESTHttpClientFactory clientfactory;
   
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }
    public RESTClient()
    {
    
    	try
    	{
    		clientfactory =new RESTHttpClientFactory();
  
	    } catch (KeyManagementException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnrecoverableKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	hitcounts=0;
    }
    public boolean  ExecuteHEAD(String url,String Username,String Password) throws Exception
    {
    	   HttpResponse response;
           this.setUrl(url);
         
        
           
           HttpHead request=new HttpHead(url);
           request.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
           request.setHeader("Content-Type", "application/json");
           
           request.setHeader("X-SPIRE-CREDENTIAL-TYPE", "password");
           request.setHeader("X-SPIRE-CREDENTIAL-USERNAME", Username);
           request.setHeader("X-SPIRE-CREDENTIAL-PASSWORD", Password);
           request.setHeader("X-Requested-With", "XMLHttpRequest");
           request.setHeader("X-SPIRE-CREDENTIAL-TENANT-ID", "bastion.security");
           
           host = new HttpHost(request.getURI().getPath());
           route = new HttpRoute(host); 
           
           response=executeRequest(request);
        
           if(response.getStatusLine().getStatusCode()==HTTP_OK)
           {
        	   request.abort();
        	   return true;
           }
		return false;
        
           
    }
    public String  ExecuteGet(ArrayList <NameValuePair> params,String url) throws Exception
    {
    		HttpResponse response;
            String combinedParams = "";
            if(params!=null &&!params.isEmpty()){
                combinedParams += "?";
                
                for(NameValuePair p : params)
                {
                    String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue());
                    if(combinedParams.length() > 1)
                    {
                    	combinedParams  +=  "&&" + paramString;
                    }
                    else
                    {
                    	combinedParams += paramString;
                    }
                }             
            }
  
           String s=combinedParams.toString();
           String thePath=url+s; 
           this.setUrl(thePath);
        
           HttpGet request = new HttpGet(thePath);    	
           host = new HttpHost(request.getURI().getPath());
           route = new HttpRoute(host); 
     
           response=executeRequest(request);
           while(response.getStatusLine().getStatusCode()==HTTP_PARTIAL)
           {
	        	this.hitcounts+=1;
	        	if(hitcounts==10)
	        	{
	        		 throw new OdaException("Error in Data  from Server");
	        	}
	           	Thread.sleep(5);
	           	try
	           	{
	           	 	response=executeRequest(request);
	           	}
	           	catch(HttpRetryException ex)
	           	{
	           		System.out.println("Sending the Request again");
	           	}
	          
           }
           this.hitcounts=0;
           request.abort();
           return this.getResponse();
    }
    public String  ExecutePost(List<JSONObject> jsonobjlist,String url) throws Exception
    {		
    		HttpResponse response;
    		HttpPost request = new HttpPost(url);
    	
    		if(jsonobjlist==null)
    		{
    			throw new Exception("Required JSON object for a POST is missing");
    		}
    		Iterator<JSONObject> itrjb=jsonobjlist.iterator(); 
    		String pass = "";
    		while(itrjb.hasNext())
    		{
    			JSONObject jb=itrjb.next();
    			pass=jb.toString()+pass;           			
    		}
    		request.addHeader("Content-type","application/json");
            StringEntity se = new StringEntity(pass);
            se.setContentType("application/json"); 
            se.setContentEncoding( new BasicHeader(HTTP.CONTENT_TYPE, "application/json")); 
            request.setEntity(se);
        	host = new HttpHost(request.getURI().getPath());
            route = new HttpRoute(host); 
            
            response=executeRequest(request);
            while(response.getStatusLine().getStatusCode()==HTTP_PARTIAL)
            {
            	this.hitcounts+=1;
	        	if(hitcounts==10)
	        	{
	        		 throw new OdaException("Error in Data  from Server");
	        	}
            	Thread.sleep(5);
            	try
            	{
            		response=executeRequest(request);
            	}
            	catch(HttpRetryException ex)
            	{
            		System.out.println("Sending the Request again");
            	}
            }
            this.hitcounts=0;
            request.abort();
            return this.getResponse();
    }
    public void clientconnectiontest(RESTConnection   connection) throws UnknownHostException, IOException
    {
    
    
   	
   		  	if(connection.getApi().equalsIgnoreCase(RESTConstants.APPSTACK))
   		  	{
	   		  	Socket sock=new Socket(connection.getIpAddress(),443);
	   		  	sock.close();
   		  	}
   		  	else if(connection.getApi().equalsIgnoreCase(RESTConstants.GATEWAY))
   		  	{
	   		  	Socket sock=new Socket(connection.getIpAddress(),8765);
	   		  	sock.close();
   		  	}
   		  	
 
   	 
    }
    private HttpResponse executeRequest(HttpUriRequest request) throws OdaException,HttpRetryException
    {
    	
	    	
			
			
        HttpClient client =clientfactory.getClient();	
     	ManagedClientConnection conn = null;
        httpResponse=null;
        try {
        	HttpContext context = new BasicHttpContext();
            httpResponse = client.execute(request,context);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            ClientConnectionManager manager = client.getConnectionManager(); 
            ClientConnectionRequest connRequest = manager.requestConnection(route, null);
            try {
				conn = connRequest.getConnection(0, null);
			} catch (InterruptedException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            HttpEntity entity = httpResponse.getEntity();
            
            System.out.println("the response"+responseCode);
            if (entity != null&&responseCode!=HTTP_PARTIAL) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();        
            }
            EntityUtils.consume(entity);
            conn.releaseConnection();
          
        } 
        catch(HttpRetryException ex)
        {
        	throw new HttpRetryException();
        }
        catch(ConnectionClosedException ex)
        {
        	throw new HttpRetryException();
        }
        catch (ClientProtocolException e) 
        {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
            throw new OdaException("Error in RESTClient  HttpResponse");
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
            throw new OdaException("Error in RESTClient I/O HttpResponse");
        }
		return httpResponse;
	
    }

    private static String convertStreamToString(InputStream is) throws OdaException,HttpRetryException {
    	String message=null;
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
			message= reader.readLine();
		} catch(ConnectionClosedException e)
        {
          	throw new HttpRetryException();
            
        }catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new OdaException("Error in Fetching the data");
		}
        
		return message;
      
    }
}