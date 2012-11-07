package oda.datatools.connectivity.rest.impl;

import org.eclipse.datatools.connectivity.oda.OdaException;

public interface SearchRequest {


	public RESTList executeQuery() throws OdaException;
	public void setQueryText(String Query);
	public int getOffset();
	public  void setOffset(int offset);
	public int getlimit();
	public  void setlimit(int limit);
	public void setRESTlist(RESTList siqlist);
	public RESTList getRESTlist();
	
}
