package oda.datatools.connectivity.rest.siq.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTResponseThread  implements Runnable,RESTConstants{

	private HttpClient httpclient;
	private RESTClient rest_client;
	private HttpContext context ;
	private HttpResponse httpresponse;
	private HttpUriRequest httprequest;
    private HttpEntity entity;
    private ManagedClientConnection conn;
    private ClientConnectionRequest connRequest;
    private ClientConnectionManager manager;
    private HttpHost host;
    private HttpRoute route;
    private String datasettype;
    private boolean datagot;
    
    
	public boolean isDatagot() {
		return datagot;
	}

	public void setDatagot(boolean datagot) {
		this.datagot = datagot;
	}

	public RESTResponseThread(HttpClient client,RESTClient rest_client,HttpUriRequest httprequest,String datasettype)
	{
		this.httpclient=client;
		this.rest_client=rest_client;
		this.httprequest=httprequest;
		this.datasettype=datasettype;
	}
	
	@Override
	public void run() 
	{
     	conn = null;
     	long contentlength;
    	host = new HttpHost(httprequest.getURI().getPath());
        route = new HttpRoute(host);
        int hitcounts=0;
        try {
        	
	    	  while(true)
	          {
	    		  
	    		  
	    		  
	    		  
		        	context = new BasicHttpContext();
		        	httpresponse=httpclient.execute(httprequest,context);
		        	System.out.println("the response"+httpresponse.getStatusLine().getStatusCode());
		        	rest_client.setResponseCode(httpresponse.getStatusLine().getStatusCode());
		        	rest_client.setMessage(httpresponse.getStatusLine().getReasonPhrase());
		            
		            manager = httpclient.getConnectionManager(); 
		            connRequest = manager.requestConnection(route, null);
		            try {
						conn = connRequest.getConnection(0, null);
					} catch (InterruptedException e) {
						
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            entity = httpresponse.getEntity();
		            hitcounts+=1;
	 	        	if(hitcounts==10)
	 	        	{
	 	        		 throw new OdaException("Error in Data  from Server");
	 	        	}
	 	        	if(httpresponse.getStatusLine().getStatusCode()==HTTP_PARTIAL)
	 	        	{
	 	        		Thread.sleep(5);
	 	        		continue;
	 	        	}
		            if (entity != null&&httpresponse.getStatusLine().getStatusCode()!=HTTP_PARTIAL)
		            {
		                InputStream instream = entity.getContent();
		                contentlength=entity.getContentLength();
		                System.out.println("the content length is "+datasettype);
		                
		                
		             
		                		byte data[]=new byte[1024];
				    			long count=0,total=0;
				    			StringBuilder response_builder = new StringBuilder("");
				    			if(datasettype!=null&&datasettype.equals(RESTConstants.ODA_DATA_SET_UI_ID))
	    					 	{
					    			try {
					    				
							    				while(true)
							    				{		
					    					 		count=instream.read(data);
						    					    if(count == -1 || count == '|') 
						    					    {
						    					    	break;
						    					    }
							    					String s=new String(data,0,(int) count);
							    					response_builder.append(s);
							    					total+=count;
							    					int total_per=(int)((total*100)/contentlength);
							    					rest_client.setTotal_percent(total_per);
							    					System.out.println("the restclient"+total_per);
							    				}
							    				rest_client.setResponse(response_builder.toString());
							    				instream.close();
								    			EntityUtils.consume(entity);
								    			conn.releaseConnection();
							    		}
							        	 catch(ConnectionClosedException e)
							             {
							        		 e.printStackTrace();
							        		 rest_client.setErrorstatus(true);
							             }
							        	catch (IOException e) {
							    			e.printStackTrace();
							    			rest_client.setErrorstatus(true);
							    		} 
					    				catch (Exception e)
					    				{
							    			e.printStackTrace();
							    			rest_client.setErrorstatus(true);
							    		}
	    					 	}
				    			else if(datasettype!=null &&datasettype.equals(RESTConstants.ODA_DATA_SET_ID))
	    					 	{

					    					try {
					    								String s;
						    					 		while(true)
						    					 		{
						    					 			count=instream.read(data);
						    					 			s=new String(data,0,(int) count);
						    					 			if(s.contains("["))
						    					 				break;
						    					 			
						    					 		}
						    					 		response_builder = new StringBuilder("");
						    					 		response_builder.append(s.substring(s.indexOf('[')+1,s.length()));
						    					 		while(true)
						    					 		{
						    					 			count=instream.read(data);
						    					 			
								    					    if(count == -1 || count == '|'||count==']') 
								    					        break;
								    					    
									    					String s=new String(data,0,(int) count);
									    					response_builder.append(s);
									    					total+=count;
						    					 		}
						    					 		instream.close();
										    			EntityUtils.consume(entity);
										    			conn.releaseConnection();
					    						}
									        	 catch(ConnectionClosedException e)
									             {
									        		 e.printStackTrace();
									        		 rest_client.setErrorstatus(true);
									             }
									        	catch (IOException e) {
									    			e.printStackTrace();
									    			rest_client.setErrorstatus(true);
									    		} 
							    				catch (Exception e)
							    				{
									    			e.printStackTrace();
									    			rest_client.setErrorstatus(true);
									    		}
		    						
	    					 	}
	    					 	
		                
		              
		            }
		            break;
	            
	          
	          
	        }
        }
        catch (IOException | OdaException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			rest_client.setErrorstatus(true);
		}
        finally
        {
        	httpclient.getConnectionManager().shutdown();
        }
       
			
		
	}

}
