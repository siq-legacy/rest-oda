package oda.datatools.connectivity.rest.siq.impl;

import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTClient implements RESTConstants {
	
	
    private int responseCode;
    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpClient getClient() {
		return client;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public void setTotal_percent(Integer total_percent) {
		this.total_percent = total_percent;
	}
	private String message;
	private HttpClient client;
    public void setClient(HttpClient client) {
		this.client = client;
	}
	private String url;
    private Integer total_percent;
    
	private Thread Running_thread;
    private Boolean errorstatus;

    public Boolean getErrorstatus() {
		return errorstatus;
	}

	public void setErrorstatus(Boolean errorstatus) {
		this.errorstatus = errorstatus;
	}
	private RESTHttpClientFactory clientfactory;
    private String Response;
    public void setResponse(String response) {
		Response = response;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResponse() {
        return Response;
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
           
          
           response=executeRequest(request);
        
           if(response.getStatusLine().getStatusCode()==HTTP_OK)
           {
        	   request.abort();
        	   return true;
           }
		return false;
        
           
    }
    public void  ExecuteGet(ArrayList <NameValuePair> params,String url,String datasettype) throws Exception
    {
    		
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
           executeRequest(request,datasettype);
    }
    public void  ExecutePost(List<JSONObject> jsonobjlist,String url,String datasettype) throws Exception
    {		
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
            
            executeRequest(request,datasettype);
          
     
    }
    
    private  void executeRequest(HttpUriRequest request,String datasettype) throws OdaException,HttpRetryException
    {
       this.client=clientfactory.getClient();
	   Thread t = new Thread(new RESTResponseThread(this.client,this,request,datasettype));
       Running_thread=t;
       t.start();
      
    }
    public Thread getRunning_thread() {
		return Running_thread;
	}

	public Integer getTotal_percent() {
  		return total_percent;
  	} 
   
}