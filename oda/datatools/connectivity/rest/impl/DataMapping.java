package oda.datatools.connectivity.rest.impl;

import java.util.HashMap;
import java.util.Map;

public class DataMapping {

	/**
	 * @param args
	 */
	private Map<String,String> responseRequestMapping;
	
	private ColumnNameMapping columnnamemapping;
	private Map<String, Object> param;

	public DataMapping()
	{
		responseRequestMapping=new HashMap<String,String>();
		param=new HashMap<String,Object>();
		
	}
	
	public void addColumnNameMapping(String sourcekey,String sourcevalue,String destinationkey)
	{
		columnnamemapping=new ColumnNameMapping();
		columnnamemapping.setDestinationkey(destinationkey);
		columnnamemapping.setSourcekey(sourcekey);
		columnnamemapping.setSourcevalue(sourcevalue);
	}
	public void addRequestResponseMapping(String response_name,String request_name)
	{
		responseRequestMapping.put(response_name, request_name);
	}
	public void addParameter(String paramName,String paramValue)
	{
		param.put(paramName, paramValue);
	}

	
	public ColumnNameMapping getColumnnamemapping() {
		return columnnamemapping;
	}
	public Map<String, String> getResponseRequestMapping() {
		return responseRequestMapping;
	}
	public Map<String, Object> getParam() {
		return param;
	}
	

	
}
