package oda.datatools.connectivity.rest.siq.impl;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.json.JSON;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;


public class RESTJsonExtract {
	
	  public static JSONObject parse(Object obj) throws JSONException
	  {
		   if(obj==null)
		   {
			   throw new JSONException("Object is Null");
		   }
		   JSONObject jsonobj;  
			try
			{
				jsonobj = (JSONObject)JSON.parse(obj.toString());
			}
			catch(Exception ex)
			{
				throw new JSONException("String Object is Null");
			}
			return jsonobj;
	  }
	  public static  void JSONcatalyst(String obj1,Map<String,Object> returnstring) throws JSONException {
		
		JSONObject jsonobj=RESTJsonExtract.parse(obj1);
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) {
	      Map.Entry e = (Map.Entry)i.next();
	      if (e.getValue().getClass().getName() == "org.apache.commons.json.JSONObject")
	      {
	    	  JSONcatalyst(e.getValue().toString(),returnstring);
	      }
	      else
	      {
	    	  returnstring.put(e.getKey().toString(), e.getValue().toString());
	    	  return ;
	      }
	    }
	    return ;
	  }
	  
	  public static  void JSONColumncatalyst(String obj1,Map<String,Object> returnstring) throws JSONException {
		    JSONObject jsonobj=RESTJsonExtract.parse(obj1);
		    Iterator i = jsonobj.entrySet().iterator();
		    while (i.hasNext()) 
		    {
		      Map.Entry e = (Map.Entry)i.next();
		      if (e.getValue().getClass().getName() == "net.sf.json.JSONObject")
		      {
		        JSONcatalyst(e.getValue().toString(),returnstring);
		      }
		      else
		      {
		    	  returnstring.put(e.getKey().toString(), e.getValue().toString());
		    	  return ;
		      }
		    }
		    return ;
		  }
}
