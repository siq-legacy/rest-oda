package oda.datatools.connectivity.rest.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTResultSetMetaData
  implements IResultSetMetaData
{
  private List<String> colHeaders = new ArrayList<String>();
  protected static Logger logger = Logger.getLogger(RESTResultSetMetaData.class.getName());
  private RESTList siqlist;

  public void setColumntag(RESTList siqlistentries)
  {
	  this.siqlist = siqlistentries;
	    for (String tag : siqlistentries.getColumnlist())
	      this.colHeaders.add(tag);
  }
  public RESTResultSetMetaData()
  {
    
  }

  public int getColumnCount() throws OdaException
  {
    return this.colHeaders.size();
  }

  public int getColumnDisplayLength(int arg0)
    throws OdaException
  {
    return 0;
  }

  public String getColumnLabel(int index)
    throws OdaException
  {
    return getColumnName(index);
  }

  public String getColumnName(int index)
    throws OdaException
  {
    return (String)this.colHeaders.get(index - 1);
  }

  public int getdatatype(int index)
  {
    List siqdatatype = this.siqlist.getDatatype();
    Class cls = (Class)siqdatatype.get(index);
 
    if (cls.getName() == "java.lang.String")
    {
      return 1;
    }
    if (cls.getName() == "java.lang.Boolean")
    {
      return 16;
    }
    if (cls.getName() == "java.lang.Integer")
    {
      return 4;
    }
    if (cls.getName() == "java.lang.BigInteger")
    {
      return -5;
    }
    if (cls.getName() == "java.text.SimpleDateFormat")
    {
      return -6;
    }
    if (cls.getName() == "java.util.Date")
    {
      return 91;
    }
    if (cls.getName() == "java.lang.Double")
    {
      return 8;
    }

   
    return 1;
  }

  public int getColumnType(int index)
    throws OdaException
  {
    
    return getdatatype(index-1);
  }

  public String getColumnTypeName(int index)
    throws OdaException
  {
    int nativeTypeCode = getColumnType(index);
    return RESTDriver.getNativeDataTypeName(nativeTypeCode);
  }

  public int getPrecision(int arg0)
    throws OdaException
  {
    return 0;
  }

  public int getScale(int arg0)
    throws OdaException
  {
    return 0;
  }

  public int isNullable(int arg0)
    throws OdaException
  {
    return 0;
  }
}