package oda.datatools.connectivity.rest.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTResultSet
  implements IResultSet
{
  private int m_maxRows;
  private int m_currentRowId;
  private List<List<Object>> rows = new ArrayList<List<Object>>();
  private Iterator<List<Object>> rowIter;
  private List<Object> currentRow;
  private RESTResultSetMetaData resultSetMetaData;
  private RESTInterface searchRequest;
  
  
  public RESTResultSet(RESTInterface searchRequest,RESTResultSetMetaData resultSetMetaData) throws OdaException
  {
    this.resultSetMetaData = resultSetMetaData;
    this.searchRequest=searchRequest;
    setQuery();
  }

  public void close() throws OdaException
  {
    this.m_currentRowId = 0;
  }

  public int findColumn(String columnName) throws OdaException
  {
    for (int i = 1; i < getMetaData().getColumnCount(); i++) {
      if (getMetaData().getColumnName(i).equals(columnName)) {
        return i;
      }
    }
    return -1;
  }

  public BigDecimal getBigDecimal(int index)
    throws OdaException
  {
	if(this.currentRow.get(index-1)==null)
		return null;
	else
	{
		String ans=this.currentRow.get(index-1).toString();
		BigDecimal ansin=new BigDecimal(ans);
	    return ansin;
	}
	
  }

  public BigDecimal getBigDecimal(String columnName)
    throws OdaException
  {
    return getBigDecimal(findColumn(columnName));
  }

  public IBlob getBlob(int arg0)
    throws OdaException
  {
	//Currently not Used
    throw new UnsupportedOperationException();
  }

  public IBlob getBlob(String columnName)
    throws OdaException
  {
    return getBlob(findColumn(columnName));
  }

  public boolean getBoolean(int index)
    throws OdaException
  {

	if(this.currentRow.get(index-1)==null)
		return false;
	else
	{
		return ((Boolean)this.currentRow.get(index-1)).booleanValue();
	}
  }

  public boolean getBoolean(String columnName)
    throws OdaException
  {
    return getBoolean(findColumn(columnName));
  }

  public IClob getClob(int arg0)
    throws OdaException
  {
	  
		//Currently not Used
	    throw new UnsupportedOperationException();
  }

  public IClob getClob(String columnName)
    throws OdaException
  {
    return getClob(findColumn(columnName));
  }

  public Date getDate(int index)
    throws OdaException
  {  
	  if(this.currentRow.get(index-1)==null)
			return null;
	  else
	  {
		  String ans=this.currentRow.get(index-1).toString();
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			java.util.Date formatdate = null;
			java.sql.Date sqlDate =null;
		    try {
		    	formatdate=  dateformat.parse(ans);
		    	sqlDate=new java.sql.Date(formatdate.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sqlDate;
	  }
	
  }

  public Date getDate(String columnName)
    throws OdaException
  {
	 
    return getDate(findColumn(columnName));
  }

  public double getDouble(int index)
    throws OdaException
  {  
	  if(this.currentRow.get(index-1)==null)
		  return 0.0;
	  else
	  {
	  	String ans=this.currentRow.get(index-1).toString();
	    return new Double(ans);
	  }
  }

  public double getDouble(String columnName)
    throws OdaException
  {
    return getDouble(findColumn(columnName));
  }

  public int getInt(int index)
    throws OdaException
  {
	
	  if(this.currentRow.get(index-1)==null)
		  return 0;
	  else
	  {
		    String ans=this.currentRow.get(index-1).toString();
			Integer ansin=new Integer(ans);
		    return ansin;
	  }
	
  }

  public int getInt(String columnName)
    throws OdaException
  {
	 
    return getInt(findColumn(columnName));
  }

  public IResultSetMetaData getMetaData()
    throws OdaException
  {
    return this.resultSetMetaData;
  }

  public Object getObject(int index)
    throws OdaException
  {
    return this.currentRow.get(index-1);
  }

  public Object getObject(String arg0)
    throws OdaException
  {
    return null;
  }

  public int getRow()
    throws OdaException
  {
    return this.m_currentRowId;
  }
  public String getString(int index)
    throws OdaException
  {
	 
	  if(this.currentRow.get(index-1)==null)
		  return null;
	  else
	  {
		  return (String)this.currentRow.get(index-1);
	  }
   
  }

  public String getString(String columnname)
    throws OdaException
  {
	  if(this.currentRow.get(findColumn(columnname))==null)
		  return null;
	  else
	  {
		  return this.currentRow.get(findColumn(columnname)).toString();
	  }
  
  }

  public Time getTime(int index)
    throws OdaException
  {
	  if(this.currentRow.get(index-1)==null)
		  return null;
	  else
	  {
		  String ans=this.currentRow.get(index-1).toString();
		  SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		  java.util.Date formatdate = null;
		  java.sql.Date sqldate=null;
		  try {
			  formatdate= dateformat.parse(ans);
			  sqldate=new java.sql.Date(formatdate.getTime());
		  } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		 
		  Time time=new Time(sqldate.getTime());
	      return time;
	  }
	 
  }

  public Time getTime(String columnName)
    throws OdaException
  {
	  return getTime(findColumn(columnName));
  }

  public Timestamp getTimestamp(int index)
    throws OdaException
  {
	  if(this.currentRow.get(index-1)==null)
		  return null;
	  else
	  {
		  String ans=this.currentRow.get(index-1).toString();
		  SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		  java.util.Date formatdate = null;
		  java.sql.Date sqldate=null;
		  try {
			  formatdate=  dateformat.parse(ans);
			  sqldate=new java.sql.Date(formatdate.getTime());
		  } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  Timestamp tmp=new Timestamp(sqldate.getTime());
		  return tmp;
	  }
	 
  }

  public Timestamp getTimestamp(String columnName)
    throws OdaException
  {
    return getTimestamp(findColumn(columnName));
  }

  public void setQuery() throws OdaException
  {
		
	  
	    this.rows = this.searchRequest.executeQuery().getRows();
	    this.rowIter = this.rows.listIterator();
	    this.setMaxRows(this.rows.size());
	 
  }
  public boolean check()
  {
	    if (this.rowIter.hasNext())
	    {
	      this.m_currentRowId += 1;
	      this.currentRow = ((List<Object>)this.rowIter.next());
	      return true;
	     
	    }
	    else
	    {
	    	return false;
	    }
  }
  public boolean next()
    throws OdaException
  {

    if (check())
    {
    	return true;
    }
    else
    {
    	setQuery();
    
    	return check();
    }
   
  }

  public void setMaxRows(int max)
    throws OdaException
  {
    this.m_maxRows = max;
  }

  public boolean wasNull()
    throws OdaException
  {
    return false;
  }
  protected int getMaxRows() {
    return this.m_maxRows;
  }
}