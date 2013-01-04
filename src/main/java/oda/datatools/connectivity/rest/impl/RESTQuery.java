package oda.datatools.connectivity.rest.impl;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

public class RESTQuery
  implements IQuery
{
 
  private int m_maxRows;
  private String queryText;
  protected Logger logger = Logger.getLogger(RESTQuery.class.getName());
  private RESTList restList;
  private RESTResultSetMetaData resultsetMetaData;
  private RESTParameterMetaData parameterMetaData;
  private Map<Integer, Object> paramPositions;
  private Map<String,Object>  parameterNames;
  private RESTInterface restInterface;
  private AccessPattern accessPattern;
 
  public RESTQuery()
  {
	  paramPositions=new  HashMap<Integer,Object>();
	  parameterNames=new  HashMap<String,Object>();
	  restInterface=new RESTInterface();
  }
  public void cancel()
    throws OdaException, UnsupportedOperationException
  {
  }
  
//this is for testing purpose
 /* private void testsiq(String methodName) throws OdaException
  {
    if ((this.restList == null) || (this.restList.getRows() == null) || (this.restList.getRows().size() == 0))
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
	parameterMetaData=new RESTParameterMetaData(this.paramPositions);
	restInterface.prepare();
	restInterface.setRESTlist(restList);
    IResultSet  resultSet= new RESTResultSet(restInterface,this.resultsetMetaData);
    resultSet.setMaxRows(getMaxRows());
    
    return resultSet;
  }


  public int findInParameter(String paramPosition)
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
    this.logger.finest("resultsetMetaData");
    return resultsetMetaData;
  }

  public IParameterMetaData getParameterMetaData()
    throws OdaException
  {
    this.logger.finest("GetParameterMetaData");
    return parameterMetaData;
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
  @SuppressWarnings("unchecked")
public void prepare(String queryText)
    throws OdaException
  {
	 this.queryText=queryText;
	 int last_track=queryText.lastIndexOf('/');
	 CharSequence sequence=queryText.subSequence(last_track+1, queryText.length());
	 List<Class<?>> types;
	 List<String> names;
	 try
	 {
		 Class<?> cls = Class.forName("siqcolumns.Siqcolumns");
		 Object obj = cls.newInstance();
		 Class<?> noparams[] = {};
		 Method columnstype = cls.getDeclaredMethod(sequence.toString()+"Columns", noparams);
		 columnstype.invoke(obj, null);
		 Method columns_names = cls.getDeclaredMethod("getColumns", noparams);
	
		 names=(List<String>) columns_names.invoke(obj, null);
		 Method columns_types = cls.getDeclaredMethod("getDatatype", noparams);
		
		 types=(List<Class<?>>) columns_types.invoke(obj, null);
	 }
	 catch(Exception ex)
	 {
		 ex.printStackTrace();
		 throw new OdaException("Exception in the prepare Query");
		 
	 }
	 
	 this.restList = new RESTList();
	 this.restList.setColumnlist(names);
	 this.restList.setDatatype(types);
	 resultsetMetaData=new RESTResultSetMetaData(this.restList);
  }

  public void setAppContext(Object context)
    throws OdaException
  {
    this.logger.finest("SIQ CONTEXT ");
  }

  public void setBigDecimal(String paramName, BigDecimal paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setBigDecimal(int paramPosition, BigDecimal paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setBoolean(String paramName, boolean paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setBoolean(int paramPosition, boolean paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setDate(String paramName, Date paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setDate(int paramPosition, Date paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setDouble(String paramName, double paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setDouble(int paramPosition, double paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setInt(String paramName, int paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
	
  }

  public void setInt(int paramPosition, int paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setMaxRows(int max)
    throws OdaException
  {
    this.logger.finest("SetMaxRows " + max);
    this.m_maxRows = max;
  }

  public void setNull(String paramName)
    throws OdaException
  {
  }

  public void setNull(int paramPosition)
    throws OdaException
  {
  }

  public void setObject(String paramName, Object paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
	  accessPattern=(AccessPattern) paramValue;
	  restInterface.setDataMappingList(accessPattern.getDataMapping());
	  restInterface.setRequestMethodList(accessPattern.getRequestMethod());
	  restInterface.setParameterList(accessPattern.getRequestParameters());
	  restInterface.setQuery(accessPattern.getQueryString());
	  restInterface.setColumnMappingList(accessPattern.getColumnMapping());
	  
  }

  public void setObject(int paramPosition, Object paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
	  accessPattern=(AccessPattern) paramValue;
	  restInterface.setDataMappingList(accessPattern.getDataMapping());
	  restInterface.setRequestMethodList(accessPattern.getRequestMethod());
	  restInterface.setParameterList(accessPattern.getRequestParameters());
	  restInterface.setQuery(accessPattern.getQueryString());
	  restInterface.setColumnMappingList(accessPattern.getColumnMapping());
	  
  }

  public void setProperty(String name, String value)
    throws OdaException
  {
	
    this.logger.finest("Property " + name + " : " + value);
  }

  public void setSortSpec(SortSpec paramPosition)
    throws OdaException
  {
    throw new UnsupportedOperationException();
  }

  public void setSpecification(QuerySpecification paramPosition)
    throws OdaException, UnsupportedOperationException
  {
	  
  }

  public void setString(String paramName, String paramValue)
    throws OdaException
  {
	
	  this.parameterNames.put(paramName, paramValue);
	  
  }

  public void setString(int paramPosition, String paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
	
  }

  public void setTime(String paramName, Time paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setTime(int paramPosition, Time paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }

  public void setTimestamp(String paramName, Timestamp paramValue)
    throws OdaException
  {
	  this.parameterNames.put(paramName, paramValue);
  }

  public void setTimestamp(int paramPosition, Timestamp paramValue)
    throws OdaException
  {
	  this.paramPositions.put(paramPosition, paramValue);
  }
}