package oda.datatools.connectivity.rest.siq.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTColumnsExtract implements RESTConstants {
	
	
	private String response=null;
	List<String> columnnames;
	Map<String,HashMap<String,String>> coldatatypes;
	List<String> datatypes;
	private JSONObject response_version;
	private JSONObject jb;
	private Object obj;
	private Object object;
	public HashMap<String,HashMap<String,String>> resources_name;
	public HashMap<String,HashMap<String,String>> columnmapping;
	public HashMap<String,HashMap<String,Object>> rawcolumnmapping;
	public HashMap<String,String> complicatedcolumnmapping;
	private List<String> apilist;
	public  HashMap<String,List<String>> versions;
	private RESTConnection connection;
	private HashMap<String,List<String>> complicatedcolumns;
	
	public HashMap<String, List<String>> getComplicatedcolumns() {
		return complicatedcolumns;
	}
	public HashMap<String, HashMap<String, String>> getResources() {
		return resources_name;
	}	
	public JSONObject getResponse_version() {
		return response_version;
	}
	public void setResponse_version(JSONObject response_version) {
		this.response_version = response_version;
	}
	public RESTColumnsExtract(RESTConnection connectionarg)
	{
		columnnames=new LinkedList<String>();
		datatypes=new LinkedList<String>();
		versions=new HashMap<String,List<String>>();
		resources_name=new HashMap<String,HashMap<String,String>>();
		columnmapping=new HashMap<String,HashMap<String,String>>();	
		complicatedcolumnmapping=new HashMap<String,String>();
		rawcolumnmapping=new HashMap<String,HashMap<String,Object>>();
		apilist=new LinkedList<String>();
		connection=connectionarg;
		complicatedcolumns=new HashMap<String,List<String>>();
	}
	public List<String> getColumnnames() {
		return columnnames;
	}
	public void setColumnnames(List<String> columnnames)
	{
		this.columnnames = columnnames;
	
		for(int i=0;i<columnnames.size();i++)
		{
			if(columnnames.get(i).contains("@"))
			{
				String[] array=columnnames.get(i).split("@");
				
				for(int j=1;j<array.length;j++)
				{
					if(complicatedcolumns.get(array[0])==null)
					{
						List<String> child=new LinkedList<String>();
						child.add(array[j]);
						complicatedcolumns.put(array[0], child);
					}
					else
					{
						complicatedcolumns.get(array[0]).add(array[j]);
					}
					
				}		
			}
		}
		System.out.println("the columnsextrac "+complicatedcolumns);
	
		
	}
	public List<String> getDatatypes() {
		return datatypes;
	}
	public void setDatatypes(List<String> datatypes) {
		this.datatypes = datatypes;
	}
	public HashMap<String, HashMap<String, String>> getColumnmapping() {
		return columnmapping;
	}
	
	public HashMap<String, String> getComplicatedColumnmapping(String resource,String column,String datatype) 
	{
	
		
		HashMap<String,Object> rawcolumn=rawcolumnmapping.get(resource);
	
		JSONObject jb=JSONObject.fromObject(rawcolumn.get(column));
		
		if(jb.get(DATATYPE).equals(RESTConstants.COMP_DATA_TYPE1))
		{
			Object item=jb.get(SEQUENCE_DATATYPE_PASS1);
			jb=JSONObject.fromObject(item);
			Object structure=jb.get(SEQUENCE_DATATYPE_PASS2);
			jb=JSONObject.fromObject(structure);
			Iterator jbi = jb.entrySet().iterator();
			while (jbi.hasNext())
		    { 
		    	Map.Entry e = (Map.Entry)jbi.next();
		    	System.out.println("the line "+e.getValue());
		    	Object objvalue=e.getValue();
			    jb=JSONObject.fromObject(objvalue);
			    complicatedcolumnmapping.put((String) e.getKey(), jb.get(DATATYPE).toString());
		    }
		}
		
		return complicatedcolumnmapping;
	}
	public HashMap<String, List<String>> getVersions() {
		return versions;
	}
	public void getResponse(String urlarray,String datasettype,RESTClient rc)
	{
		String api = null;
		System.out.println("");
		response=rc.getResponse();
		System.out.println("");
		if(datasettype.equals(RESTConstants.ODA_DATA_SET_UI_ID))
		{
			 String[] cons=urlarray.split(",");
			
			 if(cons[0].equals("API"))
			 {
					 api=cons[1];
			 }
		
		 Object VersionObj;
		 jb = JSONObject.fromObject(response);
		 VersionObj=jb.get("versions");
		 jb=JSONObject.fromObject(VersionObj);
		 System.out.println(jb+"columnsname");
		 Set<String> Versions = jb.keySet();
		 List<String> verparam=new LinkedList<String>();
		 for (final String version : Versions)
		 {
			 System.out.println(version+"columnsname");
			 verparam.add(version);
			 VersionObj=jb.get(version);
			 jb=JSONObject.fromObject(VersionObj);
			 
			 Set<String> resources=jb.keySet();
			 JSONObject jbversion=jb;
			 HashMap<String,String>temp= new HashMap<String,String>();
		
			
			 for (String resource : resources)
			 {	
				 System.out.println(resource+"columnsname");
				 Object resourceobject=jbversion.get(resource);
				
				 jb=JSONObject.fromObject(resourceobject);
				 Object Schema=jb.get(SCHEMA);
				 jb=JSONObject.fromObject(Schema);
				 Iterator jbi = jb.entrySet().iterator();
				 HashMap<String,String>column_data= new HashMap<String,String>();
				 HashMap<String,Object>raw_column_data= new HashMap<String,Object>();
				 while (jbi.hasNext())
			     { 
			    	Map.Entry e = (Map.Entry)jbi.next();
			    	Object objvalue=e.getValue();
				    jb=JSONObject.fromObject(objvalue);
				    column_data.put((String) e.getKey(), jb.get(DATATYPE).toString());
				    
				    if(jb.get(DATATYPE).equals(RESTConstants.COMP_DATA_TYPE1))
				    {
				    	  raw_column_data.put((String) e.getKey(), objvalue);
				    }
				    else if(jb.get(DATATYPE).equals(RESTConstants.COMP_DATA_TYPE2))
				    {
				    	  raw_column_data.put((String) e.getKey(), objvalue);
				    }    
			     }
				 System.out.println(resource+"columnsname"+column_data);
				 
				 temp.put(resource, resource);
				 resources_name.put(api,temp);
				 columnmapping.put(resource, column_data);
				 rawcolumnmapping.put(resource, raw_column_data);
			 }
			 
		 }
		 System.out.println("resource_name"+resources_name);
		 versions.put(api, verparam);
		}
		
	}
	@SuppressWarnings("unchecked")
	public void extract(String urlarray,String datasettype,RESTClient rc) throws OdaException  
	{
		
	  try {
			if(datasettype.equals(RESTConstants.ODA_DATA_SET_UI_ID))
			{
				 String[] cons=urlarray.split(",");
				 String api,url;
				 if(cons[0].equals("API"))
				 {
						 api=cons[1];
						 Object VersionObj;
						 url=connection.getUrl()+api+SPECIFICATION;
							if(!apilist.contains(api))
							{
								apilist.add(api);
								
								 if(connection.getApi().equals(RESTConstants.APPSTACK))
								 {
									 url="http://"+connection.getIpAddress()+RESTConstants.LOGIN_URL_APPSTACK;
									 if(rc.ExecuteHEAD(url,connection.getUsername(),connection.getPassword())==false)
									 {
										
										 throw new OdaException("Connection to datasource failed ,pls check the datasource again");
									 }
									 else
									 {
										
										 if(cons[1].equals(ENAMEL))
										 {
											 url="http://"+connection.getIpAddress()+connection.APPSTACK_ENAMEL_SPECIFICATION;
										 }
									 }
									
								 }
								 else  if(connection.getApi().equals(RESTConstants.GATEWAY))
								 {	
										 url=connection.getUrl()+api+SPECIFICATION;					 
								 }
								
								 rc.ExecuteGet(null, url,datasettype);
						}
								
					}
				 }
	  }
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OdaException("Error in extracting column names");
		}
	}
	

}
