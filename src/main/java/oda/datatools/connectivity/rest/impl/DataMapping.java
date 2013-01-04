package oda.datatools.connectivity.rest.impl;

import java.util.HashMap;
import java.util.Map;

public class DataMapping {

	/**
	 * @param args
	 */
	private Map<String,String> responseRequestMapping;
	private ColumnNameMapping columnNameMapping;
	private Map<String, Object> parameters;

	
	public Map<String, String> getResponseRequestMapping() {
		return responseRequestMapping;
	}

	public ColumnNameMapping getColumnNameMapping() {
		return columnNameMapping;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
	public void addRequestResponseMapping(String response_name,String request_name)
	{
		responseRequestMapping.put(response_name, request_name);
	}
	public void addParameter(String paramName,String paramValue)
	{
		parameters.put(paramName, paramValue);
	}
	
	public DataMapping()
	{
		responseRequestMapping=new HashMap<String,String>();
		parameters=new HashMap<String,Object>();
		
	}
	
	public void addColumnNameMapping(String sourcekey,String sourcevalue,String destinationkey)
	{
		columnNameMapping=new ColumnNameMapping();
		columnNameMapping.setDestinationKey(destinationkey);
		columnNameMapping.setSourceKey(sourcekey);
		columnNameMapping.setSourceValue(sourcevalue);
	}
	
	

	
}
