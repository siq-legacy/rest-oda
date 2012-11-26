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
	private Vector<RequestMethod> requestMethodList;
	private Vector<Map<String, Object>> parameterList;
	private Vector<Map<String, String>> dataMappingList;
	private Vector<ColumnNameMapping> columnMappingList;
	private Map<String, Object> dataMapping;
	private Map<Object, Object> columnMapping;
	private int position=0;
	private RESTList siqList;
	private RESTClient restClient;
	private String response;
	private int offset;
	private int limit;
	private boolean limitReached;
	private ColumnNameMapping columnNameMapping;

	public void setQuery(Vector<String> query) {
		this.query = query;
	}
	public void setRequestMethodList(Vector<RequestMethod> requestMethodList) {
		this.requestMethodList = requestMethodList;
	}
	public void setParameterList(Vector<Map<String, Object>> parameterList) {
		this.parameterList = parameterList;
	}
	public void setDataMappingList(
			Vector<Map<String, String>> dataMappingList) {
		this.dataMappingList = dataMappingList;
	}
	
	
	public void setColumnMappingList(
			Vector<ColumnNameMapping> columnMappingList) {
		this.columnMappingList = columnMappingList;
	}

	public void prepare()
	{
		restClient=new RESTClient();
		limit=500;
		offset=0;
		limitReached=false;
		response=null;
		columnMapping=new HashMap<Object, Object>();
		columnNameMapping=null;
	
	}
	private int fillColumnMappings(String response,ColumnNameMapping columnNameMapping)
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
	        this.columnMapping.put(obj1.get(columnNameMapping.getSourceKey()), obj1.get(columnNameMapping.getSourceValue()));
		  }
	     return jsonresourceobj.size();
	}
	public RESTList executeQuery() throws OdaException
	{
		if(limitReached)
		{
			this.siqList.reset();
			return this.siqList;
		}
		while(position<query.size())
		{
			RequestMethod method=requestMethodList.get(position);
			Map<String, Object> param=parameterList.get(position);
			 switch(method) 
	         {
	            case GET:
	             {
	            
					if(dataMapping!=null)
					{
					
						for (Map.Entry<String, Object> entry : dataMapping.entrySet())
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
						response=restClient.ExecuteGet(params,query.get(position));
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						throw new OdaException("Data from the Server has Exception");
					}
					columnNameMapping=columnMappingList.get(position);
					if(columnNameMapping!=null)
					{
						int remaining=this.fillColumnMappings(response, columnNameMapping);
						if(remaining>=limit)
						{
							continue;
						}
					}
					this.siqList.reset();
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
						response=restClient.ExecutePost(jsonlist,query.get(position));
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
					dataMapping=new HashMap<String, Object>();
					Map<String, String> datamappingstring=dataMappingList.get(position);
					for (Map.Entry<String, String> entry : datamappingstring.entrySet())
					{
						dataMapping.put(entry.getValue(),returnstring.get(entry.getKey()));
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
				    	    siqList.createRow();
					        JSONObject obj1 = jsonresourceobj.getJSONObject(j);
					        extraction(obj1.toString());  
					        siqList.addtoRowList();
					    
					  }
			      }
				response=null;
				if(siqList.getRows().size()<500)
				{
					limitReached=true;
				}
			
				return siqList;
			}
			else
			{
				position++;
			}
			
		}
		
		return siqList;
		
	
	}
	public RESTList getRESTlist() {
		return siqList;
	}
	public void setRESTlist(RESTList siqList) {
		this.siqList = siqList;
	}
	
	public void extraction(String JSONString) {
	    JSONObject jsonobj = JSONObject.fromObject(JSONString);
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) {
	      Map.Entry e = (Map.Entry)i.next();
	      if (e.getValue().getClass().getName() != "net.sf.json.JSONObject")
	      {
	    	    	for(int j=0;j<columnMappingList.size();j++)
	    	    	{
	    	    		ColumnNameMapping columnNameMapping=columnMappingList.get(j);
	    	    		if(columnNameMapping!=null)
	    	    		{
	    	    			int position=this.siqList.getColumnlist().indexOf(e.getKey());
	    	    	    	if(position!=-1)
	    	    	    	if(columnNameMapping.getDestinationKey().equals(e.getKey())&&columnMapping.get(e.getValue())!=null)
	    	    	    	{
	    	    	    		  
	    	    			    	this.siqList.addObj(columnMapping.get(e.getValue()),position);
	    	    	    	}
	    	    	    	else
	    	    	    	{
	    	    	    			this.siqList.addObj(e.getValue(),position);
	    	    	    	}
	    	    		}
    	    		    else
    		    	    {
    		    	    	int position=this.siqList.getColumnlist().indexOf(e.getKey());
    		   		    	if(position!=-1)
    		   		    	this.siqList.addObj(e.getValue(),position);
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
	    	int position=this.siqList.getColumnlist().indexOf(e.getKey());
	    	if(position!=-1)
	    	{
	    		if(e.getValue().equals(key))
	    		{
	    			this.siqList.addObj(value,position);
	    		}
	    		else
	    		{
	    			this.siqList.addObj(e.getValue().toString(),position);
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
					      	this.siqList.createRow();
				        	catalyst2(obj2.toString(),j,key,value);
				        	if(this.siqList.getRow().contains(value))
				        	{
				        		  this.siqList.addtoRowList();   
				        	}
						  
					  }
				      return ;
		    	}
		    	else
		    	{
		    		int position=this.siqList.getColumnlist().indexOf(e.getKey());
			    	if(position!=-1)
			    	this.siqList.addObj(e.getValue(),position);
		    	}
		    
		      }
		    }
		  }
		  */
	
}
