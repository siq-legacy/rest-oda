package oda.datatools.connectivity.rest.siq.impl;

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

	private String[]  query;
	private Vector<RequestMethod> requestMethodList;
	private Vector<Map<String, Object>> parameterList;
	private Vector<Map<String, String>> dataMappingList;
	private Vector<ColumnNameMapping> columnMappingList;
	private Map<String, Object> dataMapping;
	private Map<Object, Object> columnMapping;
	private Map<String,Integer> arraysize;
	private Map<String,Integer> arrayrunning;
	private Map<Integer, String> arrayresponse;
	private int arrayresponsecount;
	public int getArrayresponsecount() {
		return arrayresponsecount;
	}
	public void setArrayresponsecount() {
		this.arrayresponsecount = arrayresponsecount+1;
	}
	private int position=0;
	private RESTList siqList;
	private RESTClient restClient;
	private String response;
	private int offset;
	private int limit;
	private boolean limitReached;
	private ColumnNameMapping columnNameMapping;

	public void setQuery(String[] query) {
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

	public String login_app_url(String url)
	{
	url=url.substring(0,url.indexOf('/',8))+RESTConstants.LOGIN_URL_APPSTACK;
		return url;
		
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
		arraysize=new HashMap<String,Integer>();
		arrayresponse=new HashMap<Integer,String>();

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
	public RESTList executeQuery(RESTConnection connection,String datasettype) throws OdaException
	{
		
		if(limitReached)
		{
			this.siqList.reset();
			return this.siqList;
		}
		while(position<query.length)
		{
			RequestMethod method=requestMethodList.get(position);
			
			if(method==null)
			{
				/*
				 * This is a test where UI design is used using the NON-UI dataset so that this can be used as a template without any changes from the design part later"
				 */
			}
			
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
					
					
					try
					{
						if(connection.getApi().equalsIgnoreCase(RESTConstants.APPSTACK))
						 {
							 if(restClient.ExecuteHEAD(login_app_url(query[position]),connection.getUsername(),connection.getPassword())==false)
							 {
								
								 throw new OdaException("Connection to datasource failed ,pls check the datasource again");
							 }
							 else
							 {
								 StringBuffer url=new StringBuffer(query[position]);	 
								 url.insert(query[position].indexOf('/',10)+1,RESTConstants.APPSTACK_PROXY).toString();
								 query[position]=url.toString();
							 }
							 
							 
							 
							 response=restClient.ExecuteGet(null,query[position],datasettype);
						 }
						else
						{
							params.add(new NameValuePair("offset",String.valueOf(offset)));
							params.add(new NameValuePair("limit",String.valueOf(limit)));
							offset+=limit;
							response=restClient.ExecuteGet(params,query[position],datasettype);
							
						}
						
					
						
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
						throw new OdaException("Data from the Server has Exception");
					}
					System.out.println("the Query "+restClient.getUrl());
					System.out.println("the response"+response);
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
						response=restClient.ExecutePost(jsonlist,query[position],datasettype);
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
			if(position==query.length-1)
			{
				if (response != null)
			    {
					
					
			    }
				response=null;
				System.out.println("the siqlist "+siqList.getRows());
				if(siqList.getRows().size()<500)
				{
					limitReached=true;
				}
			
				position=0;
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
	public void reponsethreading()
	{

		  JSONObject jb = JSONObject.fromObject(response);
	      JSONArray jsonresourceobj = null;
	      Iterator i = jb.entrySet().iterator();
	      while (i.hasNext())
	      {
	        Map.Entry e = (Map.Entry)i.next();
	        if (e.getKey().equals("resources"))
	        {
	          jsonresourceobj = (JSONArray)e.getValue();
	          for (int j = 0; j < jsonresourceobj.size(); j++)
			  {
	        	  
	        	    arraysize=new HashMap<String,Integer>();
	        	    arrayrunning=new HashMap<String,Integer>();
	        	    setArrayresponsecount();
	        	    siqList.createRow();
			        JSONObject obj1 = jsonresourceobj.getJSONObject(j);
			        arrayresponse.put(this.getArrayresponsecount(),obj1.toString());
			        extraction(null,obj1.toString(),false,false);  
			        siqList.addtoRowList();
			        
			  }
	        }
	        else
	        {
	        	if(e.getKey().toString().equals("total"))
	        		continue;
	        	arraysize=new HashMap<String,Integer>();
	      	    arrayrunning=new HashMap<String,Integer>();
	      	    setArrayresponsecount();
	        	siqList.createRow();
	        	arrayresponse.put(this.getArrayresponsecount(),response.toString());
	        	extraction(null,response.toString(),false,false);
	        	siqList.addtoRowList();
	        	break;
	        	
	        	
	        }
	      }   
    
	}
	public boolean extraction(String parent,String JSONString,boolean parentcheck,boolean levelcheck)
	{	
	    JSONObject jsonobj = JSONObject.fromObject(JSONString);
	    Iterator i = jsonobj.entrySet().iterator();
	    while (i.hasNext()) 
	    {
	      Map.Entry e = (Map.Entry)i.next();
	      
	      if (e.getValue().getClass().getName() == "net.sf.json.JSONArray"||e.getValue().getClass().getName() == "net.sf.json.JSONObject")
	      {
	    	 
	    	  if(parent==null)
	    	  {
	    		  parent=e.getKey().toString();
	  	    	
	    	  }
	    	  else
	    	  {
	    		  parent=parent+"@"+e.getKey().toString();
	    	  }
	    	
	    	  if(columncheck(parent,this.siqList.getColumnlist()))
			  {
	    		  String type=checkobjectarray(e.getValue());
	    		  switch(type)
	    		  {
	    		  		case "object":
	    		  			extraction(parent,e.getValue().toString(),true,true);
	    		  			break;
	    		  		case "array":
	    		  			JSONArray jarray=(JSONArray)e.getValue();
	    		  			if(arraysize.get(parent)==null)
	    		  			{
	    		  				arraysize.put(parent, jarray.size());
	    		  			}	
	    		  			if(arrayrunning.get(parent)==null)
	    		  			{
	    		  				arrayrunning.put(parent, -1);
	    		  			}
	    		  			if(arrayrunning.get(parent).equals(arraysize.get(parent)-1))
	    		  			{
	    		  				extraction(parent,jarray.getString(arrayrunning.get(parent)).toString(),true,true);
	    		  				
	    		  			}
	    		  			else
	    		  			{
	    		  				arrayrunning.put(parent, arrayrunning.get(parent)+1);
	    		  				extraction(parent,jarray.getString(arrayrunning.get(parent)).toString(),true,true);
	    		  			}
	    		  			break;
	    		  }  
			  }
	      }
	      else
	      {
		    	boolean check=false;
	  	  		for(int j=0;j<columnMappingList.size();j++)
	  	    	{
	  	    		ColumnNameMapping columnNameMapping=columnMappingList.get(j);
	  	    		if(columnNameMapping!=null)
	  	    		{
	  	        	
	  	    			int position=this.siqList.getColumnlist().indexOf(e.getKey());
	  	    	    	if(position!=-1)
	  	    	    	if(columnNameMapping.getDestinationKey().equals(e.getKey())&&columnMapping.get(e.getValue())!=null)
	  	    	    	{
	  	    	    			position=this.siqList.getColumnlist().indexOf(columnNameMapping.getSourceValue());
	  	    	    			
	  	    	    			if(this.siqList.getDatatype().get(this.siqList.getColumnlist().indexOf(columnNameMapping.getSourceValue())).equalsIgnoreCase("uuid"))
	  	    	    			{
	  	    	    				this.siqList.addObj(columnMapping.get(e.getValue()).toString().replaceAll("-", "").toUpperCase(),position);
	  	    	    			}
	  	    	    			else
	  	    	    			{
	  	    	    				this.siqList.addObj(columnMapping.get(e.getValue()),position);
	  	    	    			}
	  	    			    
	  	    	
	  	    	    	}
	  	    	    	else
	  	    	    	{
		  	    	    		if(this.siqList.getDatatype().get(this.siqList.getColumnlist().indexOf(e.getKey())).equalsIgnoreCase("uuid"))
	  	    	    			{
		  	    	    			this.siqList.addObj(e.getValue().toString().replaceAll("-", "").toUpperCase(),position);
	  	    	    			}
		  	    	    		else
		  	    	    		{
		  	    	    			this.siqList.addObj(e.getValue(),position);
		  	    	    		}
		  	    	    			
	  	    	    		
	  	    	    	}
	  	    	    	check=true;
	  	    	   }
	  	    	 
		    		   
	  	    	}
	  	  		if(!check)
		    	    {
	  	  			
	  	  			
		  	  			String parentparam=null;
			    		if(parentcheck)
			    		{
			    			parentparam=parent;
			    			parentparam=parentparam+"@"+e.getKey().toString();
			    		}
			    	
			    		if(parentparam!=null)
			    		{
		   		    		position=this.siqList.getColumnlist().indexOf(parentparam);
			   		    	if(position!=-1)
			   		    	{
			   		    		if(this.siqList.getDatatype().get(this.siqList.getColumnlist().indexOf(parentparam)).equalsIgnoreCase("uuid"))
	  	    	    			{
			   		    			this.siqList.addObj(e.getValue().toString().replaceAll("-", "").toUpperCase(),position);
	  	    	    			}
			   		    		else
			   		    		{
			   		    			this.siqList.addObj(e.getValue(),position);
			   		    		}
			   		    		
			   		    	}
			    		}
			    		else
			    		{
			    			int position=this.siqList.getColumnlist().indexOf(e.getKey());
			   		    	if(position!=-1)
			   		    	{
			   		    		if(this.siqList.getDatatype().get(this.siqList.getColumnlist().indexOf(e.getKey())).equalsIgnoreCase("uuid"))
	  	    	    			{
		  	    	    			this.siqList.addObj(e.getValue().toString().replaceAll("-", "").toUpperCase(),position);
	  	    	    			}
		  	    	    		else
		  	    	    		{
		  	    	    			this.siqList.addObj(e.getValue(),position);
		  	    	    		}
			   		    		
			   		    	}
			   		    	
			    		}
		    	    	
		   		    	
		    	    }
	      }
	    	  
	    }
	    boolean pcheck=false;
	    for (Map.Entry<String, Integer> entry : arraysize.entrySet())
		{
	    	if(arrayrunning.get(entry.getKey()).equals(entry.getValue()-1))
	    	{
	    		pcheck=false;
	    	}
	    	else
	    	{
	    		pcheck=true;
	    		break;
	    	}
		}
	    if(!levelcheck&&pcheck)
		{
			siqList.addtoRowList();
		 	siqList.createRow();
    		extraction(null,arrayresponse.get(this.getArrayresponsecount()),false,false);
    		
		}
	    return pcheck;
	    
	}
	
	
	public String checkobjectarray(Object value)
	{
		if(value instanceof JSONObject)
		{
			return "object";
		}
		else if(value instanceof JSONArray)
		{
			return "array";
		}
		else
		{
			return null;
		}
	}
	public boolean columncheck(String item,List<String> columnlist)
	{
		List<String> child=this.getRESTlist().getComplicatedcolumns().get(item);
	
		if(child!=null)
			return true;
		else 
			return false;
		
	}

}