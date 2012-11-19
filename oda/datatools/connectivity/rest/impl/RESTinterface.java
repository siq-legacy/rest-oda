package oda.datatools.connectivity.rest.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTinterface {

	private Vector<String> query;

	public void setQuery(Vector<String> query) {
		this.query = query;
	}
	public void setMethodlist(Vector<RequestMethod> methodlist) {
		this.methodlist = methodlist;
	}
	public void setParamlist(Vector<Map<String, Object>> paramlist) {
		this.paramlist = paramlist;
	}
	public void setDatamappinglist(Vector<Boolean> datamappinglist) {
		this.datamappinglist = datamappinglist;
	}
	public void setDatamappingstringlist(
			Vector<Map<String, String>> datamappingstringlist) {
		this.datamappingstringlist = datamappingstringlist;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	private Vector<RequestMethod> methodlist;
	private Vector<Map<String, Object>> paramlist;
	private Vector<Boolean> datamappinglist;
	private Vector<Map<String, String>> datamappingstringlist;
	private Map<String, Object> datamappingsvalues;
	private int position=0;
	private RESTList siqlist;
	private RESTClient restclient;
	private String message;
	private int offset;
	private int limit;
	public void prepare()
	{
		restclient=new RESTClient();
		
		limit=500;
		offset=0;
	}
	public RESTList executeQuery() throws OdaException
	{
		
		while(position<query.size())
		{
			restclient.clearParameters();
			restclient.setURL(query.get(position));
			RequestMethod method=methodlist.get(position);
			Map<String, Object> param=paramlist.get(position);
			
			if(method.equals(RequestMethod.GET))
			{
				if(datamappingsvalues!=null)
				{
				
					for (Map.Entry<String, Object> entry : datamappingsvalues.entrySet())
					{
						param.put(entry.getKey(),entry.getValue());
					}
				}
				restclient.AddParam("offset", String.valueOf(offset));
				restclient.AddParam("limit", String.valueOf(limit));
				offset+=limit;
				for (Map.Entry<String, Object> entry : param.entrySet())
				{
					restclient.AddParam(entry.getKey(),entry.getValue().toString());
				}
				try
				{
					restclient.Execute(RequestMethod.GET);
					message=restclient.getResponse();
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					throw new OdaException("Data from the Server has Exception");
				}
				this.siqlist.freeMem();
				
			}
			else
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
				restclient.AddJsonPost(jsonlist);
				try
				{
					restclient.Execute(RequestMethod.POST);
					message=restclient.getResponse();	
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
					throw new OdaException("Data from the Server has Exception");
				}
				Map<String,Object> returnstring=new HashMap<String,Object>();
				if(message!=null)
				{
					RESTJsonExtract.JSONcatalyst(message, returnstring);
				}
				else
				{
					throw new OdaException("Gateway has failed the message");
				}
				Boolean datamapping= datamappinglist.get(position);
				if(datamapping.booleanValue()==true)
				{
					datamappingsvalues=new HashMap<String, Object>();
					Map<String, String> datamappingstring=datamappingstringlist.get(position);
					for (Map.Entry<String, String> entry : datamappingstring.entrySet())
					{
						datamappingsvalues.put(entry.getValue(),returnstring.get(entry.getKey()));
					}
				}	
				restclient.clearParameters();
				
			}
			if(position==query.size()-1)
			{
				if (message != null)
			    {
					  JSONObject jb = JSONObject.fromObject(message);
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
					        this.siqlist.createRow();
					        JSONObject obj1 = jsonresourceobj.getJSONObject(j);
					        catalyst(obj1.toString(),j);
					        this.siqlist.addtoRowList();
					        
					  }    
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
	 public void catalyst(String obj1, int col) {
		    JSONObject jsonobj = JSONObject.fromObject(obj1.toString());
		    Iterator i = jsonobj.entrySet().iterator();
		    while (i.hasNext()) {
		      Map.Entry e = (Map.Entry)i.next();
		      if (e.getValue().getClass().getName() == "net.sf.json.JSONObject")
		      {
		        catalyst(e.getValue().toString(), col);
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
