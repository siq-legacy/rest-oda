package oda.datatools.connectivity.rest.siq.impl;

public interface RESTConstants {

	public final String SPECIFICATION="/_specification";
	public final String DATATYPE="__type__";
	public final String VERSIONS="versions";
	public final String SCHEMA="schema";
	
	
	public final int HTTP_PARTIAL=206;
	public final int HTTP_OK=200;
	public final int HTTP_=301;
	public final int HTTP_FORBIDDEN=403;
	
	public final static String ODA_DATA_SOURCE_ID = "oda.datatools.connectivity.rest.siq";
	public final static String ODA_DATA_SET_ID="oda.datatools.connectivity.rest.siq.dataSet";
	public final static String ODA_DATA_SET_UI_ID="oda.datatools.connectivity.rest.siq.dataSet.UI";
	
	public final String PROP_USERNAME="username";
	public final String PROP_PASSWORD="password";
	public final String PROP_IPADDRESS="ipaddress";
	public final String PROP_PORT="port";
	public final String PROP_APP="app";
	
	public final String LOGIN_URL_APPSTACK="/proxy/security/1.0/subject";
	public final String APPSTACK_ENAMEL_SPECIFICATION="/proxy/enamel/_specification";
	
	public final String APPSTACK="Appstack";
	public final String GATEWAY="Gateway";
	
	
	public final String EXPLORERS="exploreres";
	public final String ENAMEL="enamel";
	
	
}
