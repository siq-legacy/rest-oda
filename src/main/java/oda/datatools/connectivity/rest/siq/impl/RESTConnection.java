package oda.datatools.connectivity.rest.siq.impl;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

import com.ibm.icu.util.ULocale;
public class RESTConnection
  implements IConnection,RESTConstants
{
 
  private boolean isOpen = false;
  private  String IpAddress=null;
  private  String Port=null;
  private  String Username=null;
  private  String Password=null;
  private  String Url=null;
  private  String api=null;
  
  public  String getApi() {
	return api;
}

public  void setApi(String api) {
	this.api = api;
}
private Map<String, IQuery> queryMap = new HashMap<String, IQuery>();
  private Map<String, RESTQuery> restqueryMap = new HashMap<String, RESTQuery>();
  public  String getUrl() {
	return Url;
}

public  String getIpAddress() {
	return this.IpAddress;
}

private void setIpAddress(String ipAddress) {
	IpAddress = ipAddress;
}

public  String getPort() {
	return Port;
}

private void setPort(String port) {
	Port = port;
}

public  String getUsername() {
	return Username;
}

private void setUsername(String username) {
	Username = username;
}

public  String getPassword() {
	return Password;
}

private void setPassword(String password) {
	Password = password;
}
  public RESTConnection() {

    System.out.println("i got the connection");
  }

  public void close() throws OdaException {
 
	System.out.println("the connection is closed");
    this.isOpen = false;
  }

  public void commit()
    throws OdaException
  {
	  
  }

  public int getMaxQueries()
    throws OdaException
  {
    return 0;
  }

  public IDataSetMetaData getMetaData(String datasettype)
    throws OdaException
  {
    return new RESTDataSetMetaData(this,datasettype);
  }

  public boolean isOpen()
    throws OdaException
  {
    return this.isOpen;
  }


  public void open(Properties parameters)
    throws OdaException
  {
	
	  Set<Entry<Object,Object>> param=parameters.entrySet();
	  Iterator<Entry<Object, Object>> it = param.iterator();
	  while(it.hasNext())
	  {
		  Entry<Object, Object> paramobject=it.next();
		  switch(paramobject.getKey().toString())
		  {
		  	case PROP_USERNAME:
		  		setUsername(paramobject.getValue().toString());
		  		break;
		  	case PROP_PASSWORD:
		  		this.setPassword(paramobject.getValue().toString());
		  		break;
		  	case PROP_IPADDRESS:
		  		this.setIpAddress(paramobject.getValue().toString());
		  		break;
		  	case PROP_PORT:
		  		if(paramobject.getValue()!=null)
		  		{
		  			this.setPort(paramobject.getValue().toString());
		  		}
		  		else
		  		{
		  			this.setPort("");
		  		}
		  	case PROP_APP:
		  		this.setApi(paramobject.getValue().toString());
		  		break;
		  	
		  }
		  Url="http://"+getIpAddress()+":"+getPort()+"/";
		  
	  }
	  this.isOpen = true;
  }
  public String checkconnection(String gatewayport,String appstackport) throws Exception
  {
	 
	  setPort("8080");
	  try 
	  {
		  
		  	Socket sock=new Socket(getIpAddress(),Integer.parseInt(getPort()));
		  	sock.close();
	  }
	  catch(Exception ex)
	  {
		  if (ex.getMessage().contains("refused"))
		  {
			  System.out.println("connection refusal");		  
		  }
		  else if(ex.getMessage().contains("timed"))
		  {
			  System.out.println("connection timedout");		
			  return null;
		  }
	  }
	  String prepPort="8765";
	  try
	  {
		  
		  	Socket sock=new Socket(getIpAddress(),Integer.parseInt(prepPort));
		  	sock.close();
		  	
		  	
	  }
	  catch(Exception ex)
	  {
		  System.out.println(ex.getLocalizedMessage());
		  if (ex.getMessage().contains("refused"))
		  {
			  Url="http://"+getIpAddress()+RESTConstants.LOGIN_URL_APPSTACK;
			  RESTClient rc=new RESTClient();
			  if(rc.ExecuteHEAD(Url,this.getUsername(),this.getPassword())==true)
			  {
				  return APPSTACK;
			  }
			  else
			  {
		
				  return null;
			  }
		  }
	  }
	  setPort("8765");
	  return GATEWAY;
	  
	  
  }
  public void rollback()
    throws OdaException
  {
  }

  public void setAppContext(Object arg0)
    throws OdaException
  {
  }

  public void setLocale(ULocale arg0)
    throws OdaException
  {
  }
  public RESTQuery newRESTQuery(String dataSetType) throws OdaException {
		
		 RESTQuery aQuery =this.restqueryMap.get(dataSetType);
		 
		    if (aQuery != null) {
		     
		      return aQuery;
		    }
		    aQuery = new RESTQuery(dataSetType,this);
		    this.restqueryMap.put(dataSetType, aQuery);

		    return aQuery;
	}
public IQuery newQuery(String dataSetType) throws OdaException {
	
	 IQuery aQuery =this.queryMap.get(dataSetType);
	 
	    if (aQuery != null) {
	     
	      return aQuery;
	    }
	    aQuery = new RESTQuery(dataSetType,this);
	    this.queryMap.put(dataSetType, aQuery);

	    return aQuery;
}
}