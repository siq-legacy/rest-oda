package oda.datatools.connectivity.rest.impl;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import com.ibm.icu.util.ULocale;
public class RESTConnection
  implements IConnection
{
 
  private boolean isOpen = false;
  private Map<String, IQuery> queryMap = new HashMap<String, IQuery>();
  public RESTConnection() {

    System.out.println("i got the connection");
  }

  public void close() throws OdaException {
 
    this.isOpen = false;
  }

  public void commit()
    throws OdaException
  {
	  
  }

  public int getMaxQueries()
    throws OdaException
  {
    return 0;
  }

  public IDataSetMetaData getMetaData(String datasettype)
    throws OdaException
  {
    return new RESTDataSetMetaData(this);
  }

  public boolean isOpen()
    throws OdaException
  {
    return this.isOpen;
  }


  public void open(Properties arg0)
    throws OdaException
  {
    this.isOpen = true;
  }

  public void rollback()
    throws OdaException
  {
  }

  public void setAppContext(Object arg0)
    throws OdaException
  {
  }

  public void setLocale(ULocale arg0)
    throws OdaException
  {
  }


public IQuery newQuery(String dataSetType) throws OdaException {

	 IQuery aQuery = (IQuery)this.queryMap.get(dataSetType);
	    if (aQuery != null) {
	      return aQuery;
	    }
	    aQuery = new RESTQuery();
	    this.queryMap.put(dataSetType, aQuery);

	    return aQuery;
}
}