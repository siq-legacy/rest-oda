package oda.datatools.connectivity.rest.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
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
                System.out.println("the url   "+url + combinedParams);
                HttpGet request = new HttpGet(url + combinedParams);
                for(NameValuePair h : headers)
                {
                    request.addHeader(h.getName(), h.getValue());
                }
                httpResponse=executeRequest(request);
                while(httpResponse!=null &&httpResponse.getStatusLine().getStatusCode()==206)
                {
                	Thread.sleep(5);
                	httpResponse=executeRequest(request);
                }
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

    private HttpResponse executeRequest(HttpUriRequest request)
    {
        org.apache.http.client.HttpClient client = RESTHttpClientFactory.getThreadSafeClient();
       
        httpResponse=null;
        try {
        	System.out.println("the URL"+request.getURI());
        	System.out.println("Response Time");
            long initial=System.currentTimeMillis();
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            System.out.println("the response"+responseCode);
            System.out.println("the message"+message);
            long ini=System.currentTimeMillis()-initial;
         
        	System.out.println("Taken "+ini);
            HttpEntity entity = httpResponse.getEntity();
            
            
            if (entity != null) {
            	System.out.println("entity.getContent() Time ");
            	initial=System.currentTimeMillis();
                InputStream instream = entity.getContent();
                ini=System.currentTimeMillis()-initial;
                System.out.println("the response mssage"+response);
                System.out.println("Taken "+ini);
              	System.out.println("Packing Time ");
            	initial=System.currentTimeMillis();
            	
                response = convertStreamToString(instream);
                ini=System.currentTimeMillis()-initial;
                System.out.println("Taken "+ini);
            	
                instream.close();
                
            }
          
        } catch (ClientProtocolException e)  {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        } catch (IOException e) {
            client.getConnectionManager().shutdown();
            e.printStackTrace();
        }
		return httpResponse;
	
    }

    private static String convertStreamToString(InputStream is) {
    	String message=null;
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	System.out.println("Packing Time Original");
    	long initial=System.currentTimeMillis();
        try {
			message= reader.readLine();
			long ini=System.currentTimeMillis()-initial;
			System.out.println("Taken"+ini);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return message;
      
    }
}