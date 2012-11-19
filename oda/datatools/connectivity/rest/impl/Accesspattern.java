package oda.datatools.connectivity.rest.impl;

import java.util.Map;
import java.util.Vector;

public class Accesspattern{

	private int offset;
	private Vector<String> query;
	public Vector<String> getQuery() {
		return query;
	}
	public Vector<RequestMethod> getMethod() {
		return method;
	}
	public Vector<Map<String, Object>> getParam() {
		return param;
	}
	public Vector<Boolean> getDatamapping() {
		return datamapping;
	}
	public Vector<Map<String, String>> getDatamappingstring() {
		return datamappingstring;
	}
	private Vector<RequestMethod> method;
	private Vector<Map<String, Object>> param;
	private Vector<Boolean> datamapping;
	private Vector<Map<String, String>> datamappingstring;
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	private int limit;
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public void setQuery(String query, RequestMethod method,
			Map<String, Object> param,boolean datamapping,Map<String, String> datamappingstring) {
			this.query.add(query);
			this.method.add(method);
			this.param.add(param);
			this.datamapping.add(datamapping);
			this.datamappingstring.add(datamappingstring);
		
	}
	public Accesspattern()
	{
		 query=new  Vector<String>();
		 method=new Vector<RequestMethod>();
		 param=new  Vector<Map<String, Object>>();
		 datamapping=new Vector<Boolean>();
		 datamappingstring=new Vector<Map<String, String>>();
	}
	
	





}
