package oda.datatools.connectivity.rest.siq.impl;

import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;


public class RESTJsonExtract {
		
	  public static  void JSONcatalyst(String obj1,Map<String,Object> returnstring) {
	    JSONObject jsonobj = JSONObject.fromObject(obj1);
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) {
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
	  
	  public static  void JSONColumncatalyst(String obj1,Map<String,Object> returnstring) {
		    JSONObject jsonobj = JSONObject.fromObject(obj1);
		    Iterator i = jsonobj.entrySet().iterator();
		    while (i.hasNext()) {
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
