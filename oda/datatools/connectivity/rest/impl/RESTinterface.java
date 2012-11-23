package oda.datatools.connectivity.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.NameValuePair;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTInterface {

	private Vector<String> query;
	private Vector<RequestMethod> methodlist;
	private Vector<Map<String, Object>> paramlist;
	private Vector<Map<String, String>> datamappingstringlist;
	private Vector<ColumnNameMapping> columnmappinglist;
	private Map<String, Object> datamappingsvalues;
	private Map<Object, Object> columnsmappingsvalues;
	private int position=0;
	private RESTList siqlist;
	private RESTClient restclient;
	private String response;
	private int offset;
	private int limit;
	private boolean limitreached;
	private ColumnNameMapping columnnamemapping;

	public void setQuery(Vector<String> query) {
		this.query = query;
	}
	public void setMethodlist(Vector<RequestMethod> methodlist) {
		this.methodlist = methodlist;
	}
	public void setParamlist(Vector<Map<String, Object>> paramlist) {
		this.paramlist = paramlist;
	}
	public void setDatamappingstringlist(
			Vector<Map<String, String>> datamappingstringlist) {
		this.datamappingstringlist = datamappingstringlist;
	}
	
	
	public void setcolumnmappinglist(
			Vector<ColumnNameMapping> columnmappinglist) {
		this.columnmappinglist = columnmappinglist;
	}

	public void prepare()
	{
		restclient=new RESTClient();
		limit=500;
		offset=0;
		limitreached=false;
		response=null;
		columnsmappingsvalues=new HashMap<Object, Object>();
		columnnamemapping=null;
	
	}
	private int fillColumnMappings(String response,ColumnNameMapping columnnamemapping)
	{    
		  JSONObject jb = JSONObject.fromObject(response);
	      JSONArray jsonresourceobj = null;
	      Iterator i = jb.entrySet().iterator();
	      while (i.hasNext()) {
	        Map.Entry e = (Map.Entry)i.next();
	        if (e.getKey().equals("resources"))
	        {
	          jsonresourceobj = (JSONArray)e.getValue();
	        }
	        
	      }
	      for (int j = 0; j < jsonresourceobj.size(); j++)
		  {
		        JSONObject obj1 = jsonresourceobj.getJSONObject(j);
	        this.columnsmappingsvalues.put(obj1.get(columnnamemapping.getSourcekey()), obj1.get(columnnamemapping.getSourcevalue()));
		  }
	     return jsonresourceobj.size();
	}
	public RESTList executeQuery() throws OdaException
	{
		if(limitreached)
		{
			this.siqlist.reset();
			return this.siqlist;
		}
		while(position<query.size())
		{
			RequestMethod method=methodlist.get(position);
			Map<String, Object> param=paramlist.get(position);
			 switch(method) 
	         {
	            case GET:
	             {
	            
					if(datamappingsvalues!=null)
					{
					
						for (Map.Entry<String, Object> entry : datamappingsvalues.entrySet())
						{
							param.put(entry.getKey(),entry.getValue());
						}
					}
					ArrayList<NameValuePair> params =new ArrayList<NameValuePair>();
					for (Map.Entry<String, Object> entry : param.entrySet())
					{
						params.add(new NameValuePair(entry.getKey(),entry.getValue().toString()));
					}
					params.add(new NameValuePair("offset",String.valueOf(offset)));
					params.add(new NameValuePair("limit",String.valueOf(limit)));
					offset+=limit;
					
					try
					{
						response=restclient.ExecuteGet(params,query.get(position));
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						throw new OdaException("Data from the Server has Exception");
					}
					columnnamemapping=columnmappinglist.get(position);
					if(columnnamemapping!=null)
					{
						int remaining=this.fillColumnMappings(response, columnnamemapping);
						if(remaining>=limit)
						{
							continue;
						}
					}
					this.siqlist.reset();
					break;
	             }
	            case POST: 
	            {
		            List<JSONObject> jsonlist=new LinkedList<JSONObject>();	
					for (Map.Entry<String, Object> entry : param.entrySet())
					{
						Object jsonobj=entry.getValue();
						JSONObject jb;
						try
						{
							 jb=JSONObject.fromObject(jsonobj);
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
							throw new OdaException("Data from the Client is not a jason object");
						}
						jsonlist.add(jb);
					
					}
					
					try
					{
						response=restclient.ExecutePost(jsonlist,query.get(position));
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						throw new OdaException("Data from the Server has Exception");
					}
					Map<String,Object> returnstring=new HashMap<String,Object>();
					if(response!=null)
					{
						RESTJsonExtract.JSONcatalyst(response, returnstring);
					}
					else
					{
						throw new OdaException("Gateway has failed the response");
					}
					datamappingsvalues=new HashMap<String, Object>();
					Map<String, String> datamappingstring=datamappingstringlist.get(position);
					for (Map.Entry<String, String> entry : datamappingstring.entrySet())
					{
						datamappingsvalues.put(entry.getValue(),returnstring.get(entry.getKey()));
					}
					break;
		       }
	         }
			if(position==query.size()-1)
			{
				if (response != null)
			    {
					  JSONObject jb = JSONObject.fromObject(response);
				      JSONArray jsonresourceobj = null;
				      Iterator i = jb.entrySet().iterator();
				      while (i.hasNext()) {
				        Map.Entry e = (Map.Entry)i.next();
				        if (e.getKey().equals("resources"))
				        {
				          jsonresourceobj = (JSONArray)e.getValue();
				        }
				        
				      }
				   
				      for (int j = 0; j < jsonresourceobj.size(); j++)
					  {
				    	    siqlist.createRow();
					        JSONObject obj1 = jsonresourceobj.getJSONObject(j);
					        extraction(obj1.toString(),j);  
					        siqlist.addtoRowList();
					    
					  }
			      }
				response=null;
				if(siqlist.getRows().size()<500)
				{
					limitreached=true;
				}
			
				return siqlist;
			}
			else
			{
				position++;
			}
			
		}
		
		return siqlist;
		
	
	}
	public RESTList getRESTlist() {
		return siqlist;
	}
	public void setRESTlist(RESTList siqlist) {
		this.siqlist = siqlist;
	}
	
	public void extraction(String obj1, int col) {
	    JSONObject jsonobj = JSONObject.fromObject(obj1.toString());
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) {
	      Map.Entry e = (Map.Entry)i.next();
	      if (e.getValue().getClass().getName() != "net.sf.json.JSONObject")
	      {
	    	    	for(int j=0;j<columnmappinglist.size();j++)
	    	    	{
	    	    		ColumnNameMapping columnnamemapping=columnmappinglist.get(j);
	    	    		if(columnnamemapping!=null)
	    	    		{
	    	    			int position=this.siqlist.getColumnlist().indexOf(e.getKey());
	    	    	    	if(position!=-1)
	    	    	    	if(columnnamemapping.getDestinationkey().equals(e.getKey())&&columnsmappingsvalues.get(e.getValue())!=null)
	    	    	    	{
	    	    	    		  
	    	    			    	this.siqlist.addObj(columnsmappingsvalues.get(e.getValue()),position);
	    	    	    	}
	    	    	    	else
	    	    	    	{
	    	    	    			this.siqlist.addObj(e.getValue(),position);
	    	    	    	}
	    	    		}
    	    		    else
    		    	    {
    		    	    	int position=this.siqlist.getColumnlist().indexOf(e.getKey());
    		   		    	if(position!=-1)
    		   		    	this.siqlist.addObj(e.getValue(),position);
    		    	    }
	    	    	}
	    	   
	    	 
	      }
	    
	    }
	}
	
	/* 
	 public void catalyst2(String obj1, int col,Object key,Object value) {
	    JSONObject jsonobj = JSONObject.fromObject(obj1.toString());
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) {
	      Map.Entry e = (Map.Entry)i.next();
	      if (e.getValue().getClass().getName() == "net.sf.json.JSONObject")
	      {
	    	  catalyst2(e.getValue().toString(), col,key,value);
	      }
	      else
	      {
	    	int position=this.siqlist.getColumnlist().indexOf(e.getKey());
	    	if(position!=-1)
	    	{
	    		if(e.getValue().equals(key))
	    		{
	    			this.siqlist.addObj(value,position);
	    		}
	    		else
	    		{
	    			this.siqlist.addObj(e.getValue().toString(),position);
	    		}
	    		
	    	}
	    	
	      }
	    }
	  } 
	 public void catalyst1(String obj1, int col,Map<String, String> columnmappingstring) {
		    JSONObject jsonobj = JSONObject.fromObject(obj1.toString());
		    Iterator i = jsonobj.entrySet().iterator();
		    while (i.hasNext()) {
		      Map.Entry e = (Map.Entry)i.next();
		      if (e.getValue().getClass().getName() == "net.sf.json.JSONObject")
		      {
		    	  catalyst1(e.getValue().toString(), col,columnmappingstring);
		      }
		      else
		      { 
		    	if(response!=null)
		    	{
		    		Object key = null;
		    		Object value = null;
		    		for (Map.Entry<String, String> entry : columnmappingstring.entrySet())
					{
		    			key=jsonobj.get(entry.getKey());
		    			value=jsonobj.get(entry.getValue());
		    			
					}

		    		  JSONObject jb = JSONObject.fromObject(response);
				      JSONArray jsonresourceobj = null;
				      Iterator it = jb.entrySet().iterator();
				      while (it.hasNext()) {
				        Map.Entry en = (Map.Entry)it.next();
				        if (en.getKey().equals("resources"))
				        {
				          jsonresourceobj = (JSONArray)en.getValue();
				        }
				      }
				      for (int j = 0; j < jsonresourceobj.size(); j++)
					  {
					        
					        JSONObject obj2 = jsonresourceobj.getJSONObject(j);
					      	this.siqlist.createRow();
				        	catalyst2(obj2.toString(),j,key,value);
				        	if(this.siqlist.getRow().contains(value))
				        	{
				        		  this.siqlist.addtoRowList();   
				        	}
						  
					  }
				      return ;
		    	}
		    	else
		    	{
		    		int position=this.siqlist.getColumnlist().indexOf(e.getKey());
			    	if(position!=-1)
			    	this.siqlist.addObj(e.getValue(),position);
		    	}
		    
		      }
		    }
		  }
		  */
	
}
