package oda.datatools.connectivity.rest.siq.impl;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;

public class RESTDataSetMetaData
  implements IDataSetMetaData
{
  private IConnection m_connection;

  public RESTDataSetMetaData(IConnection connection)
  {
    this.m_connection = connection;
  }

  public IConnection getConnection()
    throws OdaException
  {
    return this.m_connection;
  }

  public int getDataSourceMajorVersion()
    throws OdaException
  {
    return 1;
  }

  public int getDataSourceMinorVersion()
    throws OdaException
  {
    return 0;
  }

  public IResultSet getDataSourceObjects(String arg0, String arg1, String arg2, String arg3)
    throws OdaException
  {
    throw new UnsupportedOperationException();
  }

  public String getDataSourceProductName()
    throws OdaException
  {
    return "SIQ DataSource";
  }

  public String getDataSourceProductVersion()
    throws OdaException
  {
    return Integer.toString(getDataSourceMajorVersion()) + "." + 
      Integer.toString(getDataSourceMinorVersion());
  }

  public int getSQLStateType()
    throws OdaException
  {
    return 1;
  }

  public int getSortMode()
  {
    return 0;
  }

  public boolean supportsInParameters()
    throws OdaException
  {
    return true;
  }

  public boolean supportsMultipleOpenResults()
    throws OdaException
  {
    return false;
  }

  public boolean supportsMultipleResultSets()
    throws OdaException
  {
    return false;
  }

  public boolean supportsNamedParameters()
    throws OdaException
  {
    return false;
  }

  public boolean supportsNamedResultSets()
    throws OdaException
  {
    return true;
  }

  public boolean supportsOutParameters()
    throws OdaException
  {
    return false;
  }
}