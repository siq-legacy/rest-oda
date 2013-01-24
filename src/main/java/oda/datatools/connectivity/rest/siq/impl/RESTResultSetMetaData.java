package oda.datatools.connectivity.rest.siq.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
public class RESTResultSetMetaData
  implements IResultSetMetaData
{
  private List<String> colHeaders = new ArrayList<String>();
  protected static Logger logger = Logger.getLogger(RESTResultSetMetaData.class.getName());
  private RESTList siqlist;



public RESTResultSetMetaData(RESTList siqlist)
  {
	this.siqlist = siqlist;
    for (String tag : this.siqlist.getColumnlist())
      this.colHeaders.add(tag);
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

  public int getColumnType(int index)
    throws OdaException
  {
	  return  RESTDriver.getManifest().getDataSetType(RESTDriver.ODA_DATA_SET_ID).getDataTypeMapping(this.siqlist.getDatatype().get(index-1)).getOdaScalarDataTypeCode();
	  
  }

  public String getColumnTypeName(int index)
    throws OdaException
  {
	
	return RESTDriver.getManifest().getDataSetType(RESTDriver.ODA_DATA_SET_ID).getDataTypeMapping(this.siqlist.getDatatype().get(index-1)).getOdaScalarDataType();

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