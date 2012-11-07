package oda.datatools.connectivity.rest.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

import siqcolumns.Siqcolumns;

public class RESTQuery
  implements IQuery
{
  private int m_maxRows;
  private String queryText;
  protected Logger logger = Logger.getLogger(RESTQuery.class.getName());
  private RESTList Restlist;
  private RESTResultSetMetaData resultsetmetadata;
  private RESTParameterMetaData parametermetadata;
  private Map<String,Object> Param;
  private SearchRequest searchRequest;
 
  public RESTQuery()
  {
	  Param=new  HashMap<String,Object>();
  }
  public void cancel()
    throws OdaException, UnsupportedOperationException
  {
  }
  
//this is for testing purpose
 /* private void testsiq(String methodName) throws OdaException
  {
    if ((this.Restlist == null) || (this.Restlist.getRows() == null) || (this.Restlist.getRows().size() == 0))
      throw new OdaException("Data Feed is null or empty in " + methodName);
  }
*/
  public void clearInParameters() throws OdaException
  {
    this.logger.finest("ClearInParameters");
   
  }
  
  public void close()
    throws OdaException
  {
    this.logger.finest("CLOSE");
    this.logger.finest("");
  }
  public IResultSet executeQuery()
    throws OdaException
  {
    this.logger.finest("EXECUTE QUERY");
	parametermetadata=new RESTParameterMetaData();
    parametermetadata.setParametercount(Param.size());
    searchRequest.setQueryText(this.queryText);
    searchRequest.setRESTlist(Restlist);
    IResultSet  resultSet= new RESTResultSet(searchRequest,this.resultsetmetadata,this.Param);
    resultSet.setMaxRows(getMaxRows());
    
    return resultSet;
  }


  public int findInParameter(String arg0)
    throws OdaException
  {
	  
    return 0;
  }

  public String getEffectiveQueryText()
  {
    return this.queryText;
  }

  public int getMaxRows()
    throws OdaException
  {
    this.logger.finest("GetMaxRows ");
    return this.m_maxRows;
  }

  public IResultSetMetaData getMetaData()
    throws OdaException
  {
    this.logger.finest("ResultSetMetaData");
    return resultsetmetadata;
  }

  public IParameterMetaData getParameterMetaData()
    throws OdaException
  {
    this.logger.finest("GetParameterMetaData");
    return parametermetadata;
  }

  public SortSpec getSortSpec()
    throws OdaException
  {
    return null;
  }

  public QuerySpecification getSpecification()
  {
	
    return null;
  }
  public void prepare(String queryText)
    throws OdaException
  {
	 this.queryText=queryText;
	 Siqcolumns columns=new Siqcolumns();
	 columns.nodeColumns();
	 this.Restlist = new RESTList();
	 this.Restlist.setColumnlist(columns.getColumns());
	 this.Restlist.setDatatype(columns.getDatatype());
	 resultsetmetadata=new RESTResultSetMetaData();
	 resultsetmetadata.setColumntag(this.Restlist);
  }

  public void setAppContext(Object context)
    throws OdaException
  {
    this.logger.finest("SIQ CONTEXT ");
  }

  public void setBigDecimal(String arg0, BigDecimal arg1)
    throws OdaException
  {
  }

  public void setBigDecimal(int arg0, BigDecimal arg1)
    throws OdaException
  {
  }

  public void setBoolean(String arg0, boolean arg1)
    throws OdaException
  {
  }

  public void setBoolean(int arg0, boolean arg1)
    throws OdaException
  {
  }

  public void setDate(String arg0, Date arg1)
    throws OdaException
  {
  }

  public void setDate(int arg0, Date arg1)
    throws OdaException
  {
  }

  public void setDouble(String arg0, double arg1)
    throws OdaException
  {
  }

  public void setDouble(int arg0, double arg1)
    throws OdaException
  {
  }

  public void setInt(String arg0, int arg1)
    throws OdaException
  {
	
  }

  public void setInt(int arg0, int arg1)
    throws OdaException
  {
	  
  }

  public void setMaxRows(int max)
    throws OdaException
  {
    this.logger.finest("SetMaxRows " + max);
    this.m_maxRows = max;
  }

  public void setNull(String arg0)
    throws OdaException
  {
  }

  public void setNull(int arg0)
    throws OdaException
  {
  }

  public void setObject(String arg0, Object arg1)
    throws OdaException
  {
	  this.Param.put(arg0, arg1);
	  searchRequest=(SearchRequest) arg1;
  }

  public void setObject(int arg0, Object arg1)
    throws OdaException
  {
	  
  }

  public void setProperty(String name, String value)
    throws OdaException
  {
	
    this.logger.finest("Property " + name + " : " + value);
  }

  public void setSortSpec(SortSpec arg0)
    throws OdaException
  {
    throw new UnsupportedOperationException();
  }

  public void setSpecification(QuerySpecification arg0)
    throws OdaException, UnsupportedOperationException
  {
	  
  }

  public void setString(String arg0, String arg1)
    throws OdaException
  {
	
	 
	  
  }

  public void setString(int arg0, String arg1)
    throws OdaException
  {
	
	
  }

  public void setTime(String arg0, Time arg1)
    throws OdaException
  {
  }

  public void setTime(int arg0, Time arg1)
    throws OdaException
  {
  }

  public void setTimestamp(String arg0, Timestamp arg1)
    throws OdaException
  {
  }

  public void setTimestamp(int arg0, Timestamp arg1)
    throws OdaException
  {
  }
}