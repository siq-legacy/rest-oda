package oda.datatools.connectivity.rest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.eclipse.datatools.connectivity.oda.OdaException;
public class RESTClient {

   

    private ArrayList <NameValuePair> params;
    private ArrayList <NameValuePair> headers;
    private String url;
    private List<JSONObject> jsonobjlist;
    private int responseCode;
    private String message;
    private String response;
    private HttpResponse httpResponse;

    public String getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return message;
    }

    public int getResponseCode() {
        return responseCode;
    }
    public void clearParameters()
    {
    	params=null;
    	headers=null;
    	jsonobjlist=null;
    	url=null;
    }
    public void setURL(String url)
    {
    	this.url = url;
        params = new ArrayList<NameValuePair>();
        headers = new ArrayList<NameValuePair>();
    }
    public RESTClient()
    {
        
    }

    public void AddParam(String name, String value)
    {
        params.add(new NameValuePair(name, value));
    }

    public void AddHeader(String name, String value)
    {
        headers.add(new NameValuePair(name, value));
    }
    public void AddJsonPost(List<JSONObject> jo)
    {
    	jsonobjlist = jo;
    }
    public void httpGet(String urlStr) throws IOException {
		  URL url = new URL(urlStr);
		  System.out.println(url.toString());
		  HttpURLConnection conn =
		      (HttpURLConnection) url.openConnection();
		  responseCode=conn.getResponseCode();
		  if (conn.getResponseCode() != 200) {
		    throw new IOException(conn.getResponseMessage());
		  }
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
		}
    public void  Execute(RequestMethod method) throws Exception
    {
    	 switch(method) 
        {
           case GET:
            {
                String combinedParams = "";
                if(!params.isEmpty()){
                    combinedParams += "?";
                    
                    for(NameValuePair p : params)
                    {
                        String paramString = p.getName() + "=" + p.getValue();
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
      	
      		   System.out.println(url+s);
               httpGet(url+s);
               break;
            }
            case POST:
            {
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
                    httpResponse=executeRequest(request);
                    while(httpResponse!=null &&httpResponse.getStatusLine().getStatusCode()==206)
                    {
                    	Thread.sleep(5);
                    	httpResponse=executeRequest(request);
                    }
                break;
            }
        }
       
       
    }
    
    private HttpResponse executeRequest(HttpUriRequest request) throws OdaException
    {
     	 HttpClient client = new DefaultHttpClient();
         
        httpResponse=null;
        try {
        	
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            System.out.println("the Reason code is"+responseCode);
            HttpEntity entity = httpResponse.getEntity();
            
            
            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                instream.close();
                
            }
          
        } catch (ClientProtocolException e)  {
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