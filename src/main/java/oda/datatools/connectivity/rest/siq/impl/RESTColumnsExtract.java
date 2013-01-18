package oda.datatools.connectivity.rest.siq.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	private JSONObject jasonobj;
	private Object object;
	
	public JSONObject getResponse_version() {
		return response_version;
	}
	public void setResponse_version(JSONObject response_version) {
		this.response_version = response_version;
	}
	public RESTColumnsExtract(RESTList restlistarg)
	{
		columnnames=new LinkedList<String>();
		datatypes=new LinkedList<String>();
		restlist=restlistarg;
		
	}
	@SuppressWarnings("unchecked")
	public void extract(String urlarray) throws OdaException 
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
			 
			 
			 RESTClient rc=new RESTClient();
			 
			 try {
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
				 obj=jb.get("schema");
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
			
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OdaException("Error in extracting column names");
			}
		 }
		 restlist.setColumnlist(this.getColumns());
		 restlist.setDatatype(this.getDataTypes());
		 
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
