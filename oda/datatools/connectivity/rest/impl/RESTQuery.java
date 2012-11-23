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
  private RESTList Restlist;
  private RESTResultSetMetaData resultsetmetadata;
  private RESTParameterMetaData parametermetadata;
  private Map<Integer, Object> Param_pos;
  private Map<String,Object>  Param_name;
  private RESTInterface restinterface;
  private AccessPattern accesspattern;
 
  public RESTQuery()
  {
	  Param_pos=new  HashMap<Integer,Object>();
	  Param_name=new  HashMap<String,Object>();
	  restinterface=new RESTInterface();
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
	parametermetadata=new RESTParameterMetaData(this.Param_pos);
	restinterface.prepare();
	restinterface.setRESTlist(Restlist);
    IResultSet  resultSet= new RESTResultSet(restinterface,this.resultsetmetadata);
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
	 
	 this.Restlist = new RESTList();
	 this.Restlist.setColumnlist(names);
	 this.Restlist.setDatatype(types);
	 resultsetmetadata=new RESTResultSetMetaData(this.Restlist);
  }

  public void setAppContext(Object context)
    throws OdaException
  {
    this.logger.finest("SIQ CONTEXT ");
  }

  public void setBigDecimal(String arg0, BigDecimal arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setBigDecimal(int arg0, BigDecimal arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }

  public void setBoolean(String arg0, boolean arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setBoolean(int arg0, boolean arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }

  public void setDate(String arg0, Date arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setDate(int arg0, Date arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }

  public void setDouble(String arg0, double arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setDouble(int arg0, double arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }

  public void setInt(String arg0, int arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
	
  }

  public void setInt(int arg0, int arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
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
	  this.Param_name.put(arg0, arg1);
	  accesspattern=(AccessPattern) arg1;
	  restinterface.setDatamappingstringlist(accesspattern.getDatamappingstring());
	  restinterface.setMethodlist(accesspattern.getMethod());
	  restinterface.setParamlist(accesspattern.getParam());
	  restinterface.setQuery(accesspattern.getQuery());
	  restinterface.setcolumnmappinglist(accesspattern.getColumnmappingstring());
	  
  }

  public void setObject(int arg0, Object arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
	  accesspattern=(AccessPattern) arg1;
	  restinterface.setDatamappingstringlist(accesspattern.getDatamappingstring());
	  restinterface.setMethodlist(accesspattern.getMethod());
	  restinterface.setParamlist(accesspattern.getParam());
	  restinterface.setQuery(accesspattern.getQuery());
	  restinterface.setcolumnmappinglist(accesspattern.getColumnmappingstring());
	  
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
	
	  this.Param_name.put(arg0, arg1);
	  
  }

  public void setString(int arg0, String arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
	
  }

  public void setTime(String arg0, Time arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setTime(int arg0, Time arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }

  public void setTimestamp(String arg0, Timestamp arg1)
    throws OdaException
  {
	  this.Param_name.put(arg0, arg1);
  }

  public void setTimestamp(int arg0, Timestamp arg1)
    throws OdaException
  {
	  this.Param_pos.put(arg0, arg1);
  }
}