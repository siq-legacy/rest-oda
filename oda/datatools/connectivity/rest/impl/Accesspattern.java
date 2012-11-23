package oda.datatools.connectivity.rest.impl;

import java.util.Map;
import java.util.Vector;

public class AccessPattern{

	private Vector<String> query;
	private Vector<RequestMethod> method;
	private Vector<Map<String, Object>> param;
	private Vector<Map<String, String>> datamappingstring;
	private Vector<ColumnNameMapping> columnmappingstring;
	
	public Vector<String> getQuery() {
		return query;
	}
	
	public Vector<RequestMethod> getMethod() {
		return method;
	}
	public Vector<Map<String, Object>> getParam() {
		return param;
	}
	public Vector<Map<String, String>> getDatamappingstring() {
		return datamappingstring;
	}
	
	public Vector<ColumnNameMapping> getColumnmappingstring() {
		return columnmappingstring;
	}
	
	public void setQuery(String query, RequestMethod method,DataMapping datamapping) {
			this.query.add(query);
			this.method.add(method);
			this.param.add(datamapping.getParam());
			this.datamappingstring.add(datamapping.getResponseRequestMapping());
			this.columnmappingstring.add(datamapping.getColumnnamemapping());
		
	}
	public AccessPattern()
	{
		 query=new  Vector<String>();
		 method=new Vector<RequestMethod>();
		 param=new  Vector<Map<String, Object>>();
		 datamappingstring=new Vector<Map<String, String>>();
		 columnmappingstring=new Vector<ColumnNameMapping>();
	}
	
	





}
