package oda.datatools.connectivity.rest.impl;

import java.util.Map;
import java.util.Vector;
/*
 * This Class is used for the Following:
 * 		This is the class from which user will be able to provide input to the ODA-rest Driver.
 * 		1.queryString	->a Vector which consist of Queries at various levels.
 * 		2.requestMethod	->a Vector which consist of an Enum which is either GET or POST for each level of request.
 * 		3.requestParameters ->a Vector which consist of parameters which can be needed in the POST or GET requests on each level.
 * 		4.dataMapping		->a Vector which consist of request and response mapping which is needed at each level.
 * 		5.columnMapping		->a Vector which consist of columns which can be needed to be mapped from one level to another in case of naming by a particular case.
 * 
 */
public class AccessPattern{

	private Vector<String> queryString;
	private Vector<RequestMethod> requestMethod;
	private Vector<Map<String, Object>> requestParameters;
	private Vector<Map<String, String>> dataMapping;
	private Vector<ColumnNameMapping> columnMapping;
	
	public Vector<String> getQueryString() {
		return queryString;
	}
	
	public Vector<RequestMethod> getRequestMethod() {
		return requestMethod;
	}
	public Vector<Map<String, Object>> getRequestParameters() {
		return requestParameters;
	}
	public Vector<Map<String, String>> getDataMapping() {
		return dataMapping;
	}
	
	public Vector<ColumnNameMapping> getColumnMapping() {
		return columnMapping;
	}
	
	public void setqueryString(String queryString, RequestMethod requestMethod,DataMapping datamapping) {
			this.queryString.add(queryString);
			this.requestMethod.add(requestMethod);
			this.requestParameters.add(datamapping.getParameters());
			this.dataMapping.add(datamapping.getResponseRequestMapping());
			this.columnMapping.add(datamapping.getColumnNameMapping());
		
	}
	public AccessPattern()
	{
		 queryString=new  Vector<String>();
		 requestMethod=new Vector<RequestMethod>();
		 requestParameters=new  Vector<Map<String, Object>>();
		 dataMapping=new Vector<Map<String, String>>();
		 columnMapping=new Vector<ColumnNameMapping>();
	}
	
	





}
