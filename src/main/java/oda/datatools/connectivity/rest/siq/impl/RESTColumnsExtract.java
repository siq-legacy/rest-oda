package oda.datatools.connectivity.rest.siq.impl;

import java.util.ArrayList;
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
	private RESTList restlist;
	private JSONObject jb;
	private Object obj;
	private Object object;
	public HashMap<String,HashMap<String,String>> resources_name;
	public HashMap<String,HashMap<String,String>> columnmapping;
	public HashMap<String, HashMap<String, String>> getResources() {
		return resources_name;
	}
	public  HashMap<String,List<String>> versions;
	
	
	public JSONObject getResponse_version() {
		return response_version;
	}
	public void setResponse_version(JSONObject response_version) {
		this.response_version = response_version;
	}
	private void queryConstruct()
	{
		
	}
	public RESTColumnsExtract(RESTList restlistarg)
	{
		columnnames=new LinkedList<String>();
		datatypes=new LinkedList<String>();
		restlist=restlistarg;
		versions=new HashMap<String,List<String>>();
		resources_name=new HashMap<String,HashMap<String,String>>();
		columnmapping=new HashMap<String,HashMap<String,String>>();
		
		
	}
	public HashMap<String, HashMap<String, String>> getColumnmapping() {
		return columnmapping;
	}
	public HashMap<String, List<String>> getVersions() {
		return versions;
	}
	@SuppressWarnings("unchecked")
	public void extract(String urlarray,String datasettype) throws OdaException  
	{
		RESTClient rc=new RESTClient();
	  try {
			if(datasettype.equals(RESTConstants.ODA_DATA_SET_UI_ID))
			{
				 String[] cons=urlarray.split("=");
				 String api,url;
				 if(cons[0].equals("API"))
				 {
						 api=cons[1];
						 Object VersionObj;
						 url=RESTConnection.getUrl()+api+SPECIFICATION;
						
						
						 System.out.println("the url"+url);
						 if(response==null)
						 {
							 if(RESTConnection.getApi().equals(RESTConstants.APPSTACK))
							 {
								 url="http://"+RESTConnection.getIpAddress()+RESTConstants.LOGIN_URL_APPSTACK;
								 if(rc.ExecuteHEAD(url,RESTConnection.getUsername(),RESTConnection.getPassword())==false)
								 {
									
									 throw new OdaException("Connection to datasource failed ,pls check the datasource again");
								 }
								 else
								 {
									 System.out.println("cons[1]"+cons[1]);
									 if(cons[1].equals(ENAMEL))
									 {
										 url="http://"+RESTConnection.getIpAddress()+RESTConnection.APPSTACK_ENAMEL_SPECIFICATION;
									 }
								 }
								
							 }
							 else  if(RESTConnection.getApi().equals(RESTConstants.GATEWAY))
							 {
								 if(cons[1].equals(EXPLORERS))
								 {
									 url=RESTConnection.getUrl()+api+SPECIFICATION;
								 }
								 
							 }
							 System.out.println(url);
							 response=rc.ExecuteGet(null, url);
							 jb = JSONObject.fromObject(response);
							 System.out.println("the response "+response);
							 VersionObj=jb.get("versions");
							 jb=JSONObject.fromObject(VersionObj);
						 }
						 Set<String> Versions = jb.keySet();
						 List<String> verparam=new LinkedList<String>();
						 for (final String version : Versions)
						 {
							
							 verparam.add(version);
							 VersionObj=jb.get(version);
							 jb=JSONObject.fromObject(VersionObj);
							 
							 Set<String> resources=jb.keySet();
							 JSONObject jbversion=jb;
							 HashMap<String,String>temp= new HashMap<String,String>();
							 System.out.println("version"+version);
							 for (String resource : resources)
							 {	
								 Object resourceobject=jbversion.get(resource);
								
								 jb=JSONObject.fromObject(resourceobject);
								 Object Schema=jb.get(SCHEMA);
								 jb=JSONObject.fromObject(Schema);
								 Iterator jbi = jb.entrySet().iterator();
								 HashMap<String,String>column_data= new HashMap<String,String>();
								 while (jbi.hasNext())
							     {
									 
							    	Map.Entry e = (Map.Entry)jbi.next();
							    	Object objvalue=e.getValue();
							    	 if(resource.equals("infoset"))
									 {
										 System.out.println("the reource "+objvalue);
									 }
								    jb=JSONObject.fromObject(objvalue);
								    column_data.put((String) e.getKey(), jb.get(DATATYPE).toString());
								     
							     }
								 System.out.println(resource+"columnsname"+column_data);
								 
								 temp.put(resource, resource);
								 resources_name.put(api,temp);
								 columnmapping.put(resource, column_data);
							 }
							 
						 }
						 System.out.println("resource_name"+resources_name);
						 versions.put(api, verparam);
				}
				
				
			}		
			else
			{
					 //url=http://172.17.18.64:8765/explorers/1.0/contenttype
			
					 String[] url=urlarray.split(";");
					
					 for(int i=0;i<url.length;i++)
					 {
						 int last_track=url[i].lastIndexOf('/');
						 
						 CharSequence sequence=url[i].subSequence(0,last_track);
						 //sequence=http://172.17.18.64:8765/explorers/1.0/
						 
						 String category=url[i].subSequence(url[i].lastIndexOf('/')+1,url[i].length()).toString();
						 //http://172.17.18.64:8765/explorers/1.0/
						 
						 String version=sequence.subSequence(sequence.toString().lastIndexOf('/')+1,sequence.length()).toString();
						 
						 String new_url=sequence.subSequence(0, sequence.toString().lastIndexOf('/'))+SPECIFICATION;
						 
							 
						 if(response==null)
						 {
							 response=rc.ExecuteGet(null, new_url);
							 jb = JSONObject.fromObject(response);
							 obj=jb.get("versions");
							 jb=JSONObject.fromObject(obj);
							 object=jb.get(version);
						 }
						
					
						 jb=JSONObject.fromObject(object);
						 this.setResponse_version(jb);
						 obj=jb.get(category);
						 jb=JSONObject.fromObject(obj);
						 obj=jb.get(SCHEMA);
						 jb=JSONObject.fromObject(obj);
						 Iterator jbi = jb.entrySet().iterator();
						
					     while (jbi.hasNext())
					     {
					    	 Map.Entry e = (Map.Entry)jbi.next();
					    	 if(columnnames.indexOf(e.getKey())==-1)
					    	 {
					    		 columnnames.add((String) e.getKey());
						    	 Object objvalue=e.getValue();
						    	 jb=JSONObject.fromObject(objvalue);
						    	 datatypes.add(jb.get(DATATYPE).toString());
						     }
					     }
					
							
						} 
					 restlist.setColumnlist(this.getColumns());
					 restlist.setDatatype(this.getDataTypes());
					 }
	  			}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
				e.printStackTrace();
				throw new OdaException("Error in extracting column names");
				}
	}
	public List<String> getColumns()
	{
		return columnnames;
		
	}
	public List<String> getDataTypes()
	{
	     return datatypes;
		
	}

}
