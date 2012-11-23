package oda.datatools.connectivity.rest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.datatools.connectivity.oda.OdaException;
public class RESTClient {
    private int responseCode;
    private String message;
    private String response;
    private HttpResponse httpResponse;
    private HttpClient client;
    private final int HTTP_PARTIAL=206;
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
    	 
    }

    /*public int  httpGet(String urlStr) throws IOException {
		  URL url = new URL(urlStr);
		  HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
		  responseCode=conn.getResponseCode();
		  BufferedReader rd = new BufferedReader(
		      new InputStreamReader(conn.getInputStream()));
		  StringBuilder sb = new StringBuilder();
		  String line;
		  while ((line = rd.readLine()) != null) {
		    sb.append(line);
		  }
		  response=sb.toString();
		  
		  rd.close();
		  conn.disconnect();
		  return responseCode;
		}*/
    public String  ExecuteGet(ArrayList <NameValuePair> params,String url) throws Exception
    {
    		int responsecode;
            String combinedParams = "";
            if(!params.isEmpty()){
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
           //int responsecode= httpGet(url+s);
           String thePath=url+s; 
           HttpGet request = new HttpGet(thePath);    	
          
           /*System.out.println(url+s);
           while(responsecode==206)
           {
           	Thread.sleep(5);
           	responsecode= httpGet(url+s);
           }
           return this.getResponse();*/
           
           responsecode=executeRequest(request);
           while(responsecode==this.HTTP_PARTIAL)
           {
           	Thread.sleep(5);
           	responsecode=executeRequest(request);
           }
           request.abort();
           return this.getResponse();
    }
    public String  ExecutePost(List<JSONObject> jsonobjlist,String url) throws Exception
    {		
    		int responsecode;
    		HttpPost request = new HttpPost(url);    		
    		if(jsonobjlist==null)
    		{
    			throw new Exception("Jason object is missing");
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
            responsecode=executeRequest(request);
            while(responsecode==this.HTTP_PARTIAL)
            {
            	Thread.sleep(5);
            	responsecode=executeRequest(request);
            }
            request.abort();
            return this.getResponse();
    }
    
    private int executeRequest(HttpUriRequest request) throws OdaException
    {
     	 client =RESTHttpClientFactory.getThreadSafeClient();
     	
        httpResponse=null;
        try {
        	HttpContext context = new BasicHttpContext();
            httpResponse = client.execute(request,context);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
          
            HttpEntity entity = httpResponse.getEntity();
            
            
            if (entity != null&&responseCode!=206) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
                System.out.println("the Response length is"+response.length());
              
            }
            EntityUtils.consume(entity);
          
        } 
        catch(ConnectionClosedException ex)
        {
        	return HTTP_PARTIAL;
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
		return responseCode;
	
    }

    private static String convertStreamToString(InputStream is) throws OdaException {
    	String message=null;
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
			message= reader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw new OdaException("Error in Fetching the data");
		}
		return message;
      
    }
}